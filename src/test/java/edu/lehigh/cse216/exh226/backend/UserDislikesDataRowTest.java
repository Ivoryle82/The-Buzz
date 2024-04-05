package edu.lehigh.cse216.exh226.backend;

import edu.lehigh.cse216.exh226.backend.UserDislikesDataRow;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UserDislikesDataRowTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName : name of the test case
     */
    public UserDislikesDataRowTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(UserDislikesDataRowTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it creates
     */
    public void testConstructor() {
        String userID = "testing userID"; // the username
        int messageID = 456; // the messageID
        UserDislikesDataRow testUserDislikesDataRow = new UserDislikesDataRow(userID, messageID);
        assertTrue(testUserDislikesDataRow.mUserID.equals(userID));
        assertTrue(testUserDislikesDataRow.mMessageID == messageID);
        assertFalse(testUserDislikesDataRow.mDateCreated == null); // because this is automatically created, if it is
                                                                   // null
        // something went wrong
    }
}