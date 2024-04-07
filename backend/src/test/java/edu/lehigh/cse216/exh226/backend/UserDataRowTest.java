package edu.lehigh.cse216.exh226.backend;

import edu.lehigh.cse216.exh226.backend.UserDataRow;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UserDataRowTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName : name of the test case
     */
    public UserDataRowTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(UserDataRowTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it creates
     */
    public void testConstructor() {
        String username = "testing username"; // the username
        String password = "testing password"; // the password
        String bio = "testing bio"; // the user's biography
        String email = "testing email"; // the user's email
        UserDataRow testUserDataRow = new UserDataRow(username, password, bio, email);
        assertTrue(testUserDataRow.mUsername.equals(username));
        assertTrue(testUserDataRow.mPassword.equals(password));
        assertTrue(testUserDataRow.mBio.equals(bio));
        assertTrue(testUserDataRow.mEmail.equals(email));
        assertFalse(testUserDataRow.mDateCreated == null); // because this is automatically created, if it is null
                                                           // something went wrong
    }
}
