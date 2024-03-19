package edu.lehigh.cse216.exh226.backend;

import spark.Spark; // Import the Spark package, so we can use the get function to create an HTTP GET route

import com.google.gson.*; // Import Google's JSON library

import edu.lehigh.cse216.exh226.backend.controllers.*;
import edu.lehigh.cse216.exh226.backend.DatabaseRoutes;
import java.util.Map;

/**
 * For now, our app creates an HTTP server with only one route
 * 
 * When an HTTP client connects to this erver on the default SPARK port (4567),
 * and request /hello, we return "Hello World". Otherwise, we produce an error
 */
public class App {

    private static final String DEFAULT_URL_DB = "";
    private static final String DEFAULT_PORT_DB = "5432";
    private static final int DEFAULT_PORT_SPARK = 4567;

    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
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

    public static void main(String[] args) {
        // Set the port on which to listen for requests from the environment
        Spark.port(getIntFromEnv("PORT", DEFAULT_PORT_SPARK));

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

        Database mdDatabase = Database.getDatabase(DEFAULT_URL_DB, DEFAULT_PORT_DB);
        // dataStore holds all of the data that has been provided via HTTP requests
        //
        // NB: everytime we shut down the server, we will lose all data, and
        // everytime we start the server, we'll have an empty dataStore,
        // with IDs starting over from 0.

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

        // prob dont need
        // final DataStore dataStore = new DataStore();

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> { // a request for "/" is same as "http://localhost:4567/"
            res.redirect("/index.html");
            return "";
        });

    }

    /**
     * For connecting backend to SQL database.
     * 1. I need to store my postgres elephant sql stuff in environment variables
     * and then use them as parameters in the DataBase class I made that connects
     * to postgres sql server
     */

    private static Database getDataBaseConnection() {
        if (System.getenv("DATABASE_URL") != null) {
            return Database.getDatabase(System.getenv("DATABASE_URL"), DEFAULT_URL_DB);
        }
        Map<String, String> env = System.getenv();
        // System.out.println("TEST: " + env);
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT"); // NB: I WILL NEED THIS LATER
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        return Database.getDatabase(ip, port, "", user, pass);
    }

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
