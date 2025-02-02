package edu.lehigh.cse216.exh226.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should be
     * null. Otherwise, there is a valid open connection.
     */
    private Connection mConnection;

    // =========== Prepared Statements for working with userTbl =========
    /**
     * mInsertOneUserTbl: inserts an entry into the userTbl
     */
    private PreparedStatement mInsertOneUser;

    /**
     * mSelectOneUser: selects one entry from userTbl, necessary for
     * user to view their own profile
     */
    private PreparedStatement mSelectOneUser;

    /**
     * mUpdateOneUser: updates one entry from userTbl, necessary for
     * user to update their profile
     * Tech Debt: When updating a profile need to update all corresponding data
     */
    private PreparedStatement mUpdateOneUser;

    /**
     * mDeleteOneUser: deletes one entry from userTbl, necessary for
     * user to delete their profile.
     * Tech Debt: When deleting a profile need to delete all corresponding data
     */
    private PreparedStatement mDeleteOneUser; // TECH DEBT: IMPLEMENT THIS LATER
    // ========== End of userTbl Prepared Statements ==========

    // ========== Prepared Statements for messageTbl ==========
    /**
     * mInsertOneMessage: inserts an entry into the messageTbl
     */
    private PreparedStatement mInsertOneMessage;

    /**
     * mSelectMaxMessageID: returns the largest messageID from messageTbl, necessary
     * for the server to properly increment the messageID when a new message is
     * requested
     */
    private PreparedStatement mSelectMaxMessageID;

    /**
     * mSelectOneMessage: selects one entry from messageTbl, necessary for
     * user to view any message
     */
    private PreparedStatement mSelectOneMessage;

    /**
     * mSelectAllMessage: selects all entry from messageTbl, necessary for
     * user to view all messages from anyone.
     * Tech Debt: may make more sense to make them only able to view all of their
     * own messages
     */
    private PreparedStatement mSelectAllMessage;

    /**
     * mSelectAllMessage: selects all entry from messageTbl for a specific user
     * Tech Debt: may make more sense to make them only able to view all of their
     * own messages
     */
    private PreparedStatement mSelectAllMessageForUser;

    /**
     * mUpdateOneMessage: updates one entry from messageTbl, necessary for
     * user to update a message of theirs
     * Tech Debt: When updating a message user should only be able to update theirs
     */
    private PreparedStatement mUpdateOneMessage;

    /**
     * mUpdateMessageLikes: updates the likeCount field only for and element
     * in the messageTbl. Finds the message via messageID.
     */
    private PreparedStatement mUpdateOneMessageLikes;

    /**
     * mDeleteOneMessage: deletes one entry from messageTbl, necessary for
     * user to delete their profile.
     * Tech Debt: When deleting a message need to delete all corresponding data,
     * IE: the userLikes from the userLikesTbl
     */
    private PreparedStatement mDeleteOneMessage;

    // ========== End of messageTbl Prepared Statements =========

    // ========== Prepared Statements for userLikesTbl ==========
    /**
     * mInsertOneUserLike: inserts one entry into the userLikeTbl
     */
    private PreparedStatement mInsertOneUserLikes;

    /**
     * mSelectOneUserLike: selects one entry from the userLikeTbl
     * based on a passed username and messageID.
     * If this returns null it means the user hasn't liked the message yet
     */
    private PreparedStatement mSelectOneUserLikes;

    /**
     * mDeleteOneUserLike: deletes one entry into the userLikeTbl
     */
    private PreparedStatement mDeleteOneUserLikes;

    // ========== End of userLikesTbl Prepared Statements ========

    // KEEP THE PREPARED STATEMENTS BELOW UNTIL AFTER TESTING THE ONES ABOVE
    // /**
    // * A prepared statement for getting all data in the database
    // */
    // private PreparedStatement mSelectAll;

    // /**
    // * A prepared statement for getting on row form the database
    // */
    // private PreparedStatement mSelectOne;

    // /**
    // * A prepared statement for deleting a row from the database
    // */
    // private PreparedStatement mDeleteOne;

    // /**
    // * A prepared statement for inseting into the database
    // */
    // private PreparedStatement mInsertOne;

    // /**
    // * A prepared statment for updating a single row in the database
    // */
    // private PreparedStatement mUpdateOne;
    // /**
    // * A prepared statement for creating the table in our database
    // */
    // private PreparedStatement mCreateTable;

    // /**
    // * A prepared statment for dropping the table in our database
    // */
    // private PreparedStatement mDropTable;

    /**
     * createPreparedStatements: initialize all prepared statements
     * This is called in the getDatabase() method which returns a Database object
     * that gets a connection with our SQL database
     * 
     * Tech Debt: We should make the Strings for each prepared statement in
     * constant variables declared at the top of the program
     * Tech Debt: We need to get rid of the functionality for adding and dropping a
     * table, only admin should be allowed to do this
     * 
     * @return this (the current Database object), null if there was an error
     * 
     */
    private Database createPreparedStatements() {
        // NB: PreparedStatement(s) are prepared into the connection with
        // .prepareStatement("") The parameter is SQL language
        try {
            // Prepared Statements for userTbl
            mInsertOneUser = mConnection.prepareStatement("INSERT INTO userTbl VALUES (?, ?, ?, ?, ?)");
            mSelectOneUser = mConnection.prepareStatement("SELECT * FROM userTbl WHERE username=?");
            mUpdateOneUser = mConnection.prepareStatement(
                    "UPDATE userTbl SET userID=0,username=?,password=?,bio=?,email=? WHERE username=?");
            mDeleteOneUser = mConnection.prepareStatement("DELETE FROM userTbl WHERE username=?");

            // Prepared Statements for messageTbl
            mInsertOneMessage = mConnection.prepareStatement("INSERT INTO messageTbl VALUES (?,?,?,?,0)");
            mSelectMaxMessageID = mConnection.prepareStatement("SELECT max(messageID) FROM messageTbl");
            mSelectOneMessage = mConnection.prepareStatement("SELECT * FROM messageTbl WHERE messageID=?");
            mSelectAllMessage = mConnection.prepareStatement("SELECT * FROM messageTbl");
            mSelectAllMessageForUser = mConnection.prepareStatement("SELECT * FROM messageTbl WHERE username=?");
            mUpdateOneMessage = mConnection.prepareStatement(
                    "UPDATE messageTbl SET title=?,content=? WHERE messageID=?");
            mUpdateOneMessageLikes = mConnection.prepareStatement(
                    "UPDATE messageTbl SET likeCount=? WHERE messageID=?");
            mDeleteOneMessage = mConnection.prepareStatement("DELETE FROM messageTbl WHERE messageID=?");

            // Prepared Statements for userLikesTbl
            mInsertOneUserLikes = mConnection.prepareStatement("INSERT INTO userLikesTbl VALUES (?,?)");
            mSelectOneUserLikes = mConnection
                    .prepareStatement("SELECT * FROM userLikesTbl WHERE messageID=? AND username=?");
            mDeleteOneUserLikes = mConnection
                    .prepareStatement("DELETE FROM userLikesTbl WHERE username=? AND messageID=?");

            // BELOW STATEMENTS ARE UNECESSARY BUT KEEP THEM IN FOR NOW UNTIL AFTER TESTING
            // mCreateTable = mConnection.prepareStatement(
            // "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL,
            // message VARCHAR(500) NOT NULL)");
            // mDropTable = mConnection.prepareStatement("DROP TABLE tblData");
            // mDeleteOne = mConnection.prepareStatement("DELETE FROM tblDATA WHERE id =
            // ?");
            // mInsertOne = mConnection.prepareStatement("INSERT INTO tblData VALUES
            // (default, ?, ?)");
            // mSelectAll = mConnection.prepareStatement("SELECT id, subject FROM tblData");
            // mSelectOne = mConnection.prepareStatement("SELECT * from tblData WHERE
            // id=?");
            // mUpdateOne = mConnection.prepareStatement("UPDATE tblData SET message = ?
            // WHERE id = ?");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statements");
            e.printStackTrace();
            disconnect();
            return null;
        }
        return this;
    }

    /**
     * Deleted the RowData class since in our package the DataRow class
     * essentially functions the same (containing a int:id, String:title,
     * String:content)
     * it also included other functionality, a method to copy an existed DataRow
     * object
     * and also a Date property for storing the date it was created
     */

    /**
     * The Database constructor is private: we will only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database on specified port
     * 
     * @param host The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param path the path to use, can be null or empty
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String host, String port, String path, String user, String pass) {
        if (path == null || "".equals(path)) {
            path = "/";
        }

        Database db = new Database(); // Create an un-configured Database object

        // Give the Database object a connection, fail if we cannot get one
        try {
            String dbUrl = "jdbc:postgresql://" + host + ":" + port + path;
            // System.out.printf("TEST: %s\n", dbUrl);
            // System.out.printf("TEST: %s\n", user);
            // System.out.printf("TEST: %s\n", pass);
            Connection conn = DriverManager.getConnection(dbUrl, user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        db = db.createPreparedStatements();
        return db;
    }

    /**
     * getDatabase: second getDatabase method gets a fully-configured connection to
     * the database. Calls the above getDatabase() method after parsing db_url
     * 
     * @param db_url       The postgres database url
     * @param port_default port to use if absent in db_url this will be 5432 in the
     *                     program
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url, String port_default) {
        try {
            URI dbUri = new URI(db_url);
            // System.out.println("TESTING dbURI: " + dbUri.getUserInfo() + "\n");
            // System.out.println("TESTING dbURI: " + dbUri.getPort() + "\n");
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String host = dbUri.getHost();
            String path = dbUri.getPath();
            String port = dbUri.getPort() == -1 ? port_default : Integer.toString(dbUri.getPort());

            return getDatabase(host, port, path, username, password);
        } catch (URISyntaxException e) {
            System.err.println("Error: URI Syntax Error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * disconnect: Close the currect connection to the database, if one exists
     * 
     * NB: The connection will always be null after this call, even if an error
     * occurs during the closing operation
     * 
     * Tech Debt: must close result sets and prepared statements before closing the
     * connection. Use the .close() method for the prepared statements and result
     * sets
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null; // Setting the connection to null will make it so we cannot use the
                            // connection anymore, but some resources open in the database will not be
                            // closed
        return true; // .close() ran as intended and the reference to the connection is deleted
    }

    /********************************************************************************
     * The methods below all are used to work with the 3 tables within the database
     * via the prepared statements initialized above
     ********************************************************************************/

    /**
     * insertUserTblRow: Creates one user in the userTbl
     * 
     * @param username : a string for the username (primary key)
     * @param password : a string for the password
     * @param bio      : a string for the user's biography
     * @param email    : a string for the user's email
     * 
     * @return : returns the userID of the new row in userTbl
     */
    int insertUserTblRow(String username, String password, String bio, String email) {
        int count = 0;
        try {
            mInsertOneUser.setString(1, username);
            mInsertOneUser.setString(2, password);
            mInsertOneUser.setString(3, bio);
            mInsertOneUser.setString(4, email);
            count += mInsertOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * selectOneUserTblRow: selects one user from the userTbl
     * 
     * @param username : the username (unique primary key) for an element in userTbl
     * 
     * @return : returns a UserDataRow
     */
    UserDataRow selectOneUserTblRow(String username) {
        UserDataRow res = null;
        try {
            mSelectOneUser.setString(1, username);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) { // rs.next() verifies if there is an element in the ResultSet
                res = new UserDataRow(rs.getString("username"), rs.getString("password"),
                        rs.getString("bio"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * updateOneUserTblRow : updates one row in the userTbl
     * 
     * Tech Debt: need to make sure that a user can choose to
     * update any number of these fields, or have front end deal w/ this
     * just need to make sure we pass enough parameters (and correct ones) in here
     * 
     * @param username    : a String for username of the userTbl row to update
     * @param newUsername : a String for the new username
     * @param newPassword : a String for the new password
     * @param newBio      : a String for the new bio
     * @param newEmail    : a String for the new email
     * 
     * @return : returns an int for the number of rows that were updated, or -1 if
     *         there was an error
     */
    int updateOneUserTblRow(String username, String newUsername, String newPassword, String newBio, String newEmail) {
        int res = -1;
        try {
            mUpdateOneUser.setString(1, newUsername);
            mUpdateOneUser.setString(2, newPassword);
            mUpdateOneUser.setString(3, newBio);
            mUpdateOneUser.setString(4, newEmail);
            mUpdateOneUser.setString(5, username); // this is how we query the specific row in userTbl
            res = mUpdateOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * deleteOneUserTblRow: deletes one row in userTbl
     * 
     * @param username : a String for the username of the userTbl row to delete
     * 
     * @return : The number of rows that were deleted, return -1 if there was an
     *         error
     */
    int deleteOneUserTblRow(String username) {
        int res = -1;
        try {
            mDeleteOneUser.setString(1, username);
            res = mDeleteOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    } // End of userTbl methods

    /**
     * insertMessageTblRow: Creates one message in the messageTbl
     * 
     * Tech Debt: backend should generate a unique messageID for each message
     * 
     * @param username  : a string for the username (foreign key)
     * @param messageID : a string for the messageID (primary key)
     * @param title     : a String for the message title
     * @param content   : a String for the message content
     * @param likeCount : an int for the likeCount, will be 0
     * 
     * @return : returns the number of rows in messageTbl
     */
    int insertMessageTblRow(String username, int messageID, String title, String content) {
        int result = 0;
        try {
            mInsertOneMessage.setString(1, username);
            mInsertOneMessage.setInt(2, messageID);
            mInsertOneMessage.setString(3, title);
            mInsertOneMessage.setString(4, content);
            result = mInsertOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result; // returns the number of rows added either 1 or 0
    }

    /**
     * selectMaxMessageID: selects the maximum messageID from the messageTbl
     * 
     * @param NONE
     * 
     * @return : returns an int for the largest messageID
     */
    int selectMaxMessageID() {
        int result = -1;
        try {
            ResultSet rs = mSelectMaxMessageID.executeQuery();
            if (rs.next()) {
                result = rs.getInt("messageID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * selectOneMessageTblRow: selects one message from the messageTbl
     * 
     * @param messageID : a int for the messageID (unique primary key) for an
     *                  element in messageTbl
     * 
     * @return : returns a MessageDataRow
     */
    MessageDataRow selectOneMessageTblRow(int messageID) {
        MessageDataRow res = null;
        try {
            mSelectOneMessage.setInt(1, messageID);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) { // rs.next() verifies if there is an element in the ResultSet
                res = new MessageDataRow(rs.getString("username"), rs.getInt("messageID"),
                        rs.getString("title"), rs.getString("content"), rs.getInt("likeCount"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * selectAllMessageTblRowForUser: selects all messages for a specific user
     * 
     * @param username : a String for the username
     * 
     * @return : an ArrayList of MessageDataRow objects
     */
    ArrayList<MessageDataRow> selectAllMessageTblRowForUser(String username) {
        ArrayList<MessageDataRow> res = new ArrayList<MessageDataRow>();
        try {
            mSelectAllMessageForUser.setString(1, username);
            ResultSet rs = mSelectAllMessageForUser.executeQuery();
            while (rs.next()) {
                res.add(new MessageDataRow(rs.getString("username"), rs.getInt("messageID"), rs.getString("title"),
                        rs.getString("content"), rs.getInt("likeCount")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * selectAllMessageTblRow: selects all messages
     * 
     * @param username : a String for the username
     * 
     * @return : an ArrayList of MessageDataRow objects
     */
    ArrayList<MessageDataRow> selectAllMessageTblRow() {
        ArrayList<MessageDataRow> res = new ArrayList<MessageDataRow>();
        try {
            ResultSet rs = mSelectAllMessage.executeQuery();
            while (rs.next()) {
                res.add(new MessageDataRow(rs.getString("username"), rs.getInt("messageID"), rs.getString("title"),
                        rs.getString("content"), rs.getInt("likeCount")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * updateOneMessageTblRow : updates one row in the messageTbl
     * 
     * Tech Debt: need to make sure that a user can choose to
     * update any number of these fields, or have front end deal w/ this
     * just need to make sure we pass enough parameters (and correct ones) in here
     * 
     * @param messageID  : an int for the messageID
     * @param newTitle   : a String for the new password
     * @param newContent : a String for the new bio
     *
     * @return : returns an int for the number of rows that were updated, or -1 if
     *         there was an error
     */
    int updateOneMessageTblRow(int messageID, String newTitle, String newContent) {
        int res = -1;
        try {
            mUpdateOneMessage.setString(1, newTitle);
            mUpdateOneMessage.setString(2, newContent);
            mUpdateOneMessage.setInt(3, messageID); // this is how we query the specific row in messageTbl
            res = mUpdateOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * updateOneMessageTblRowLikes : updates one row in the messageTbl
     * 
     * Tech Debt: In the backend we can use the selectOneMessageTblRow to first get
     * the likes and have that increment or decrement via a route to that method.
     * Then use that resulting value to update the likeCount.
     * OR we could have 2 separate database methods, 1 for inc and 1 for dec
     * likeCount, BUT, doing that would require us to call the
     * selectOneMessageTblRow anyway before incrementing or decrementing the
     * likeCount
     * 
     * @param messageID    : an int for the messageID
     * @param newLikeCount : an int for the new likeCount
     *
     * @return : returns an int for the number of rows that were updated, or -1 if
     *         there was an error
     */
    int updateOneMessageTblRowLikes(int messageID, int newLikeCount) {
        int res = -1;
        try {
            mUpdateOneMessageLikes.setInt(1, newLikeCount);
            mUpdateOneMessageLikes.setInt(2, messageID); // this is how we query the specific row in messageTbl
            res = mUpdateOneMessageLikes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * deleteOneMessageTblRow: deletes one row in messageTbl
     * 
     * @param messageID : a int for the message of the messageTbl row to delete
     * 
     * @return : The number of rows that were deleted, return -1 if there was an
     *         error
     */
    int deleteOneMessageTblRow(int messageID) {
        int res = -1;
        try {
            mDeleteOneMessage.setInt(1, messageID);
            res = mDeleteOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    } // End of messageTbl methods

    /**
     * insertUserLikesTblRow: Creates one userLikes in the userLikesTbl
     * 
     * @param username  : a string for the username
     * @param messageID : a int for the messageID
     * 
     * @return : returns the number of rows in userLikesTbl
     */
    int insertUserLikesTblRow(String username, int messageID) {
        int count = 0;
        try {
            mInsertOneUserLikes.setString(1, username);
            mInsertOneUserLikes.setInt(2, messageID);
            count += mInsertOneUserLikes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * selectOneUserLikesTblRow: selects one userLikes from the userLikesTbl
     * 
     * @param username  : a String for the username of an element in userLikesTbl
     * @param messageID : a int for the messageID of an element in userLikesTbl
     * 
     * @return : returns a UserLikesDataRow
     */
    UserLikesDataRow selectOneUserLikesTblRow(String username, int messageID) {
        UserLikesDataRow res = null;
        try {
            mSelectOneUserLikes.setString(1, username);
            mSelectOneUserLikes.setInt(2, messageID);
            ResultSet rs = mSelectOneUserLikes.executeQuery();
            if (rs.next()) { // rs.next() verifies if there is an element in the ResultSet
                res = new UserLikesDataRow(rs.getString("username"), rs.getInt("messageID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * deleteOneUserLikesTblRow: deletes one row in userLikesTbl
     * 
     * @param username  : a String for the username of the userLikesTbl row to
     *                  delete
     * @param messageID : a String for the messageID of the userLikesTbl row to
     *                  delete
     * 
     * @return : The number of rows that were deleted, return -1 if there was an
     *         error (this included if a row was not deleted)
     */
    int deleteOneUserLikesTblRow(String username, int messageID) {
        int res = -1;
        try {
            mDeleteOneUserLikes.setString(1, username);
            mDeleteOneUserLikes.setInt(2, messageID);
            res = mDeleteOneUserLikes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    } // End of userLikesTbl methods

    // BELOW METHODS ARE UNECESSARY AND WILL BE DELETED

    // /**
    // * insertRow: Insert a row into the database
    // *
    // * @param subject : The subject for this new row
    // * @param message : The message body for this new row
    // *
    // * @return : The number of rows that were inserted
    // */
    // int insertRow(String subject, String message) {
    // int count = 0;
    // try {
    // mInsertOne.setString(1, subject);
    // mInsertOne.setString(2, message);
    // count += mInsertOne.executeUpdate();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return count;
    // }

    // /**
    // * Query the database for a list of all subjects and their IDs
    // * Equivalent to DataStore.readAll
    // *
    // * @return All rows, as an ArrayList of RowData (rows) objects
    // */
    // ArrayList<DataRow> selectAll() {
    // ArrayList<DataRow> res = new ArrayList<DataRow>();
    // try {
    // ResultSet rs = mSelectAll.executeQuery(); // executeQuery i assumed executes
    // // SQL. So here it is selecting
    // // all via the PreparedStatement to select all
    // while (rs.next()) {
    // res.add(new DataRow(rs.getInt("id"), rs.getString("subject"), null));
    // }
    // rs.close();
    // return res;
    // } catch (SQLException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    // /**
    // * Get all data for a specific row, by ID
    // *
    // * @param id : The id of the row beign requested
    // *
    // * @return :The data for the requested row (RowData object), or null if
    // * invalid
    // * ID
    // */
    // DataRow selectOne(int id) {
    // DataRow res = null;
    // try {
    // mSelectOne.setInt(1, id);
    // ResultSet rs = mSelectOne.executeQuery();
    // if (rs.next()) { // Why do we do rs.next()?
    // res = new DataRow(rs.getInt("id"), rs.getString("subject"),
    // rs.getString("message"));
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return res;
    // }

    // /**
    // * Delete a row by ID
    // *
    // * @param id : The id of the row to delete
    // *
    // * @return : The number of rows that were deleted. -1 indicates an error.
    // */
    // int deleteRow(int id) {
    // int res = -1;
    // try {
    // mDeleteOne.setInt(1, id);
    // res = mDeleteOne.executeUpdate(); // Execute update differs from execute
    // query
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return res;
    // }

    // /**
    // * Update the message for a row in the database
    // *
    // * @param id : The id of the row to update
    // * @param message : the new message contents
    // *
    // * @return : The number of rows that were updated. -1 indicates an error
    // */
    // int updateOne(int id, String message) {
    // int res = -1;
    // try {
    // mUpdateOne.setString(1, message);
    // mUpdateOne.setInt(2, id);
    // res = mUpdateOne.executeUpdate();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return res;
    // }

    /**
     * Create tblData. If it already exists, print an error
     * we know it creates tblData bc that is the name from the PreparedStatement
     */
    // void createTable() {
    // try {
    // mCreateTable.execute(); // another new execute command, prob differs based on
    // SQL content
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }

    // /**
    // * Remove tblData from the database. If it does not exist, print an error
    // */
    // void dropTable() {
    // try {
    // mDropTable.execute();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }
}
