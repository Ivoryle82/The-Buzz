package edu.lehigh.cse216.exh226.backend;

import spark.Spark; // Import the Spark package, so we can use the get function to create an HTTP GET route

import com.google.gson.*; // Import Google's JSON library

import edu.lehigh.cse216.exh226.backend.controllers.*;
//import edu.lehigh.cse216.exh226.backend.DatabaseRoutes;
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

    // private static final String DEFAULT_URL_DB = "";
    // WE NEVER STORE THE DATABASE URL IN THE APP, THAT WOULD LEAK OUR PASSWORD
    // GIVING ANYBODY CONTROL OVER ALL ACTIONS IN OUR DATABASE
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
            return Database.getDatabase(System.getenv("DATABASE_URL"), DEFAULT_PORT_DB);
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
        Spark.get("/", (request, response) -> {
            response.redirect("/index.html");
            return "";
        });

        /**
         * Route for signing up (POST). Creates a new entry in the userTbl.
         * Does not return any data to the front end.
         * TECH DEBT: After sign up it should return a token that is similar to the
         * login token.
         * 
         * Parameters(data in request.body):
         * mBio : A String for the bio
         * mEmail : A String for the email
         * 
         * Parameters(data in request.params):
         * username : String
         * password : String
         */
        Spark.post("/signup/:username/:password", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            String username = request.params("username");
            String password = request.params("password");
            UserDataRow userData = gson.fromJson(request.body(), UserDataRow.class);
            String bio = userData.mBio;
            String email = userData.mEmail;

            int numUsersAdded = db.insertUserTblRow(username, password, bio, email);
            return gson.toJson(new StructuredResponse("ok", "number of users added: " + numUsersAdded, null));
        });

        /**
         * Route for logging in (GET). Queries the userTbl to see if the password
         * matches
         * with the username. If it does, return the username to the front-end. Serving
         * as a token that authorizes that corresponding user's interactions. Else, do
         * nothing (for now).
         * 
         * Parameters(data in request.body):
         * none
         * 
         * Parameters(data in request.params):
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
        Spark.get("/login/:username/:password", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            String username = request.params("username");
            String password = request.params("password");
            UserDataRow user = db.selectOneUserTblRow(username);
            if (user.getPassword().equals(password)) {
                return gson.toJson(new StructuredResponse("ok", null, username));
            } else {
                return gson.toJson(new StructuredResponse("error", "failed login", null));
            }
        });

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
        Spark.get("/userprofiles:username", (request, response) -> {
            String username = request.params("username");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectOneUserTblRow(username)));
        });

        /**
         * Route to update user profile (PUT)
         * 
         * Parameters(data in request.body):
         * mNewUsername : String
         * mPassword : String
         * mBio : String
         * mEmail : String
         * mUsername : String (this is for authentication)
         * 
         * Parameters(data in request url):
         * username : String
         * 
         * "http://localhost:4567/userprofiles:username/edit"
         * 
         * TECH DEBT: username passed like this has many flaws, plz read everything I
         * wrote about Tech Debt of creating an authentication token
         */
        Spark.put("/userprofiles:username/edit", (request, response) -> {
            String username = request.params("username");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            UserRequest reqData = gson.fromJson(request.body(), UserRequest.class);
            UserDataRow userData = db.selectOneUserTblRow(username);

            if (reqData.mUsername.equals(userData.mUsername)) { // Front end is logged in, we are allowed to edit
                db.updateOneUserTblRow(userData.mUsername, reqData.mNewUsername, reqData.mPassword, reqData.mBio,
                        reqData.mEmail);
                return gson.toJson(new StructuredResponse("ok", "user updated profile", null));
            } else {
                return gson.toJson(new StructuredResponse("error", "tried to edit a user that wasnt yours", null));
            }
        });

        /**
         * Route to delete user profile (DELETE)
         * 
         * Parameters(data in request.body):
         * mUsername : String (for authentication)
         * 
         * Parameters(data in request url):
         * username : String
         * 
         * "http://localhost:4567/userprofiles:username/delete"
         */
        Spark.delete("/userprofiles:username/delete", (request, response) -> {
            String username = request.params("username");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            UserRequest reqData = gson.fromJson(request.body(), UserRequest.class);
            UserDataRow userData = db.selectOneUserTblRow(username);

            if (reqData.mUsername.equals(userData.mUsername)) { // Front end is logged in, we are allowed to delete
                db.deleteOneUserTblRow(userData.mUsername);
                return gson.toJson(new StructuredResponse("ok", "user deleted profile", null));
            } else {
                return gson.toJson(new StructuredResponse("error", "tried to delete a user that wasnt yours", null));
            }
        });

        /**
         * Route to return all messages (GET). Queries the messageTbl and returns all
         * elements.
         * 
         * Parameters(data in request.body):
         * none
         * 
         * "http://localhost:4567/messages"
         */
        Spark.get("/messages", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllMessageTblRow()));
        });

        /**
         * Route to return all messages for a specific user (GET). Queries the
         * messageTbl and returns all elements.
         * 
         * Parameters(data in request.body):
         * none
         * 
         * "http://localhost:4567/messages:username"
         */
        Spark.get("/messages:username", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            String username = request.params("username");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllMessageTblRowForUser(username)));
        });

        /**
         * Route to view one message (GET). Queries the messageTbl and returns on
         * element
         * based on messageID.
         * 
         * Parameters(data in request.body):
         * none
         * 
         * Parameters(data in request url):
         * messageID : String
         * 
         * "http://localhost:4567/messages:messageID"
         */
        Spark.get("/messages:messageID", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            String messageID = request.params("messageID");
            return gson.toJson(new StructuredResponse("ok", null, db.selectOneMessageTblRow(messageID)));
        });

        /**
         * Route to add one message to messageTbl (POST)
         * 
         * Parameters(data in request.body):
         * mUsername : String
         * mMessageID : String
         * mTitle : String
         * mContent : String
         * mLikeCount : int
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages/add"
         */
        Spark.post("/messages:messageID/add", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            String messageID = request.params("messageID");
            MessageDataRow data = gson.fromJson(request.body(), MessageDataRow.class);
            int numRows = db.insertMessageTblRow(data.mUsername, messageID, data.mTitle, data.mContent,
                    data.mLikeCount);
            return gson.toJson(new StructuredResponse("ok", "total messages: " + numRows, null));
        });

        /**
         * Route to update one message in messageTbl (PUT)
         * 
         * Parameters(data in request.body):
         * mUsername : String
         * mTitle : String
         * mContent : String
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages:messageID/edit"
         */
        Spark.put("/messages:messageID/edit", (request, response) -> {
            String messageID = request.params("messageID");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            MessageDataRow reqData = gson.fromJson(request.body(), MessageDataRow.class);
            MessageDataRow msgData = db.selectOneMessageTblRow(messageID);
            if (reqData.mUsername.equals(msgData.mUsername)) {
                int numRows = db.updateOneMessageTblRow(messageID, reqData.mTitle, reqData.mContent);
                return gson.toJson(new StructuredResponse("ok", "total messages: " + numRows, null));
            } else {
                return gson.toJson(new StructuredResponse("error", "tried to edit a message that wasnt yours", null));
            }
        });

        /**
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * SOMETHING VERY IMPORTANT
         * THE JSON KEYS IN FOR THE DATA MUST MATCH THE VARIABLE NAMES OF THE CLASS
         * OBJECT THE GSON LIBRARY IS PARSING THAT JSON DATA TO
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */

        /**
         * Route to delete one message in messageTbl (DELETE)
         * 
         * Parameters(data in request.body):
         * mUsername : String, this is so a user can only delete his messages. SO this
         * must match the token given at login.
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages:messageID/delete"
         */
        Spark.delete("/messages:messageID/delete", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            String messageID = request.params("messageID");
            // ensure status 200 OK, and MIME type of JSON
            response.status(200);
            response.type("application/json");
            String reqUsername = gson.fromJson(request.body(), MessageDataRow.class).mUsername;
            String msgUsername = db.selectOneMessageTblRow(messageID).mUsername;

            if (reqUsername.equals(msgUsername)) {
                return db.deleteOneMessageTblRow(messageID);
            } else {
                return gson.toJson(new StructuredResponse("error", "unable to delete row (not your message)", null));
            }
        });

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
        Spark.put("/messages:messageID/like", (request, response) -> {
            int result;
            String messageID = request.params("messageID");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            String username = gson.fromJson(request.body(), MessageDataRow.class).mUsername;
            int likeCount = db.selectOneMessageTblRow(messageID).mLikeCount;
            UserLikesDataRow userLikesData = db.selectOneUserLikesTblRow(username, messageID);

            if (userLikesData == null) { // we haven't liked the message yet
                likeCount++;
                result = db.updateOneMessageTblRowLikes(messageID, likeCount);
            } else {
                likeCount--;
                result = db.updateOneMessageTblRowLikes(messageID, likeCount);
            }

            return gson.toJson(new StructuredResponse("ok", "total likes: " + result, null));
        });
    }
}

/**
 * TECHNICAL DEBT: IN A LOT OF THESE MEHTODS I KINDA IGNORED THE RETURN VALUE
 * FROM SOME OF THE DATABASE METHODS
 * --> ADD THE PROPER FUNCTIONALITY FOR ALL DATABASE METHODS SO THEY GIVE A
 * SPECIFIC RETURN VALUE IF THE METHOD FAILS
 * --> THEN MAKE SURE ALL THE ROUTES HANDLE THAT METHOD CORRECTLY AND SET THE
 * PROPER STATUS CODE FOR EACH INTERACTION
 */