package edu.lehigh.cse216.team09.exh226.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateUserTable;
    private PreparedStatement mCreateMessageTable;
    private PreparedStatement mCreateUserLikesTable;
    private PreparedStatement mCreateUserDislikesTable;
    private PreparedStatement mCreateCommentsTable;
    
    /**
     * Prepared statements for user table CRUD operations
     */
    private PreparedStatement mInsertUser;
    private PreparedStatement mSelectAllUser;
    private PreparedStatement mSelectOneUser;
    private PreparedStatement mUpdateUser;
    private PreparedStatement mDeleteUser;
    private PreparedStatement mInvalidateUser;
    private PreparedStatement mDropUserTable;

    /**
     * Prepared statements for message table CRUD operations
     */
    private PreparedStatement mInsertMessage;
    private PreparedStatement mSelectAllMessage;
    private PreparedStatement mSelectOneMessage;
    private PreparedStatement mUpdateMessage;
    private PreparedStatement mDeleteMessage;
    private PreparedStatement mInvalidateIdea;
    private PreparedStatement mDropMessageTable;



    /**
     * Prepared statements for userLikes table CRUD operations
     */
    private PreparedStatement mDropUserLikesTable;
    private PreparedStatement mInsertUserLike;
    private PreparedStatement mDeleteUserLike;
    private PreparedStatement mUpdateUserLike;
    private PreparedStatement mSelectAllUserLikes;
    private PreparedStatement mSelectOneUserLike;

    /**
     * Prepared statements for userLikes table CRUD operations
     */
    private PreparedStatement mDropUserDislikesTable;
    private PreparedStatement mInsertUserDislike;
    private PreparedStatement mDeleteUserDislike;
    private PreparedStatement mUpdateUserDislike;
    private PreparedStatement mSelectAllUserDislikes;
    private PreparedStatement mSelectOneUserDislike;

    /**
     * Prepared statements for comments table CRUD operations
     */
    private PreparedStatement mDropCommentsTable;
    private PreparedStatement mInsertComment;
    private PreparedStatement mSelectAllComments;
    private PreparedStatement mSelectOneComment;
    private PreparedStatement mUpdateComment;
    private PreparedStatement mDeleteComment;
    private PreparedStatement mInvalidateComment;


    public static class RowData {
        String mUserID;
        int mMessageID;
        int mCommentID;

        String mTitle;
        String mContent;

        int mLikeCount;
        int mDislikeCount;

        String mFileID;
        String mUsername;
        String mEmail;
        String mBio;

        String mDateCreated;


        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(String userID, int messageID, String title, String content, int likeCount, int dislikeCount, String fileID) {
            mUserID = userID;
            mMessageID = messageID;
            mTitle = title;
            mContent = content;
            mLikeCount = likeCount;
            mDislikeCount = dislikeCount;
            mFileID = fileID;
        }

        /**
         * Construct a RowData object for the user table
         */
        public RowData(String userID, String username, String email, String bio) {
            mUserID = userID;
            mUsername = username;
            mEmail = email;
            mBio = bio;
        }

        /**
         * Construct a RowData object for the userLikes table
         */
        public RowData(String userID, int messageId) {
            mUserID = userID;
            mMessageID = messageId;
        }

        public RowData(int commentID, int messageID, String userID,  String content) {
            mCommentID = commentID;
            mMessageID = messageID;
            mUserID = userID;
            mContent = content;
           // mFileID = fileID;
        }

    }

    private Database() {}

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String ip, String port, String user, String pass) {
        // Create an un-configured Database object
        Database db = new Database();
        
        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
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

        try {
            
            //USER TABLE
            // Create user table
            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE userTbl (userID VARCHAR(225), username VARCHAR(225), email VARCHAR(225), bio VARCHAR(225))");
            // Prepare statements for user table CRUD operations
            db.mInsertUser = db.mConnection.prepareStatement(
                "INSERT INTO userTbl (userID, username, email, bio) VALUES (?, ?, ?, ?)");
            db.mSelectAllUser = db.mConnection.prepareStatement(
                    "SELECT * FROM userTbl");
            db.mSelectOneUser = db.mConnection.prepareStatement(
                    "SELECT * FROM userTbl WHERE userID = ?");
            db.mUpdateUser = db.mConnection.prepareStatement(
                    "UPDATE userTbl SET username = ?, email = ?, bio = ? WHERE userID = ?");
            db.mDeleteUser = db.mConnection.prepareStatement(
                "DELETE FROM userTbl WHERE userID = ?");
            db.mInvalidateUser = db.mConnection.prepareStatement(
                    "UPDATE userTbl SET valid = false WHERE userID = ?");
            db.mDropUserTable = db.mConnection.prepareStatement("DROP TABLE IF EXISTS userTbl");
            // Create other tables as needed...
           
           
            // Create message table
            db.mCreateMessageTable = db.mConnection.prepareStatement(
                    "CREATE TABLE messageTbl (userID VARCHAR(225), messageID int, title VARCHAR(225), content VARCHAR(225), LikeCount int, DislikeCount int, fileID VARCHAR(225))");
                    // Prepare statements for message table CRUD operations
            db.mInsertMessage = db.mConnection.prepareStatement(
                "INSERT INTO messageTbl (userID, messageID, title, content, LikeCount, DislikeCount, fileID) VALUES (?, ?, ?, ?, ?, ?, ?)");
            db.mSelectAllMessage = db.mConnection.prepareStatement(
                    "SELECT * FROM messageTbl");
            db.mSelectOneMessage = db.mConnection.prepareStatement(
                    "SELECT * FROM messageTbl WHERE messageID = ?");
            db.mUpdateMessage = db.mConnection.prepareStatement(
                    "UPDATE messageTbl SET title = ?, content = ?, LikeCount = ?, DislikeCount = ?, fileID = ? WHERE messageID = ?");
            db.mDeleteMessage = db.mConnection.prepareStatement(
                    "DELETE FROM messageTbl WHERE messageID = ?");
            db.mInvalidateIdea = db.mConnection.prepareStatement(
                "UPDATE messageTbl SET valid = false WHERE messageID = ?");
            db.mDropMessageTable = db.mConnection.prepareStatement("DROP TABLE IF EXISTS messageTbl");


            //Create User Like Table
            db.mCreateUserLikesTable = db.mConnection.prepareStatement(
                 "CREATE TABLE userLikesTbl (username VARCHAR(225), messageID int)");
            db.mDropUserLikesTable = db.mConnection.prepareStatement("DROP TABLE IF EXISTS userLikesTbl");
            // Prepare statements for userLikesTable CRUD operations
            db.mInsertUserLike = db.mConnection.prepareStatement(
                    "INSERT INTO userLikesTbl (username, messageID) VALUES (?, ?)");
            db.mDeleteUserLike = db.mConnection.prepareStatement(
                    "DELETE FROM userLikesTbl WHERE id = ?");
            db.mUpdateUserLike = db.mConnection.prepareStatement("UPDATE userLikesTbl SET userID = ?, messageID = ?");
            db.mSelectAllUserLikes = db.mConnection.prepareStatement(
                    "SELECT * FROM userLikesTbl");
            db.mSelectOneUserLike = db.mConnection.prepareStatement(
                    "SELECT * FROM userLikesTbl WHERE id = ?");
            
            //Create User Dislike Table
            db.mCreateUserDislikesTable = db.mConnection.prepareStatement(
                 "CREATE TABLE userDislikesTbl (username VARCHAR(225), messageID int)");
            db.mDropUserDislikesTable = db.mConnection.prepareStatement("DROP TABLE IF EXISTS userDislikesTbl");
            // Prepare statements for userLikesTable CRUD operations
            db.mInsertUserLike = db.mConnection.prepareStatement(
                    "INSERT INTO userDislikesTbl (username, messageID) VALUES (?, ?)");
            db.mDeleteUserLike = db.mConnection.prepareStatement(
                    "DELETE FROM userDislikesTbl WHERE messageID = ?");
            db.mUpdateUserDislike = db.mConnection.prepareStatement("UPDATE userDislikesTbl SET userID = ?, messageID = ?");
            db.mSelectAllUserDislikes = db.mConnection.prepareStatement(
                    "SELECT * FROM userDislikesTbl");
            db.mSelectOneUserDislike = db.mConnection.prepareStatement(
                    "SELECT * FROM userDislikesTbl WHERE messageID = ?");
            
            //Create Comments table
            db.mCreateCommentsTable = db.mConnection.prepareStatement(
                          "CREATE TABLE commentsTbl (commentID int, messageID int, userID VARCHAR(225), content VARCHAR(225))");
            db.mDropCommentsTable = db.mConnection.prepareStatement("DROP TABLE IF EXISTS commentsTbl");
            db.mDeleteComment = db.mConnection.prepareStatement("DELETE FROM commentsTbl WHERE commentID = ?");
            db.mInsertComment = db.mConnection.prepareStatement("INSERT INTO commentsTbl (commentID, messageID, userID, content) VALUES (?, ?, ?, ?)");
            db.mSelectAllComments = db.mConnection.prepareStatement("SELECT * FROM commentsTbl");
            db.mSelectOneComment = db.mConnection.prepareStatement("SELECT * from commentsTbl WHERE commentID=?");
            db.mUpdateComment = db.mConnection.prepareStatement("UPDATE commentsTbl SET content = ? WHERE commentID = ?");
            db.mInvalidateComment = db.mConnection.prepareStatement(
                "UPDATE commentsTbl SET valid = false WHERE commentID = ?");
            // Create other tables as needed...

    
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }

        return db;
    } 


    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
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
        mConnection = null;
        return true;
    }

    /**
     * Create the message table in the database
     */
    void createMessageTable() {
        try {
            mCreateMessageTable.execute();
            System.out.println("Message Table Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for message table
    int insertMessage(String userID, int messageID, String title, String content, int likeCount, int dislikeCount, String fileID) {
        int count = 0;
        try {
            mInsertMessage.setString(1, userID);
            mInsertMessage.setInt(2, messageID);
            mInsertMessage.setString(3, title);
            mInsertMessage.setString(4, content);
            mInsertMessage.setInt(5, likeCount);
            mInsertMessage.setInt(6, dislikeCount);
            mInsertMessage.setString(7, fileID);

            //mInsertMessage.setString(7, file);
            count += mInsertMessage.executeUpdate();
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return count;
    }

    ArrayList<RowData> selectAllMessages() {
        ArrayList<RowData> messages = new ArrayList<>();
        try {
            ResultSet rs = mSelectAllMessage.executeQuery();
            while (rs.next()) {
                RowData message = new RowData(
                        rs.getString("userID"),
                        rs.getInt("messageID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("LikeCount"),
                        rs.getInt("DislikeCount"),
                        rs.getString("fileID")
                );
                messages.add(message);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    RowData selectOneMessage(int messageID) {
        RowData message = null;
        try {
            mSelectOneMessage.setInt(1, messageID);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                message = new RowData(
                        rs.getString("userID"),
                        rs.getInt("messageID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("LikeCount"),
                        rs.getInt("DislikeCount"),
                        rs.getString("fileID")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    int updateMessage(int messageID, String title, String content, int likeCount, int dislikeCount, String fileID) {
        int count = 0;
        try {
            mUpdateMessage.setString(1, title);        // title
            mUpdateMessage.setString(2, content);      // content
            mUpdateMessage.setInt(3, likeCount);      // LikeCount
            mUpdateMessage.setInt(4, dislikeCount);   // DislikeCount
            mUpdateMessage.setString(5, fileID);      // fileID
            mUpdateMessage.setInt(6, messageID);      // messageID
            count += mUpdateMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int deleteMessage(int messageID) {
        int count = 0;
        try {
            mDeleteMessage.setInt(1, messageID);
            count += mDeleteMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

     /**
      * Remove tblData from the database.  If it does not exist, this will print
      * an error.
    */
        void dropMessageTable() {
            try {
                mDropMessageTable.execute();
                System.out.println("Message Table Dropped");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    /**
     * Invalidate an idea by setting its validity flag to false
     * 
     * @param ideaID The ID of the idea to invalidate
     * @return True if the idea was invalidated successfully, false otherwise
     */

    boolean invalidateIdea(int messageID) {
        try {
            mInvalidateIdea.setInt(1, messageID);
            int rowsUpdated = mInvalidateIdea.executeUpdate();
            if (rowsUpdated > 0) {
                 invalidateCommentsForMessage(messageID);
            }
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    void invalidateCommentsForMessage(int messageID) {
        // Retrieve comment IDs associated with the message ID
        ArrayList<Integer> commentIDs = findCommentIDsForMessage(messageID);
        
        // Iterate through the comment IDs and invalidate each comment
        for (int commentID : commentIDs) {
            invalidateComment(commentID);
        }
    }

    ArrayList<Integer> findCommentIDsForMessage(int messageID) {
        ArrayList<Integer> commentIDs = new ArrayList<>();
        try {
            // Prepare a statement to select comment IDs associated with the specified message ID
            PreparedStatement selectCommentIDsStmt = mConnection.prepareStatement(
                    "SELECT commentID FROM commentsTbl WHERE messageID = ?");
            selectCommentIDsStmt.setInt(1, messageID);
            
            // Execute the query to retrieve comment IDs
            ResultSet rs = selectCommentIDsStmt.executeQuery();
            
            // Iterate through the result set and add comment IDs to the list
            while (rs.next()) {
                int commentID = rs.getInt("commentID");
                commentIDs.add(commentID);
            }
            
            // Close the result set
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentIDs;
    }

    public void removeLeastRecentlyAccessedFile() {
        try {
            String query = "SELECT fileID FROM commentsTbl ORDER BY dateCreated ASC LIMIT 1";
            PreparedStatement statement = mConnection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                String leastRecentlyAccessedFile = resultSet.getString("fileID");
    
                // Delete the least recently accessed file from the database 
                PreparedStatement deleteStatement = mConnection.prepareStatement("DELETE FROM commentsTbl WHERE fileID = ?");
                deleteStatement.setString(1, leastRecentlyAccessedFile);
                deleteStatement.executeUpdate();
    
                System.out.println("Least recently accessed file '" + leastRecentlyAccessedFile + "' removed successfully.");
            } else {
                System.out.println("No files found to remove.");
            }
        } catch (SQLException e) {
            System.err.println("Error removing least recently accessed file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //User Table 
    
    /**
     * Create the user table in the database
     */
    void createUserTable() {
        try {
            mCreateUserTable.execute();
            System.out.println("User Table Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a new user into the user table
     * 
     * @param userID   The ID of the user
     * @param username The username of the user
     * @param email    The email of the user
     * @param bio      The bio of the user
     * @return The number of rows that were inserted
     */
    int insertUser(String userID, String username, String email, String bio) {
        int count = 0;
        try {
            mInsertUser.setString(1, userID);
            mInsertUser.setString(2, username);
            mInsertUser.setString(3, email);
            mInsertUser.setString(4, bio);
            count += mInsertUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    RowData selectOneUser(String userID) {
        RowData user = null;
        try {
            mSelectOneUser.setString(1, userID);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) {
                user = new RowData(
                        rs.getString("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("bio")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieve all users from the user table
     * 
     * @return A list of users
     */
    ArrayList<RowData> selectAllUsers() {
        ArrayList<RowData> users = new ArrayList<>();
        try {
            ResultSet rs = mSelectAllUser.executeQuery();
            while (rs.next()) {
                String userID = rs.getString("userID");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String bio = rs.getString("bio");
                users.add(new RowData(userID, username, email, bio));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Update a user's information in the user table
     * 
     * @param userID   The ID of the user to update
     * @param username The new username
     * @param email    The new email
     * @param bio      The new bio
     * @return The number of rows that were updated
     */
    int updateUser(String userID, String username, String email, String bio) {
        int count = 0;
        try {
            mUpdateUser.setString(1, username);
            mUpdateUser.setString(2, email);
            mUpdateUser.setString(3, bio);
            mUpdateUser.setString(4, userID);
            count += mUpdateUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Delete a user from the user table
     * 
     * @param userID The ID of the user to delete
     * @return The number of rows that were deleted
     */
    int deleteUser(String userID) {
        int count = 0;
        try {
            mDeleteUser.setString(1, userID);
            count += mDeleteUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    boolean invalidateUser(String userId) {
        try {
            mInvalidateUser.setString(1, userId);
            int rowsUpdated = mInvalidateUser.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
      * Remove tblData from the database. If it does not exist, this will print
      * an error.
    */
    void dropUserTable() {
        try {
            mDropUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //UserlikesTable
  
    /**
     * Create the message table in the database
     */
    void createUserLikesTable() {
        try {
            mCreateUserLikesTable.execute();
            System.out.println("Likes Table Created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
      * Remove tblData from the database. If it does not exist, this will print
      * an error.
    */
    void dropUserLikesTable() {
        try {
            mDropUserLikesTable.execute();
            System.out.println("Like Table Dropped");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a new user like into the userLikes table
     * 
     * @param username  The username of the user who liked the message
     * @param messageID The ID of the message that was liked
     * @return The number of rows that were inserted
     */
    int insertUserLike(String username, int messageID) {
        int count = 0;
        try {
            mInsertUserLike.setString(1, username);
            mInsertUserLike.setInt(2, messageID);
            count += mInsertUserLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Delete a user like from the userLikes table
     * 
     * @param likeID The ID of the like to delete
     * @return The number of rows that were deleted
     */
    int deleteUserLike(int likeID) {
        int count = 0;
        try {
            mDeleteUserLike.setInt(1, likeID);
            count += mDeleteUserLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Retrieve all user likes from the userLikes table
     * 
     * @return A list of user likes
     */
    ArrayList<RowData> selectAllUserLikes() {
        ArrayList<RowData> userLikes = new ArrayList<>();
        try {
            ResultSet rs = mSelectAllUserLikes.executeQuery();
            while (rs.next()) {
                RowData userLike = new RowData(
                        rs.getString("userID"),
                        rs.getInt("messageID")
                );
                userLikes.add(userLike);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userLikes;
    }

    /**
     * Retrieve a specific user like from the userLikes table by its ID
     * 
     * @param likeID The ID of the like to retrieve
     * @return The user like, or null if not found
     */
    RowData selectOneUserLike(int likeID) {
        RowData userLike = null;
        try {
            mSelectOneUserLike.setInt(1, likeID);
            ResultSet rs = mSelectOneUserLike.executeQuery();
            if (rs.next()) {
                userLike = new RowData(
                        rs.getString("username"),
                        rs.getInt("messageID")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userLike;
    }

    //Comments Table Functions

    /**
     * Create the user table in the database
     */
    void createCommentsTable() {
        try {
            mCreateCommentsTable.execute();
            System.out.println("Comments Table Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the comments table from the database. If it does not exist, this will print an error.
     */
    void dropCommentsTable() {
        try {
            mDropCommentsTable.execute();
            System.out.println("Comments Table Dropped");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a comment in the comments table
    int insertComment(int commentID,  int messageID, String userID, String content) {
        int count = 0;
        try {
            mInsertComment.setInt(1, commentID);
            mInsertComment.setInt(2, messageID);
            mInsertComment.setString(3, userID);
            mInsertComment.setString(4, content);
            count += mInsertComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Retrieve all comments from the comments table
    ArrayList<RowData> selectAllComments() {
        ArrayList<RowData> comments = new ArrayList<>();
        try {
            ResultSet rs = mSelectAllComments.executeQuery();
            while (rs.next()) {
                RowData comment = new RowData(
                    rs.getInt("commentID"),
                    rs.getInt("messageID"),
                    rs.getString("userID"),
                    rs.getString("content")
                );
                comments.add(comment);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    // Retrieve a specific comment from the comments table by commentID
    RowData selectOneComment(String commentID) {
        RowData comment = null;
        try {
            mSelectOneComment.setString(1, commentID);
            ResultSet rs = mSelectOneComment.executeQuery();
            if (rs.next()) {
                comment = new RowData(
                    rs.getInt("commentID"),
                    rs.getInt("messageID"),
                    rs.getString("userID"),
                    rs.getString("content")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }

    // Update a comment in the comments table
    int updateComment(String commentID, String content, String fileID) {
        int count = 0;
        try {
            mUpdateComment.setString(1, content);
            mUpdateComment.setString(2, fileID);
            mUpdateComment.setString(3, commentID);
            count += mUpdateComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Delete a comment from the comments table by commentID
    int deleteComment(String commentID) {
        int count = 0;
        try {
            mDeleteComment.setString(1, commentID);
            count += mDeleteComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Invalidate a comment by setting its validity flag to false
     * 
     * @param commentID The ID of the comment to invalidate
     * @return True if the comment was invalidated successfully, false otherwise
     */
    boolean invalidateComment(int commentID) {
    try {
        mInvalidateComment.setInt(1, commentID);
        
        // Execute the update statement
        int rowsUpdated = mInvalidateComment.executeUpdate();
        // Return true if at least one row was updated
        return rowsUpdated > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }

    //UserDislikes Table
    /**
 * Create the user dislikes table in the database
 */
void createUserDislikesTable() {
    try {
        mCreateUserDislikesTable.execute();
        System.out.println("Dislike Table Created");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

/**
 * Remove user dislikes table from the database. If it does not exist, this will print
 * an error.
 */
void dropUserDislikesTable() {
    try {
        mDropUserDislikesTable.execute();
        System.out.println("Dislike Table Dropped");

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

/**
 * Insert a new user dislike into the userDislikes table
 *
 * @param username  The username of the user who disliked the message
 * @param messageID The ID of the message that was disliked
 * @return The number of rows that were inserted
 */
int insertUserDislike(String username, int messageID) {
    int count = 0;
    try {
        mInsertUserDislike.setString(1, username);
        mInsertUserDislike.setInt(2, messageID);
        count += mInsertUserDislike.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return count;
}

/**
 * Delete a user dislike from the userDislikes table
 *
 * @param dislikeID The ID of the dislike to delete
 * @return The number of rows that were deleted
 */
int deleteUserDislike(int dislikeID) {
    int count = 0;
    try {
        mDeleteUserDislike.setInt(1, dislikeID);
        count += mDeleteUserDislike.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return count;
}

/**
 * Retrieve all user dislikes from the userDislikes table
 *
 * @return A list of user dislikes
 */
ArrayList<RowData> selectAllUserDislikes() {
    ArrayList<RowData> userDislikes = new ArrayList<>();
    try {
        ResultSet rs = mSelectAllUserDislikes.executeQuery();
        while (rs.next()) {
            RowData userDislike = new RowData(
                    rs.getString("userID"),
                    rs.getInt("messageID")
            );
            userDislikes.add(userDislike);
        }
        rs.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return userDislikes;
}

/**
 * Retrieve a specific user dislike from the userDislikes table by its ID
 *
 * @param dislikeID The ID of the dislike to retrieve
 * @return The user dislike, or null if not found
 */
RowData selectOneUserDislike(int dislikeID) {
    RowData userDislike = null;
    try {
        mSelectOneUserDislike.setInt(1, dislikeID);
        ResultSet rs = mSelectOneUserDislike.executeQuery();
        if (rs.next()) {
            userDislike = new RowData(
                    rs.getString("username"),
                    rs.getInt("messageID")
            );
        }
        rs.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return userDislike;
}


}
// package edu.lehigh.cse216.team09.exh226.admin;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// import java.util.ArrayList;

// public class Database {
//     /**
//      * The connection to the database.  When there is no connection, it should
//      * be null.  Otherwise, there is a valid open connection
//      */
//     private Connection mConnection;

//     /**
//      * A prepared statement for getting all data in the database
//      */
//     private PreparedStatement mSelectAll;

//     /**
//      * A prepared statement for getting one row from the database
//      */
//     private PreparedStatement mSelectOne;

//     /**
//      * A prepared statement for deleting a row from the database
//      */
//     private PreparedStatement mDeleteOne;

//     /**
//      * A prepared statement for inserting into the database
//      */
//     private PreparedStatement mInsertOne;

//     /**
//      * A prepared statement for updating a single row in the database
//      */
//     private PreparedStatement mUpdateOne;

//     /**
//      * A prepared statement for creating the table in our database
//      */
//     private PreparedStatement mCreateTable;

//     /**
//      * A prepared statement for dropping the table in our database
//      */
//     private PreparedStatement mDropTable;

//     private PreparedStatement mInvalidateIdea;


//     /**
//      * RowData is like a struct in C: we use it to hold data, and we allow 
//      * direct access to its fields.  In the context of this Database, RowData 
//      * represents the data we'd see in a row.
//      * 
//      * We make RowData a static class of Database because we don't really want
//      * to encourage users to think of RowData as being anything other than an
//      * abstract representation of a row of the database.  RowData and the 
//      * Database are tightly coupled: if one changes, the other should too.
//      */
//     public static class RowData {
//         /**
//          * The ID of this row of the database
//          */
//         String mId;
//         /**
//          * The subject stored in this row
//          */
//         String mSubject;
//         /**
//          * The message stored in this row
//          */
//         String mMessage;

//         /**
//          * Construct a RowData object by providing values for its fields
//          */
//         public RowData(String userID, String subject, String message) {
//             mId = userID;
//             mSubject = subject;
//             mMessage = message;
//         }
//     }

//     /**
//      * The Database constructor is private: we only create Database objects 
//      * through the getDatabase() method.
//      */
//     private Database() {
//     }

//     /**
//      * Get a fully-configured connection to the database
//      * 
//      * @param ip   The IP address of the database server
//      * @param port The port on the database server to which connection requests
//      *             should be sent
//      * @param user The user ID to use when connecting
//      * @param pass The password to use when connecting
//      * 
//      * @return A Database object, or null if we cannot connect properly
//      */
//     static Database getDatabase(String ip, String port, String user, String pass) {
//         // Create an un-configured Database object
//         Database db = new Database();
        
//         // Give the Database object a connection, fail if we cannot get one
//         try {
//             Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
//             if (conn == null) {
//                 System.err.println("Error: DriverManager.getConnection() returned a null object");
//                 return null;
//             }
//             db.mConnection = conn;
//         } catch (SQLException e) {
//             System.err.println("Error: DriverManager.getConnection() threw a SQLException");
//             e.printStackTrace();
//             return null;
//         }

//         // Attempt to create all of our prepared statements.  If any of these 
//         // fail, the whole getDatabase() call should fail
//         try {
//             // NB: we can easily get ourselves in trouble here by typing the
//             //     SQL incorrectly.  We really should have things like "tblData"
//             //     as constants, and then build the strings for the statements
//             //     from those constants.

//             // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
//             // creation/deletion, so multiple executions will cause an exception
//             //USER TABLE
//             db.mCreateTable = db.mConnection.prepareStatement(
//                     "CREATE TABLE userTbl (userID VARCHAR(225), username VARCHAR(225), email VARCHAR(225), bio VARCHAR(225), valid boolean default false)");
            
//             // db.mCreateTable = db.mConnection.prepareStatement(
//             //         "CREATE TABLE userTbl (userID VARCHAR(225)  "
//             //         + "NOT NULL, message VARCHAR(500) NOT NULL)");
            
//             db.mDropTable = db.mConnection.prepareStatement("DROP TABLE userTbl");
//             //db.mCreateTable = db.mConnection.prepareStatement()
//             // Standard CRUD operations
//             db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM userTbl WHERE id = ?");
//             db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO userTbl VALUES (default, ?, ?)");
//             db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM userTbl");
//             db.mSelectOne = db.mConnection.prepareStatement("SELECT * from userTbl WHERE id=?");
//             db.mUpdateOne = db.mConnection.prepareStatement("UPDATE userTbl SET message = ? WHERE id = ?");

//             //MESSAGE TABLE
//             db.mCreateTable = db.mConnection.prepareStatement(
//                 "CREATE TABLE messageTbl (username VARCHAR(50), messageID int, title VARCHAR(225), content VARCHAR(225), likeCount int)");
//             // db.mCreateTable = db.mConnection.prepareStatement(
//             //     "CREATE TABLE messageTbl (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
//             //     + "NOT NULL, message VARCHAR(500) NOT NULL)");

//             db.mDropTable = db.mConnection.prepareStatement("DROP TABLE messageTbl");
//             //db.mCreateTable = db.mConnection.prepareStatement()
//             // Standard CRUD operations
//             db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM messageTbl WHERE id = ?");
//             db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO messageTbl VALUES (default, ?, ?)");
//             db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM messageTbl");
//             db.mSelectOne = db.mConnection.prepareStatement("SELECT * from messageTbl WHERE id=?");
//             db.mUpdateOne = db.mConnection.prepareStatement("UPDATE messageTbl SET message = ? WHERE id = ?");
//             db.mInvalidateIdea = db.mConnection.prepareStatement("UPDATE ideas SET valid = false WHERE ideaID = ?");


//             //USERLIKES TABLE
//             db.mCreateTable = db.mConnection.prepareStatement(
//                 "CREATE TABLE userLikesTbl (username VARCHAR(225), messageID int)");
//             // db.mCreateTable = db.mConnection.prepareStatement(
//             //     "CREATE TABLE messageTbl (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
//             //     + "NOT NULL, message VARCHAR(500) NOT NULL)");

//             db.mDropTable = db.mConnection.prepareStatement("DROP TABLE userLikesTbl");
//             //db.mCreateTable = db.mConnection.prepareStatement()
//             // Standard CRUD operations
//             db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM userLikesTbl WHERE id = ?");
//             db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO userLikesTbl VALUES (default, ?, ?)");
//             db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject FROM userLikesTbl");
//             db.mSelectOne = db.mConnection.prepareStatement("SELECT * from userLikesTbl WHERE id=?");
//             db.mUpdateOne = db.mConnection.prepareStatement("UPDATE userLikesTbl SET message = ? WHERE id = ?");

//             //USER DISLIKES TABLE
//             db.mCreateTable = db.mConnection.prepareStatement(
//                 "CREATE TABLE userDislikesTbl (username VARCHAR(225), messageID int)");
//             // db.mCreateTable = db.mConnection.prepareStatement(
//             //     "CREATE TABLE messageTbl (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
//             //     + "NOT NULL, message VARCHAR(500) NOT NULL)");

//             db.mDropTable = db.mConnection.prepareStatement("DROP TABLE userDislikesTbl");
//             //db.mCreateTable = db.mConnection.prepareStatement()
//             // Standard CRUD operations
//             db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM userDislikesTbl WHERE id = ?");
//             db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO userDislikesTbl VALUES (default, ?, ?)");
//             db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject FROM userDislikesTbl");
//             db.mSelectOne = db.mConnection.prepareStatement("SELECT * from userDislikesTbl WHERE id=?");
//             db.mUpdateOne = db.mConnection.prepareStatement("UPDATE userDislikesTbl SET message = ? WHERE id = ?");

//              //COMMENTS TABLE
//              db.mCreateTable = db.mConnection.prepareStatement(
//                 "CREATE TABLE commentsTbl (commentID int, messageID int, username VARCHAR(225), content VARCHAR(225))");
//             // db.mCreateTable = db.mConnection.prepareStatement(
//             //     "CREATE TABLE messageTbl (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
//             //     + "NOT NULL, message VARCHAR(500) NOT NULL)");

//             db.mDropTable = db.mConnection.prepareStatement("DROP TABLE commentsTbl");
//             //db.mCreateTable = db.mConnection.prepareStatement()
//             // Standard CRUD operations
//             db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM commentsTbl WHERE id = ?");
//             db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO commentsTbl VALUES (default, ?, ?)");
//             db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM commentsTbl");
//             db.mSelectOne = db.mConnection.prepareStatement("SELECT * from commentsTbl WHERE id=?");
//             db.mUpdateOne = db.mConnection.prepareStatement("UPDATE commentsTbl SET message = ? WHERE id = ?");
//         } catch (SQLException e) {
//             System.err.println("Error creating prepared statement");
//             e.printStackTrace();
//             db.disconnect();
//             return null;
//         }
//         return db;
//     }

//     /**
//      * Close the current connection to the database, if one exists.
//      * 
//      * NB: The connection will always be null after this call, even if an 
//      *     error occurred during the closing operation.
//      * 
//      * @return True if the connection was cleanly closed, false otherwise
//      */
//     boolean disconnect() {
//         if (mConnection == null) {
//             System.err.println("Unable to close connection: Connection was null");
//             return false;
//         }
//         try {
//             mConnection.close();
//         } catch (SQLException e) {
//             System.err.println("Error: Connection.close() threw a SQLException");
//             e.printStackTrace();
//             mConnection = null;
//             return false;
//         }
//         mConnection = null;
//         return true;
//     }

//     /**
//      * Insert a row into the database
//      * 
//      * @param subject The subject for this new row
//      * @param message The message body for this new row
//      * 
//      * @return The number of rows that were inserted
//      */
//     int insertRow(String subject, String message) {
//         int count = 0;
//         try {
//             mInsertOne.setString(1, subject);
//             mInsertOne.setString(2, message);
//             count += mInsertOne.executeUpdate();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return count;
//     }

//     /**
//      * Query the database for a list of all subjects and their IDs
//      * 
//      * @return All rows, as an ArrayList
//      */
//     ArrayList<RowData> selectAll() {
//         ArrayList<RowData> res = new ArrayList<>();
//         try {
//             ResultSet rs = mSelectAll.executeQuery();
//             while (rs.next()) {
//                 String userid = rs.getString("userid"); // Assuming "id" is the column name for the row ID
//                 String content = rs.getString("content"); // Adjust column name as per your database schema
//                 System.out.println(content);
//                 res.add(new RowData(userid, content, null));
//             }
//             rs.close();
//             return res;
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }
    

//     /**
//      * Get all data for a specific row, by ID
//      * 
//      * @param id The id of the row being requested
//      * 
//      * @return The data for the requested row, or null if the ID was invalid
//      */
//     RowData selectOne(int id) {
//         RowData res = null;
//         try {
//             mSelectOne.setInt(1, id);
//             ResultSet rs = mSelectOne.executeQuery();
//             if (rs.next()) {
//                 res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return res;
//     }

//     /**
//      * Delete a row by ID
//      * 
//      * @param id The id of the row to delete
//      * 
//      * @return The number of rows that were deleted.  -1 indicates an error.
//      */
//     int deleteRow(int id) {
//         int res = -1;
//         try {
//             mDeleteOne.setInt(1, id);
//             res = mDeleteOne.executeUpdate();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return res;
//     }

//     /**
//      * Update the message for a row in the database
//      * 
//      * @param id The id of the row to update
//      * @param message The new message contents
//      * 
//      * @return The number of rows that were updated.  -1 indicates an error.
//      */
//     int updateOne(int id, String message) {
//         int res = -1;
//         try {
//             mUpdateOne.setString(1, message);
//             mUpdateOne.setInt(2, id);
//             res = mUpdateOne.executeUpdate();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return res;
//     }

//     /**
//      * Create tblData.  If it already exists, this will print an error
//      */
//     void createTable() {
//         try {
//             mCreateTable.execute();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     /**
//      * Remove tblData from the database.  If it does not exist, this will print
//      * an error.
//      */
//     void dropTable() {
//         try {
//             mDropTable.execute();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     /**
//      * Invalidate an idea by setting its validity flag to false
//      * 
//      * @param ideaID The ID of the idea to invalidate
//      * @return True if the idea was invalidated successfully, false otherwise
//      */

//     boolean invalidateIdea(int ideaID) {
//         try {
//             mInvalidateIdea.setInt(1, ideaID);
//             int rowsUpdated = mInvalidateIdea.executeUpdate();
//             return rowsUpdated > 0;
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }
// }
