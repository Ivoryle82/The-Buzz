package edu.lehigh.cse216.exh226.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should be
     * null. Otherwise, there is a valid open connection.
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting on row form the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inseting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statment for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;
    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statment for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    private Database createPreparedStatements() {
        // NB: when refactoring i get rid of the db because when calling
        // db.createPreparedStatements()
        // createPreparedStatements is a method of the class so it will access the
        // instance db's attributes
        // Attempt to create all our our prepared statements. If any of these
        // shall fail, the whole getDatabase() call should fail
        // NB: PreparedStatement(s) are prepared into the connection with
        // .prepareStatement("")
        // The parameter is SQL language
        try {
            // NB: a common error here is typing the SQL incorrectly (this could mess up our
            // database)
            // A better practice is to set constants for SQL such as "tblData"

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception
            mCreateTable = mConnection.prepareStatement(
                    "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL, message VARCHAR(500) NOT NULL)");
            mDropTable = mConnection.prepareStatement("DROP TABLE tblData");

            // Standard CRUD operations
            mDeleteOne = mConnection.prepareStatement("DELETE FROM tblDATA WHERE id = ?");
            mInsertOne = mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            mSelectAll = mConnection.prepareStatement("SELECT id, subject FROM tblData");
            mSelectOne = mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            mUpdateOne = mConnection.prepareStatement("UPDATE tblData SET message = ? WHERE id = ?");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
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
     * Get a fully-configured connection to the database
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

        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            String dbUrl = "jdbc:postgresql://" + host + ":" + port + path;
            // System.out.printf("TEST: %s", user);
            // System.out.printf("TEST: %s", pass);
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
     * Get a fully-configured connection to the database
     * 
     * @param db_url       The postgres database url
     * @param port_default port to use if absent in db_url this will be 5432 in the
     *                     program
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url, String port_default) {
        // 2nd method is just meant to parse a db_url. then we call the first
        try {
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String host = dbUri.getHost();
            String path = dbUri.getPath();
            String port = dbUri.getPort() == -1 ? port_default : Integer.toString(dbUri.getPort());

            return getDatabase(host, port, path, username, password);
        } catch (URISyntaxException e) {
            System.err.println("Error: URI Syntax Error");
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Close the currect connection to the database, if one exists
     * 
     * NB: The connection will always be null after this call, even if an error
     * occurs during the closing operation
     * 
     * @return True if teh connection was cleanly close, false otherwise
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

    /**
     * Insert a row into the database
     * 
     * @param subject : The subject for this new row
     * @param message : The message body for this new row
     * 
     * @return : The number of rows that were inserted
     */
    int insertRow(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject); // I know these insert into (default, ?, ?) but how
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * Equivalent to DataStore.readAll
     * 
     * @return All rows, as an ArrayList of RowData (rows) objects
     */
    ArrayList<DataRow> selectAll() {
        ArrayList<DataRow> res = new ArrayList<DataRow>();
        try {
            ResultSet rs = mSelectAll.executeQuery(); // executeQuery i assumed executes SQL. So here it is selecting
                                                      // all via the PreparedStatement to select all
            while (rs.next()) {
                res.add(new DataRow(rs.getInt("id"), rs.getString("subject"), null));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id : The id of the row beign requested
     * 
     * @return :The data for the requested row (RowData object), or null if invalid
     *         ID
     */
    DataRow selectOne(int id) {
        DataRow res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) { // Why do we do rs.next()?
                res = new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id : The id of the row to delete
     * 
     * @return : The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate(); // Execute update differs from execute query
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id      : The id of the row to update
     * @param message : the new message contents
     * 
     * @return : The number of rows that were updated. -1 indicates an error
     */
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData. If it already exists, print an error
     * we know it creates tblData bc that is the name from the PreparedStatement
     */
    void createTable() {
        try {
            mCreateTable.execute(); // another new execute command, prob differs based on SQL content
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, print an error
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
