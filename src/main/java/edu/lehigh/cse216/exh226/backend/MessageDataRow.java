package edu.lehigh.cse216.exh226.backend;

import java.util.Date;

/**
 * Class MessageDataRow: represents a row in the SQL messageTbl
 * NB: This class needs no getter or setters, this is because
 * we use the google Gson library which will convert the instantiation
 * of this class into a JSON object format (which will be sent to frontend)
 */

public class MessageDataRow {
    public int mMessageID; // the messageID (unique primary key) (this will be an int later)
    public String mUserID; // the UserID (unique foreign key)
    public String mTitle; // the message's title
    public String mContent; // the message's content
    public int mLikeCount; // the message's amount of likes
    public int mDislikeCount; // the message's amount of dislikes
    public final Date mDateCreated; // the date created

    /**
     * MessageDataRow constructor
     * 
     * @param messageID : a String for the message's ID
     * @param username  : a String for the UserID (who made the message)
     * @param title     : a String for the title of the message
     * @param content   : a String for the message's content
     * @param likeCount : an int for the amount of likes a message has
     * 
     * @return : a MessageDataRow object
     */
    MessageDataRow(int messageID, String UserID, String title, String content, int likeCount, int dislikeCount) {
        mMessageID = messageID;
        mUserID = UserID;
        mTitle = title;
        mContent = content;
        mLikeCount = likeCount; // when a user creates a message this will be 0 always
        mDislikeCount = dislikeCount; // when a user creates a message this will be 0 always
        mDateCreated = new Date();
    }
}
