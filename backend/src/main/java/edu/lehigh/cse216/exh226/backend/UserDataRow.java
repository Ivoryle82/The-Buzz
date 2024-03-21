package edu.lehigh.cse216.exh226.backend;

import java.util.Date;

/**
 * Class UserDataRow: represents a row in the SQL userTbl
 * NB: This class needs no getter or setters, this is because
 * we use the google Gson library which will convert the instantiation
 * of this class into a JSON object format (which will be sent to frontend)
 */

public class UserDataRow {
    public String mUsername; // the username (unique primary key)
    public String mPassword; // the password
    public String mBio; // the user's biography
    public String mEmail; // the user's email
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
    UserDataRow(String username, String password, String bio, String email) {
        mUsername = username;
        mPassword = password;
        mBio = bio;
        mEmail = email;
        mDateCreated = new Date();
    }
}
