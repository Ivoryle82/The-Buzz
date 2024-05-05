package edu.lehigh.cse216.team09.exh226.admin;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;

public class DatabaseTest extends TestCase {
    private Database database;

    public DatabaseTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(DatabaseTest.class);
    }

    public void setUp() {
        // Set up the database connection for each test
        database = Database.getDatabase("jelani.db.elephantsql.com", "5432", "zpzzdvck", "bB7p6OwexymsYGtZphn1VxrD8TX-zk27");
        assertNotNull(database); // Ensure database connection is successful
    }

    public void tearDown() {
        // Close the database connection after each test
        assertTrue(database.disconnect());
    }

    public void testInsertMessage() {
        int result = database.insertMessage("userID", 0, "Title", "Content", 0, 0, "fileId");
        assertEquals(1, result); // Expecting one row to be inserted
    }

    public void testSelectAllMessages() {
        ArrayList<Database.RowData> messages = database.selectAllMessages();
        assertNotNull(messages); // Expecting non-null array list
    }

    public void testSelectOneMessage() {
        Database.RowData message = database.selectOneMessage(0);
        assertNotNull(message); // Expecting non-null message object
    }

    public void testUpdateMessage() {
        int result = database.updateMessage(0, "Updated Title", "Updated Content", 1, 1, "file");
        assertTrue(result != 0);
    }

    public void testDeleteMessage() {
        int result = database.deleteMessage(0);
        assertTrue(result != 0);
    }

    // public void testInvalidateIdea() {
    //     boolean result = database.invalidateIdea(1);
    //     assertTrue(result); // Expecting idea to be invalidated successfully
    // }

}
