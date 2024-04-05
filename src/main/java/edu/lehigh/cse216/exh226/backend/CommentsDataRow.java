package edu.lehigh.cse216.exh226.backend;

import java.util.Date;

import org.checkerframework.checker.units.qual.mm;

/**
 * Class CommentsDataRow: represents a row in the SQL commentsTbl
 * NB: This class needs no getter or setters, this is because
 * we use the google Gson library which will convert the instantiation
 * of this class into a JSON object format (which will be sent to frontend)
 */

public class CommentsDataRow {
    public int mCommentID;
    public int mMessageID;
    public String mUserID;
    public String mContent;
    public final Date mDateCreated; // the date created

    /**
     * CommentsDataRow constructor
     * 
     * @param commentID : an int for the commentID
     * @param messageID : an int for the message that is being commented
     * @param userID    : an String for the userID of the user that made the comment
     * @param content   : a String for the content of the comment
     */
    CommentsDataRow(int commentID, int messageID, String userID, String content) {
        mCommentID = commentID;
        mMessageID = messageID;
        mUserID = userID;
        mContent = content;
        mDateCreated = new Date();
    }
}
