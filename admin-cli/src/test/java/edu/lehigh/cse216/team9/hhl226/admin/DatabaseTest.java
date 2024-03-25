package edu.lehigh.cse216.team9.hhl226.admin;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Map;

public class DatabaseTest extends TestCase {
    private Database db;
    private Map<String, String> env;

    public void setUp() {
    // Set up the environment variables
    env = System.getenv();
    String ip = env.get("POSTGRES_IP");
    String port = env.get("POSTGRES_PORT");
    String user = env.get("POSTGRES_USER");
    String pass = env.get("POSTGRES_PASS");

    // Get a fully-configured connection to the database
    db = Database.getDatabase(ip, port, user, pass);

    // Check if db is null
    if (db == null) {
        fail("Failed to initialize database connection");
    }
}

    protected void tearDown() {
        // Disconnect from the database
        db.disconnect();
    }

    public void testInsertRow() {
        // Insert a row into the database
        int rowsAdded = db.insertRow("Test Subject", "Test Message");

        // Check if the row was successfully added
        assertEquals(1, rowsAdded);
    }

    public void testSelectAll() {
        // Insert a row into the database
        db.insertRow("Test Subject", "Test Message");

        // Get all rows from the database
        ArrayList<Database.RowData> rows = db.selectAll();

        // Check if at least one row was returned
        assertNotNull(rows);
        assertFalse(rows.isEmpty());
    }

    // Add more test cases for other methods in the Database class
}
