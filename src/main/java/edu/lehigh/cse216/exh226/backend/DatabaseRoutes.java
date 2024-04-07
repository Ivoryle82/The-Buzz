// package edu.lehigh.cse216.exh226.backend;

// import com.google.gson.Gson;

// import edu.lehigh.cse216.exh226.backend.controllers.InteractionController;
// import edu.lehigh.cse216.exh226.backend.controllers.MessageController;
// import edu.lehigh.cse216.exh226.backend.controllers.UserController;
// import edu.lehigh.cse216.exh226.backend.fields.User;
// import edu.lehigh.cse216.exh226.backend.requests.*;
// import edu.lehigh.cse216.exh226.backend.fields.Interaction;
// import edu.lehigh.cse216.exh226.backend.fields.Message;
// import spark.Spark;

// /*Updated By Sam Maloof
// * This file holds all the route commands for users, messages, and
// interactions
// * @Param is the correct controller for user, messages and interactions
// */
// public class DatabaseRoutes {

// private DatabaseRoutes() {
// }

// public void userRoutes(UserController udb) {
// final Gson gson = new Gson();

// // GET route that returns all message titles and Ids. All we do is get the
// data,
// // embed it in a StructuredResponse, then turn it into JSON and return it.
// // If there is no data, return "[]", so there's no need for error handling
// Spark.get("/user", (request, response) -> {
// // ensure status is 200 OK, with a MIME type of JSON
// response.status(200);
// response.type("application/json");
// return gson.toJson(new StructuredResponse("ok", null, udb.selectAll()));
// });

// // GET route that returns everything for a single row in the DataStore.
// // The ":id" suffix in the first parameter to get() becomes
// // request.params("id"),
// // so that we can get the requested row ID. If ":id" isn't a number, Spark
// will
// // reply with status 500 Internal Server Error. Otherwise, we have an
// integer,
// // and the only possible error is that it doesn't corresponding to a row with
// // data
// Spark.get("/users/:UID", (request, response) -> {
// String idx = (request.params("UID"));
// // Ensure status of 200 OK, with a MIME type of JSON
// response.status(200);
// response.type("application/json");
// User data = udb.selectOne(idx);
// if (data == null) {
// return gson.toJson(new StructuredResponse("error", idx + " not found",
// null));
// } else {
// return gson.toJson(new StructuredResponse("ok", null, data));
// }
// });

// Spark.post("/users", (request, response) -> {
// // NB: if gson.Json fails, Spark will reply with status 500 Internal Server
// // Error
// UserRequest req = gson.fromJson(request.body(), UserRequest.class);
// // Ensure status 200 OK, with a MIME type of JSON
// // NB: even on error, we return 200, but with a JSON object describing the
// error
// response.status(200);
// response.type("application/json");

// // NB: createEntry already checks for null title and message
// int newId = udb.insertRow(req.mUID, req.mBio);
// if (newId == -1) {
// return gson.toJson(new StructuredResponse("error", "error performing
// insertion", null));
// } else {
// return gson.toJson(new StructuredResponse("ok", "rows added: " + newId,
// null));
// }
// });

// // DELETE route for removing a row from the DataStore
// Spark.delete("/users/:UID", (request, response) -> {
// // If we can't get an ID, Spark will send a status 500
// String idx = (request.params("UID"));

// // ensure status 200 OK, and MIME type of JSON
// response.status(200);
// response.type("application/json");

// // NB: We aren't concerned with the message quality of a successful delete
// // boolean result = dataStore.deleteOne(idx);
// int result = udb.deleteMessage(idx);
// if (result == -1) {
// return gson.toJson(new StructuredResponse("error", "unable to delete row " +
// idx, null));
// } else {
// return gson.toJson(new StructuredResponse("ok", null, null));
// }
// });

// }

// public static void messageRoutes(MessageController mdb) {
// final Gson gson = new Gson();

// // GET route that returns all message titles and Ids. All we do is get the
// data,
// // embed it in a StructuredResponse, then turn it into JSON and return it.
// // If there is no data, return "[]", so there's no need for error handling
// Spark.get("/messages", (request, response) -> {
// // ensure status is 200 OK, with a MIME type of JSON
// response.status(200);
// response.type("application/json");
// return gson.toJson(new StructuredResponse("ok", null, mdb.selectAll()));
// });

