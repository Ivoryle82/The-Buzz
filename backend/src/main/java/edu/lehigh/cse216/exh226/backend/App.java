package edu.lehigh.cse216.exh226.backend;

import spark.Spark; // Import the Spark package, so we can use the get function to create an HTTP GET route

import com.google.gson.*; // Import Google's JSON library

import edu.lehigh.cse216.exh226.backend.controllers.*;
import edu.lehigh.cse216.exh226.backend.DatabaseRoutes;
import java.util.Map;

/**
 * For now, our app creates an HTTP server that is run via the localhost
 * (127.0.0.1), localhost is what makes the HTTP requests (until this gets
 * deployed to dokku servers).
 * 
 * The port I am using for the Java Spark requests is '4567'
 * The default port for the database is '5432'
 */
public class App {

    private static final String DEFAULT_URL_DB = "";
    private static final String DEFAULT_PORT_DB = "5432";
    private static final int DEFAULT_PORT_SPARK = 4567;

    /**
     * getDataBaseConnection() : establishes a connection to the database provided
     * by the DATABASE_URL enviornment variables, or using separate environment
     * variables that hold the fields of the DATABASE_URL
     *
     * @return : returns a Database object with an established connection to the
     *         cloud database passed via the DATABASE_URL. IE: now we can use the
     *         Database.java functionality to communicate with the SQL database.
     */
    private static Database getDataBaseConnection() {
        if (System.getenv("DATABASE_URL") != null) {
            return Database.getDatabase(System.getenv("DATABASE_URL"), DEFAULT_URL_DB);
        }
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        return Database.getDatabase(ip, port, "", user, pass);
    }

    /**
     * getIntFromEnv : searches the system's environment variables for "PORT", then
     * parses that value as an int
     * 
     * @param envar      : a String, the environment variable to search for
     * @param defaultVal : an int, the default integer value to use for the port if
     *                   envar is not found.
     * 
     * @returns : an int, either the defaultVal, or the value stored at envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    /**
     * main() : the main method for app.java.
     * 1. Ports Java spark to listen to HTTP request via the browser from localhost
     * 2. Establishes a connection to the database
     * 3. Contains all the routes for all the frontend's functionality
     * 
     * @param args : unused argument
     */
    public static void main(String[] args) {
        // Set the port on which to listen for requests from localhost
        Spark.port(getIntFromEnv("PORT", DEFAULT_PORT_SPARK));

        // Get a fully-configured connection to the database, or exit immediately
        Database db = getDataBaseConnection();
        if (db == null) {
            return;
        }

        // Import google's Gson library as gson. SEE:
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // I dont know why this is here, we should only ever getDatabase through the
        // getDatabaseConnection method
        // Database mdDatabase = Database.getDatabase(DEFAULT_URL_DB, DEFAULT_PORT_DB);

        // Set up the location for serving static files (currently to deploy front-end)
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

        /**
         * Route for serving the main page, redirects the browser to the DOM in
         * index.html
         * 
         * "http://localhost:4567/"
         */
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        /**
         * Route for logging in (GET). Queries the userTbl to see if the password
         * matches
         * with the username. If it does, return the username to the front-end. Serving
         * as a token that authorizes that corresponding user's interactions. Else, do
         * nothing (for now).
         * 
         * Parameters(data in request.body):
         * username : String
         * password : String
         * 
         * Tech Debt: We should probably create an authorization token randomly and have
         * it as field in the userTbl. Then, give that to the user.
         * We should also pass to the web front end something to indicate a failed login
         * attempt so the front-end can notify the user they got the password wrong
         * 
         * "http://localhost:4567/login/:username/:password"
         */

        /**
         * Route to return all messages (GET). Queries the messageTbl and returns all
         * elements.
         * 
         * Parameters(data in request.body):
         * none
         * 
         * "http://localhost:4567/messages"
         */

        /**
         * Route to view one message (GET). Queries the messageTbl and returns on
         * element
         * based on messageID.
         * 
         * Parameters(data in request.body):
         * none
         * 
         * "http://localhost:4567/messages:messageID"
         */

        /**
         * Route to add one message to messageTbl (POST)
         * 
         * Parameters(data in request.body):
         * title : String
         * content : String
         * username : String
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages:messageID/add"
         */

        /**
         * Route to update one message in messageTbl (PUT)
         * 
         * Parameters(data in request.body):
         * title : String
         * content : String
         * username : String
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages:messageID/edit"
         */

        /**
         * Route to delete one message in messageTbl (DELETE)
         * 
         * Parameters(data in request.body):
         * username : String, this is so a user can only delete his messages. SO this
         * must match the token given at login.
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages:messageID/edit"
         */

        /**
         * Route to like or unlike a message (PUT)
         * 
         * Parameters(data in request.body):
         * username : String, this is so a user can only delete his messages. SO this
         * must match the token given at login.
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages:messageID/like"
         */

        /**
         * Route to view user profile (GET)
         * 
         * Parameters(data in request.body):
         * none
         * 
         * Parameters(data in request url):
         * username : String
         * 
         * "http://localhost:4567/userprofiles:username"
         */

        /**
         * Route to update user profile (PUT)
         * 
         * Parameters(data in request.body):
         * newUsername : String
         * newPassword : String
         * newBio : String
         * newEmail : String
         * 
         * Parameters(data in request url):
         * username : String, (this is used for authentication, we will change this
         * later)
         * 
         * "http://localhost:4567/userprofiles:username/edit"
         * 
         * TECH DEBT: username passed like this has many flaws, plz read everything I
         * wrote about Tech Debt of create authentication token
         */

        /**
         * Route to delete user profile (DELETE)
         * 
         * Parameters(data in request.body):
         * none
         * 
         * Parameters(data in request url):
         * username : String, (this is used for authentication, we will change this
         * later)
         * 
         * "http://localhost:4567/userprofiles:username/delete"
         */

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
