// package edu.lehigh.cse216.exh226.backend.fields;

// import java.util.Date;

// /**
// * DataRow holds a row of information. A row consists of:
// * an identifier, strings for "title" & "content", and a creation date
// *
// * Because we will convert instances of the object to JSON, fields must be
// * public --> so no getters and setters are required
// */

// public class User {
// /**
// * This Will be the post id of the post being interacted with
// */
// public String UID;

// /**
// * this is the bio of the user
// */
// public String Bio;

// /**
// * The creation date for this row of data. Once set it cannot be changed
// */
// public final Date mCreated;

// /**
// * Create a row of interactions
// *
// * @param UID This is the unqiue user Id
// * @param Bio this is the users Bio
// */
// public User(String UID, String Bio) {
// this.UID = UID;
// this.Bio = Bio;
// mCreated = new Date();
// }

// /**
// * Copy constructor to create one datarow from another
// */
// User(User data) {
// UID = data.UID;
// Bio = data.Bio;
// mCreated = data.mCreated;
// }
// }
