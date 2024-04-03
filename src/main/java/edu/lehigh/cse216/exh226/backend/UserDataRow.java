package edu.lehigh.cse216.exh226.backend;

import java.util.Date;

/**
 * Class UserDataRow: represents a row in the SQL userTbl
 * NB: This class needs no getter or setters, this is because
 * we use the google Gson library which will convert the instantiation
 * of this class into a JSON object format (which will be sent to frontend)
 * 
 * NB: actually we need a getter to get the current password specifically hwne
 * we login
 */

public class UserDataRow {
    public String mUserID; // the userID
    public String mUsername; // the username (unique primary key)
    public String mEmail; // the user's email
    public String mBio; // the user's biography
    public final Date mDateCreated; // the date created

    /**
     * UserDataRow constructor
     * 
     * @param username : a string for the username (primary key)
     * @param password : a string for the password
     * @param bio      : a string for the user's biography
     * @param email    : a string for the user's email
     * 
     * @return : a UserDataRow object
     */
    UserDataRow(String userID, String username, String email, String bio) {
        mUserID = userID;
        mUsername = username;
        mEmail = email;
        mBio = bio;
        mDateCreated = new Date();
    }

    /**
     * getPassword() : returns the current password
     * TECH DEBT: PROBABLY NOT SAFE TO DO THIS
     */
    // public String getPassword() {
    // return mPassword;
    // }
    // TECH DEBT: DELETE THIS METHOD
}
