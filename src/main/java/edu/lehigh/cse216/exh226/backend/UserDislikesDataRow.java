package edu.lehigh.cse216.exh226.backend;

import java.util.Date;

/**
 * Class UserDislikesDataRow: represents a row in the SQL userLikesTbl
 * NB: This class needs no getter or setters, this is because
 * we use the google Gson library which will convert the instantiation
 * of this class into a JSON object format (which will be sent to frontend)
 */

public class UserDislikesDataRow {
    public String mUserID; // the userID
    public int mMessageID; // the messageID
    public final Date mDateCreated; // the date created

    /**
     * UserLikesDataRow constructor
     * 
     * @param username  : a string for the username
     * @param messageID : a string for the messageID
     * 
     * @return : a UserLikesDataRow object
     */
    UserDislikesDataRow(String userID, int messageID) {
        mUserID = userID;
        mMessageID = messageID;
        mDateCreated = new Date();
    }
}