// // GET route that returns everything for a single row in the DataStore.
// // The ":id" suffix in the first parameter to get() becomes
// // request.params("id"),
// // so that we can get the requested row ID. If ":id" isn't a number, Spark
// will
// // reply with status 500 Internal Server Error. Otherwise, we have an
// integer,
// // and the only possible error is that it doesn't corresponding to a row with
// // data
// Spark.get("/messages/:PID", (request, response) -> {
// int idx = Integer.parseInt(request.params("PID"));
// // Ensure status of 200 OK, with a MIME type of JSON
// response.status(200);
// response.type("application/json");
// Message data = mdb.selectOne(idx);
// if (data == null) {
// return gson.toJson(new StructuredResponse("error", idx + " not found",
// null));
// } else {
// return gson.toJson(new StructuredResponse("ok", null, data));
// }
// });

// Spark.post("/messages", (request, response) -> {
// // NB: if gson.Json fails, Spark will reply with status 500 Internal Server
// // Error
// MessageRequest req = gson.fromJson(request.body(), MessageRequest.class);
// // Ensure status 200 OK, with a MIME type of JSON
// // NB: even on error, we return 200, but with a JSON object describing the
// error
// response.status(200);
// response.type("application/json");

// // NB: createEntry already checks for null title and message
// int newId = mdb.insertRow(req.mContent);
// if (newId == -1) {
// return gson.toJson(new StructuredResponse("error", "error performing
// insertion", null));
// } else {
// return gson.toJson(new StructuredResponse("ok", "rows added: " + newId,
// null));
// }
// });

// // DELETE route for removing a row from the DataStore
// Spark.delete("/messages/:PID", (request, response) -> {
// // If we can't get an ID, Spark will send a status 500
// int idx = Integer.parseInt(request.params("PID"));

// // ensure status 200 OK, and MIME type of JSON
// response.status(200);
// response.type("application/json");

// // NB: We aren't concerned with the message quality of a successful delete
// // boolean result = dataStore.deleteOne(idx);
// int result = mdb.deleteMessage(idx);
// if (result == -1) {
// return gson.toJson(new StructuredResponse("error", "unable to delete row " +
// idx, null));
// } else {
// return gson.toJson(new StructuredResponse("ok", null, null));
// }
// });

// }

// public static void interactionRoutes(InteractionController idb) {
// final Gson gson = new Gson();

// // GET route that returns all message titles and Ids. All we do is get the
// data,
// // embed it in a StructuredResponse, then turn it into JSON and return it.
// // If there is no data, return "[]", so there's no need for error handling
// Spark.get("/interactions", (request, response) -> {
// // ensure status is 200 OK, with a MIME type of JSON
// response.status(200);
// response.type("application/json");
// return gson.toJson(new StructuredResponse("ok", null, idb.selectAll()));
// });

// // GET route that returns everything for a single row in the DataStore.
// // The ":id" suffix in the first parameter to get() becomes
// // request.params("id"),
// // so that we can get the requested row ID. If ":id" isn't a number, Spark
// will
// // reply with status 500 Internal Server Error. Otherwise, we have an
// integer,
// // and the only possible error is that it doesn't corresponding to a row with
// // data
// Spark.get("/interactions/:PID", (request, response) -> {
// int idx = Integer.parseInt(request.params("PID"));
// // Ensure status of 200 OK, with a MIME type of JSON
// response.status(200);
// response.type("application/json");
// Interaction data = idb.selectOne(idx);
// if (data == null) {
// return gson.toJson(new StructuredResponse("error", idx + " not found",
// null));
// } else {
// return gson.toJson(new StructuredResponse("ok", null, data));
// }
// });

// Spark.post("/interactions", (request, response) -> {
// // NB: if gson.Json fails, Spark will reply with status 500 Internal Server
// // Error
// InteractionRequest req = gson.fromJson(request.body(),
// InteractionRequest.class);
// // Ensure status 200 OK, with a MIME type of JSON
// // NB: even on error, we return 200, but with a JSON object describing the
// error
// response.status(200);
// response.type("application/json");

// // NB: createEntry already checks for null title and message
// int newId = idb.insertRow(req.mPID, req.mUIID, req.mLoD);
// if (newId == -1) {
// return gson.toJson(new StructuredResponse("error", "error performing
// insertion", null));
// } else {
// return gson.toJson(new StructuredResponse("ok", "rows added: " + newId,
// null));
// }
// });

// // DELETE route for removing a row from the DataStore
// Spark.delete("/interactions/:PID", (request, response) -> {
// // If we can't get an ID, Spark will send a status 500
// int idx = Integer.parseInt(request.params("PID"));

// // ensure status 200 OK, and MIME type of JSON
// response.status(200);
// response.type("application/json");

// // NB: We aren't concerned with the message quality of a successful delete
// // boolean result = dataStore.deleteOne(idx);
// int result = idb.deleteInteraction(idx);
// if (result == -1) {
// return gson.toJson(new StructuredResponse("error", "unable to delete row " +
// idx, null));
// } else {
// return gson.toJson(new StructuredResponse("ok", null, null));
// }
// });

// }

// }