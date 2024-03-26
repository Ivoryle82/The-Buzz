// package edu.lehigh.cse216.exh226.backend.fields;

// import java.util.Date;

// /**
// * DataRow holds a row of information. A row consists of:
// * an identifier, strings for "title" & "content", and a creation date
// *
// * Because we will convert instances of the object to JSON, fields must be
// * public --> so no getters and setters are required
// */

// public class Message {
// /**
// * This Will be the post id of the post being interacted with
// */
// public int PID;

// /**
// * This is what is inside the message
// */
// public String content;

// /**
// * The creation date for this row of data. Once set it cannot be changed
// */
// public final Date mCreated;

// /**
// * Create a row of interactions
// *
// * @param PID This is the unqiue postId
// * @param content : this is the message content
// */
// public Message(int PID, String content) {
// this.PID = PID;

// this.content = content;
// mCreated = new Date();
// }

// /**
// * Copy constructor to create one datarow from another
// */
// Message(Message data) {
// PID = data.PID;
// // UID = data.UID;
// content = data.content;
// mCreated = data.mCreated;
// }
// }
