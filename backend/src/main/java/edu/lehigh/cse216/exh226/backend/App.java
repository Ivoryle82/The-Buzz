package edu.lehigh.cse216.exh226.backend;

import spark.Spark; // Import the Spark package, so we can use the get function to create an HTTP GET route

import com.google.gson.*; // Import Google's JSON library
import java.util.Map;

/**
 * For now, our app creates an HTTP server with only one route
 * 
 * When an HTTP client connects to this erver on the default SPARK port (4567),
 * and request /hello, we return "Hello World". Otherwise, we produce an error
 */
public class App {
    /**
     * For connecting backend to SQL database.
     * 1. I need to store my postgres elephant sql stuff in environment variables
     * and then use them as parameters in the DataBase class I made that connects
     * to postgres sql server
     */
    private static final String DEFAULT_PORT_DB = "5432";
    private static final int DEFAULT_PORT_SPARK = 4567;

    private static Database getDataBaseConnection() {
        if (System.getenv("DATABASE_URL") != null) {
            return Database.getDatabase(System.getenv("DATABASE_URL"), DEFAULT_PORT_DB);
        }
        Map<String, String> env = System.getenv();
        // System.out.println("TEST: " + env);
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT"); // NB: I WILL NEED THIS LATER
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        return Database.getDatabase(ip, port, "", user, pass);
    }

    /**
     * Get an integer environment variable if it exists, and otherwise
     * return the default value
     * 
     * @envar The name of the enviornment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    public static void main(String[] args) {

        // Get a fully-configured connection to the database, or exit immediately
        Database db = getDataBaseConnection();
        if (db == null) {
            return;
        }

        // gson provides us with a way to turn JSON into objects & objects into JSON
        //
        // NB: gson is thread-safe
        // SEE:
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // dataStore holds all of the data that has been provided via HTTP requests
        //
        // NB: everytime we shut down the server, we will lose all data, and
        // everytime we start the server, we'll have an empty dataStore,
        // with IDs starting over from 0.
        /**
         * Im going to connect DataStore object to the db connection so it can use the
         * prepared
         * statements when performing operations specified by the Spark get, put, post,
         * delete
         * TECH DEBT: DataStore and Database are 2 very similar classes, in the future
         * DataStore should be reduced or even removed as it is only a temporay instance
         * to
         * transfer data while Database is permenant as it connects to elephantsql
         * postgres database
         */
        /**
         * ACTUALLY INSTEAD OF THIS IM JUST GOING TO DELETE THE DataStore class and
         * refactor
         * the code below to use the database connection
         */
        final DataStore dataStore = new DataStore();

        // Set the port on which to listen for requests from the environment
        Spark.port(getIntFromEnv("PORT", DEFAULT_PORT_SPARK));

        // Set up the location for serving static files
        // If the STATIC_LOCATION environment variable is set, we will serve it.
        // Otherwise, serve from /web
        // This, combined with the bash script allows us to edit web front end elements
        // and not have to re package the program. However, we will have to rerun the
        // bash, Script to copy the files back into a re-made web folder
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> { // a request for "/" is same as "http://localhost:4567/"
            res.redirect("/index.html");
            return "";
        });

        // GET route that returns all message titles and Ids. All we do is get the data,
        // embed it in a StructuredResponse, then turn it into JSON and return it.
        // If there is no data, return "[]", so there's no need for error handling
        Spark.get("/messages", (request, response) -> {
            // ensure status is 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAll()));
        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"),
        // so that we can get the requested row ID. If ":id" isn't a number, Spark will
        // reply with status 500 Internal Server Error. Otherwise, we have an integer,
        // and the only possible error is that it doesn't corresponding to a row with
        // data
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            DataRow data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // POST route for adding a new element to the DataStore. This will read JSON
        // from the body of the request, turn it into a SimpleRequest object, extract
        // the title and message, insert them, and return the ID of the newly created
        // one
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal Server
            // Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // Ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object describing the error
            response.status(200);
            response.type("application/json");

            // NB: createEntry already checks for null title and message
            int newId = db.insertRow(req.mTitle, req.mMessage);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "rows added: " + newId, null));
            }
        });

        /**
         * PUT route for updating a row in the DataStore, almost same as POST
         * 
         * Changing it to update a row in the DataBase, going to delete req.mTitle as I
         * will
         * not need to update the titles (note: this is some tech debt, i should add the
         * functionality
         * to update titles later)
         */
        Spark.put("/messages/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);

            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // DataRow result = dataStore.updateOne(idx, req.mTitle, req.mMessage);
            int result = db.updateOne(idx, req.mMessage);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, "rows updated: " + result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/messages/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));

            // ensure status 200 OK, and MIME type of JSON
            response.status(200);
            response.type("application/json");

            // NB: We aren't concerned with the message quality of a successful delete
            // boolean result = dataStore.deleteOne(idx);
            int result = db.deleteRow(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // Spark.get("/hello", (request, response) -> {
        // return "Hello World!";
        // });
    } // Passing a lambda (anonymous function) to the get function

}

/**
 * You should also consider refactoring your code at this point. In both
 * App.java
 * and DataStore.java, there are redundant operations that could be moved to
 * separate
 * functions to improve reuse. It would also be worthwhile to use try/catch
 * blocks to
 * catch the exceptions that are leading to Spark generating 500 Internal server
 * error
 * messages, and instead sending back JSON. Doing so will make the front-end we
 * eventually write a little bit simpler.
 */
