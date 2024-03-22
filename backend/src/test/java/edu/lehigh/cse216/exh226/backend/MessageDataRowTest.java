package edu.lehigh.cse216.exh226.backend;

import edu.lehigh.cse216.exh226.backend.MessageDataRow;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MessageDataRowTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName : name of the test case
     */
    public MessageDataRowTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(MessageDataRowTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it creates
     */
    public void testConstructor() {
        String messageID = "testing messageID"; // the messageID (unique primary key) (this will be an int later)
        String username = "testing username"; // the username (unique foreign key)
        String title = "testing title"; // the message's title
        String content = "testing content"; // the message's content
        int likeCount = 200; // the message's amount of likes
        MessageDataRow testMessageDataRow = new MessageDataRow(messageID, username, title, content, likeCount);
        assertTrue(testMessageDataRow.mMessageID.equals(messageID));
        assertTrue(testMessageDataRow.mUsername.equals(username));
        assertTrue(testMessageDataRow.mTitle.equals(title));
        assertTrue(testMessageDataRow.mContent.equals(content));
        assertTrue(testMessageDataRow.mLikeCount == likeCount);
        assertFalse(testMessageDataRow.mDateCreated == null); // because this is automatically created, if it is null
                                                              // something went wrong
    }
}
