package edu.lehigh.cse216.exh226.backend;

import edu.lehigh.cse216.exh226.backend.UserLikesDataRow;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UserLikesDataRowTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName : name of the test case
     */
    public UserLikesDataRowTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(UserLikesDataRowTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it creates
     */
    public void testConstructor() {
        String username = "testing username"; // the username
        int messageID = 456; // the messageID
        UserLikesDataRow testUserLikesDataRow = new UserLikesDataRow(username, messageID);
        assertTrue(testUserLikesDataRow.mUsername.equals(username));
        assertTrue(testUserLikesDataRow.mMessageID == messageID);
        assertFalse(testUserLikesDataRow.mDateCreated == null); // because this is automatically created, if it is null
        // something went wrong
    }
}