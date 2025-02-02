package edu.lehigh.cse216.exh226.backend;

import spark.Spark; // Import the Spark package, so we can use the get function to create an HTTP GET route

import com.google.gson.*; // Import Google's JSON library

// Import google's oauth api
import com.google.api.client.http.HttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.xml.stream.events.Comment;

import java.util.Date; // for createSessionToken & checkSessionToken
import java.lang.Math; // for createSessionToken
import java.text.SimpleDateFormat;
import java.text.ParseException;

// Imports for exchangeCodeForIdToken
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
    private static final String CLIENT_ID = "233303483347-q0bt1d0gt235ji0k0nna3ilufa6d35qr.apps.googleusercontent.com";
    private static final String DISCOVERY_DOCUMENT = "https://accounts.google.com/.well-known/openid-configuration";

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends. This only needs to be called once.
     * 
     * @param origin  The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any
        // get/post/put/delete. In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

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
     * exchangeCodeForIdToken : exchanges the code the is sent from the
     * authentication server for an id_token, which will then be verified
     * in the redrect route (this will be called in the redirect route).
     * 
     * To get the id token I need to send an HTTP POST to the google token endpoint
     * https://oauth2.googleapis.com/token. Java Spark cannot handle this because
     * this is an HTTP REQUEST to an external endpoint. (Basically, its like the web
     * ajax call, which is not Java Spark)
     * 
     * @param oneTimeCode : A String for the oneTiemCode
     * @param gson        : the gson library so I can work with JSON data
     * 
     * @return : A String for the id_token
     */
    public static String exchangeCodeForIdToken(String oneTimeCode, Gson gson) throws Exception {
        // System.out.println("TESTING: WE ARE IN exchangeCodeForIdToken method start");
        String clientId = CLIENT_ID;
        String clientSecret = System.getenv("CLIENT_SECRET");
        String redirectUri = "https://team-goku.dokku.cse.lehigh.edu/oauth-redirect";
        String grantType = "authorization_code";
        String idTokenString = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };

            HttpGet httpGet = new HttpGet(DISCOVERY_DOCUMENT);
            // System.out.println("Executing request " + httpGet.getRequestLine());
            String responseBody = httpClient.execute(httpGet, responseHandler);
            // System.out.println("RESPONSE BODY FOR DISCOVERY DOCUMENT: \n" +
            // responseBody);
            // Access token_endpoint data
            String tokenEndpoint = gson.fromJson(responseBody, OAuthData.class).token_endpoint;
            System.out.println("TOKEN ENDPOINT: \n" + tokenEndpoint);

            // Make the HTTP POST to the token endpoint
            HttpPost httpPost = new HttpPost(tokenEndpoint);
            // Build the request body:
            String requestBody = String.format(
                    "{\'code\': \'%s\', \'client_id\': \'%s\', \'client_secret\': \'%s\', \'redirect_uri\': \'%s\', \'grant_type\': \'%s\'}",
                    oneTimeCode, clientId, clientSecret, redirectUri, grantType);
            // Set the request body of the HTTP POST:
            StringEntity stringEntity = new StringEntity(requestBody);
            httpPost.setEntity(stringEntity);
            responseBody = ""; // empty the response body
            responseBody = httpClient.execute(httpPost, responseHandler);
            idTokenString = gson.fromJson(responseBody, OAuthData.class).id_token;
            // System.out.println("========================\nID_TOKEN: \n" + idTokenString);

        } finally {
            httpClient.close();
        }
        return idTokenString; // WORKS!!!
    }

    /**
     * createSessionToken: creates a new session token in the sessionTokenTbl
     * Used in the login route which will return this session token
     * This method will use Java Math Random to generate a random large integer
     * Then query the sessionTokenTbl to see if that integer already exists,
     * If it does, repeat until we get a unique random large int
     * 
     * Create a new entry in the sessionTokenTbl. This will have the unique session
     * token and the Date when it was created
     * 
     * @param database : the database object (so I can work with the database)
     * 
     * @return sessionToken : an integer for the unique session token
     */
    public static int createSessionToken(Database db) {
        int newSessionToken = 0;
        while (true) {
            newSessionToken = (int) (Math.random() * 1e9);
            SessionTokenDataRow sessionTokenDataRow = db.selectOneSessionTokenTblRow(newSessionToken);
            if (sessionTokenDataRow == null) {
                break;
            }
        }
        Date dateCreated = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(dateCreated);
        db.insertSessionTokenTblRow(newSessionToken, dateString);
        return newSessionToken;
    }

    /**
     * checkSessionToken: compares a session token (will get from frontend)
     * This method will query the sessionTokenTbl for the session token.
     * If the session token is null, then redirect to session timeout html page
     * (this is a safety measure, but wont be entirely needed)
     * Then, it will get that entry's Date attribute and parse it into a Java Date
     * class.
     * Then, compare the current Date to the Date of the session token.
     * If the difference is more than 5 hours then return false, else return true
     * 
     * This will be used in all routes and if false, the route should redirect
     * the page to a timeout html page and cancel the route. Also the backend
     * should delete the session token from the sessionTokenTable
     * 
     * @param sessionToken : an int for the session token (from the front end)
     * 
     * @return valid : a boolean for if the session token is valid
     */
    public static boolean checkSessionToken(Database db, int sessionToken) {
        // first get the session token from database parse to Date
        SessionTokenDataRow sessionTokenDataRow = db.selectOneSessionTokenTblRow(sessionToken);
        Date currentDate = new Date();
        Date sessionTokenDateCreated = sessionTokenDataRow.mDateCreated;
        // Calculate the time difference in milliseconds
        long timeDifference = currentDate.getTime() - sessionTokenDateCreated.getTime();
        // Convert milliseconds to hours
        long hoursDifference = timeDifference / (1000 * 60 * 60);
        if (hoursDifference > 5) {
            return false;
        } else {
            return true;
        }
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
         * Enable CORS, MUST BE DONE BEFORE SETTING UP ROUTES
         */
        if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
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
         * oauth-redirect route: google server will redirect to this route on successful
         * sign in.
         * 
         * Web makes an HTTP request to:
         * https://accounts.google.com/o/oauth2/v2/auth?
         * response_type=code&
         * client_id=233303483347-q0bt1d0gt235ji0k0nna3ilufa6d35qr.apps.googleusercontent.com&
         * scope=openid%20email%20profile&
         * redirect_uri=https://team-goku.dokku.cse.lehigh.edu/oauth-redirect&
         * nonce=0394852-3190485-2490358& (THE WEB NEEDS TO GENERATE THIS)
         * 
         * oauth-redirect: creates a new user in the userTbl (essentially replaces the
         * signup route) if the user is not in the userTbl. Creates a new session token
         * and adds it to the sessionTokenTbl
         * 
         * Returns: a new session token (an int) for the valid session token in the
         * sessionTokenTbl
         * 
         */
        Spark.get("/oauth-redirect", (request, response) -> {
            // System.out.println("TESTING 1");
            int sessionToken = 0;
            // getting the one time code:
            String oneTimeCode = request.queryParams("code");
            // System.out.println("TEST oneTimeCode: " + oneTimeCode);
            // exchanging the one time code for a id_token:
            String idTokenString = exchangeCodeForIdToken(oneTimeCode, gson);
            // System.out.println("TEST idTokenString: \n" + idTokenString);

            // Verifying the id_token:
            // Create the token verifier, used for verifying the GoogleIdToken object
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport,
                    jsonFactory)
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    // Or, if multiple clients access the backend:
                    // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Get profile information from payload
                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                // System.out.println("name: " + name);
                // System.out.println("User ID: " + userId);
                // System.out.printf("Test profile info: \n %s, %s", email, emailVerified);

                // Query the database for the userId
                UserDataRow user = db.selectOneUserTblRow(userId);
                if (user == null) { // insert user into the userTbl
                    db.insertUserTblRow(userId, name, email, "");
                }
                // Now I need to create and return a session token
                response.redirect("/");
                sessionToken = createSessionToken(db);
            } else {
                // handle an invalid ID token
                System.out.println("Invalid ID token.");
                response.status(400);
                response.type("application/json");
                return gson.toJson(new StructuredResponse("error", "Invalid ID token", null));
            }

            return gson.toJson(new StructuredResponse("ok", "", sessionToken));
        });

        /**
         * Route to view user profile (GET)
         * 
         * Parameters(data in request.body):
         * none
         * 
         * Parameters(data in request url):
         * userID : String
         * 
         * "http://localhost:4567/userprofiles/:userID"
         */
        Spark.get("/userprofiles/:userID", (request, response) -> {
            String userID = request.params("userID");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectOneUserTblRow(userID)));
        });

        /**
         * TECH DEBT: MAKE SO ONLY USER CAN UPDATE THEIR USER PROFILE
         * Route to update user profile (PUT)
         * 
         * Parameters(data in request.body):
         * mNewUsername : String
         * mEmail : String
         * mBio : String
         * 
         * Parameters(data in request url):
         * userID : String
         * 
         * "http://localhost:4567/userprofiles/:userID"
         * 
         */
        Spark.put("/userprofiles/:userID", (request, response) -> {
            String userID = request.params("userID");
            UserDataRow newUserData = gson.fromJson(request.body(), UserDataRow.class);
            String newUsername = newUserData.mNewUsername;
            String newEmail = newUserData.mEmail;
            String newBio = newUserData.mBio;
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneUserTblRow(userID, newUsername, newEmail, newBio);
            if (result != -1) {
                return gson.toJson(new StructuredResponse("ok", "user updated profile", null));
            } else {
                return gson.toJson(new StructuredResponse("error", "error updating profile", null));
            }
        });

        /**
         * Route to delete user profile (DELETE)
         * 
         * Parameters(data in request.body):
         * mUsername : String (for authentication) NOT IMPLEMENTED YET
         * 
         * Parameters(data in request url):
         * userID : String
         * 
         * "http://localhost:4567/userprofiles/:userID"
         */
        Spark.delete("/userprofiles/:username", (request, response) -> {
            String userID = request.params("userID");
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            // UserRequest reqData = gson.fromJson(request.body(), UserRequest.class);
            // UserDataRow userData = db.selectOneUserTblRow(userID);
            db.deleteOneUserTblRow(userID);
            return gson.toJson(new StructuredResponse("ok", "user deleted profile", null));
        }); // END OF USERTBL ROUTES

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
         * "http://localhost:4567/messages/msgsByUser/:username"
         */
        Spark.get("/messages/msgsByUser/:username", (request, response) -> {
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
         * "http://localhost:4567/messages/:messageID"
         */
        Spark.get("/messages/:messageID", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int messageID = Integer.parseInt(request.params("messageID"));
            return gson.toJson(new StructuredResponse("ok", null, db.selectOneMessageTblRow(messageID)));
        });

        /**
         * Route to add one message to messageTbl (POST)
         * 
         * Parameters(data in request.body):
         * mUserID : String
         * mTitle : String
         * mContent : String
         * 
         * Parameters(data in request url):
         * None
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages"
         */
        Spark.post("/messages", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            int maximumMessageID = db.selectMaxMessageID() + 1;
            response.status(200);
            response.type("application/json");
            // String messageID = request.params("messageID");
            MessageDataRow data = gson.fromJson(request.body(), MessageDataRow.class);
            int numRows = db.insertMessageTblRow(data.mUserID, maximumMessageID, data.mTitle, data.mContent);
            return gson.toJson(new StructuredResponse("ok", "total messages: " + numRows, null));
        });

        /**
         * Route to update one message in messageTbl (PUT)
         * 
         * Parameters(data in request.body):
         * mUserID : String
         * mTitle : String
         * mContent : String
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages/:messageID"
         */
        Spark.put("/messages/:messageID", (request, response) -> {
            int messageID = Integer.parseInt(request.params("messageID"));
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            MessageDataRow reqData = gson.fromJson(request.body(), MessageDataRow.class);
            MessageDataRow msgData = db.selectOneMessageTblRow(messageID);
            if (reqData.mUserID.equals(msgData.mUserID)) {
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
         * mUserID : String, this is so a user can only delete his messages. SO this
         * must match the token given at login.
         * 
         * Parameters(data in request url):
         * messageID : String (this will not be a required parameter after backend makes
         * functionality to update the messageID as a unique int)
         * 
         * "http://localhost:4567/messages/:messageID"
         * 
         * Tech Debt: Refactor the routes to have a / before parameters and also take
         * the verbs out of the uri
         */
        Spark.delete("/messages/:messageID", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int messageID = Integer.parseInt(request.params("messageID"));
            // ensure status 200 OK, and MIME type of JSON
            response.status(200);
            response.type("application/json");
            String reqUsername = gson.fromJson(request.body(), MessageDataRow.class).mUserID;
            String msgUsername = db.selectOneMessageTblRow(messageID).mUserID;

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
         * mUserID : String, this is so a user can only delete his messages. SO this
         * must match the token given at login.
         * mSessionToken : an int for the session token
         * 
         * Parameters(data in request url):
         * messageID : a String that will be parsed to an int
         * 
         * "http://localhost:4567/messages/:messageID/like"
         */
        Spark.put("/messages/:messageID/like", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int sessionToken = gson.fromJson(request.body(), SessionTokenDataRow.class).mSessionToken;
            boolean validSessionToken = checkSessionToken(db, sessionToken);
            if (validSessionToken) {
                int result;
                int messageID = Integer.parseInt(request.params("messageID"));
                String userID = gson.fromJson(request.body(), MessageDataRow.class).mUserID;
                UserLikesDataRow userLikesData = db.selectOneUserLikesTblRow(userID, messageID);
                if (userLikesData == null) { // we havent disliked the message yet
                    int likeCount = db.selectOneMessageTblRow(messageID).mLikeCount;
                    result = db.updateOneMessageTblRowLikes(messageID, likeCount++);
                    db.insertUserLikesTblRow(userID, messageID); // insert a userDislikes entry to confirm the
                                                                 // dislike
                } else {
                    int likeCount = db.selectOneMessageTblRow(messageID).mLikeCount;
                    result = db.updateOneMessageTblRowLikes(messageID, likeCount--);
                    db.deleteOneUserLikesTblRow(userID, messageID); // delete a userDislikes entry to confirm the
                                                                    // dislike removed
                }
                return gson.toJson(new StructuredResponse("ok", "total likes: " + result, null));
            } else {
                response.redirect("/");
                return gson.toJson(new StructuredResponse("error", "Invalid Session", null));
            }
        });

        /**
         * Route to dislike or undislike a message (PUT)
         * 
         * Parameters(data in request.body):
         * mSessionToken : an int for the session token
         * 
         * Parameters(data in request url):
         * messageID : a String that will be parsed to an int
         * 
         * "http://localhost:4567/messages/:messageID/dislike"
         */
        Spark.put("/messages/:messageID/dislike", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int sessionToken = gson.fromJson(request.body(), SessionTokenDataRow.class).mSessionToken;
            boolean validSessionToken = checkSessionToken(db, sessionToken);
            if (validSessionToken) {
                int result;
                int messageID = Integer.parseInt(request.params("messageID"));
                String userID = gson.fromJson(request.body(), MessageDataRow.class).mUserID;
                UserDislikesDataRow userDislikesData = db.selectOneUserDislikesTblRow(userID, messageID);
                if (userDislikesData == null) { // we havent disliked the message yet
                    int dislikeCount = db.selectOneMessageTblRow(messageID).mDislikeCount;
                    result = db.updateOneMessageTblRowDislikes(messageID, dislikeCount++);
                    db.insertUserDislikesTblRow(userID, messageID); // insert a userDislikes entry to confirm the
                                                                    // dislike
                } else {
                    int dislikeCount = db.selectOneMessageTblRow(messageID).mDislikeCount;
                    result = db.updateOneMessageTblRowDislikes(messageID, dislikeCount--);
                    db.deleteOneUserDislikesTblRow(userID, messageID); // delete a userDislikes entry to confirm the
                                                                       // dislike removed
                }
                return gson.toJson(new StructuredResponse("ok", "total dislikes: " + result, null));
            } else {
                response.redirect("/");
                return gson.toJson(new StructuredResponse("error", "Invalid Session", null));
            }
        });

        /**
         * Route to view multiple comments (GET)
         * 
         * Parameters(data in request.body):
         * mSessionToken : an int for the session token
         * 
         * Parameters(data in request url):
         * messageID : an int for the message id
         * 
         * "http://localhost:4567/comments/:messageID"
         */
        Spark.get("/comments/:messageID", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int sessionToken = gson.fromJson(request.body(), SessionTokenDataRow.class).mSessionToken;
            boolean validSessionToken = checkSessionToken(db, sessionToken);
            if (validSessionToken) {
                int messageID = Integer.parseInt(request.params("messageID"));
                return gson.toJson(new StructuredResponse("ok", "", db.selectMultipleCommentsTblRow(messageID)));
            } else {
                response.redirect("/");
                return gson.toJson(new StructuredResponse("error", "Invalid Session", null));
            }
        });

        /**
         * Route to add a comment (POST)
         * 
         * Parameters(data in request.body):
         * mSessionToken : an int for the session token
         * mUserID : a String for the userID
         * mContent : a String for the content of the comment
         * 
         * Parameters(data in request url):
         * messageID : an int for the message id
         * 
         * "http://localhost:4567/comments/:messageID"
         */
        Spark.post("/comments/:messageID", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int sessionToken = gson.fromJson(request.body(), SessionTokenDataRow.class).mSessionToken;
            boolean validSessionToken = checkSessionToken(db, sessionToken);
            if (validSessionToken) {
                int messageID = Integer.parseInt(request.params("messageID"));
                CommentsDataRow commentData = gson.fromJson(request.body(), CommentsDataRow.class);
                int maxCommentID = db.selectMaxCommentID() + 1;
                db.insertCommentsTblRow(maxCommentID, messageID, commentData.mUserID, commentData.mContent);
                return gson.toJson(new StructuredResponse("ok", "", null));
            } else {
                response.redirect("/");
                return gson.toJson(new StructuredResponse("error", "Invalid Session", null));
            }
        });

        /**
         * Route to edit a comment (PUT)
         * 
         * Parameters(data in request.body):
         * mSessionToken : an int for the session token
         * mContent : a String for the new content of the comment
         * 
         * Parameters(data in request url):
         * commentID : an int for the commentID
         * 
         * "http://localhost:4567/comments/:messageID"
         */
        Spark.put("/comments/:commentID", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int sessionToken = gson.fromJson(request.body(), SessionTokenDataRow.class).mSessionToken;
            boolean validSessionToken = checkSessionToken(db, sessionToken);
            if (validSessionToken) {
                int commentID = Integer.parseInt(request.params("commentID"));
                CommentsDataRow commentData = gson.fromJson(request.body(), CommentsDataRow.class);
                db.updateOneCommentsTblRow(commentID, commentData.mContent);
                return gson.toJson(new StructuredResponse("ok", "", null));
            } else {
                response.redirect("/");
                return gson.toJson(new StructuredResponse("error", "Invalid Session", null));
            }
        });

        /**
         * Route to delete a comment (DELETE)
         * 
         * Parameters(data in request.body):
         * mSessionToken : an int for the session token
         * 
         * Parameters(data in request url):
         * commentID : an int for the commentID
         * 
         * "http://localhost:4567/comments/:commentID"
         */
        Spark.delete("/comments/:commentID", (request, response) -> {
            // Ensure status of 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int sessionToken = gson.fromJson(request.body(), SessionTokenDataRow.class).mSessionToken;
            boolean validSessionToken = checkSessionToken(db, sessionToken);
            if (validSessionToken) {
                int commentID = Integer.parseInt(request.params("commentID"));
                db.deleteOneCommentsTblRow(commentID);
                return gson.toJson(new StructuredResponse("ok", "", null));
            } else {
                response.redirect("/");
                return gson.toJson(new StructuredResponse("error", "Invalid Session", null));
            }
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