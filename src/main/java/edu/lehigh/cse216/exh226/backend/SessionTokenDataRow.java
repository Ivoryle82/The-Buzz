package edu.lehigh.cse216.exh226.backend;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class SessionTokenDataRow: represents a row in the SQL SessionTokenTbl
 * NB: This class needs no getter or setters, this is because
 * we use the google Gson library which will convert the instantiation
 * of this class into a JSON object format (which will be sent to frontend)
 */

public class SessionTokenDataRow {
    public int mSessionToken;
    public Date mDateCreated; // the date created

    /**
     * UserLikesDataRow constructor
     * 
     * @param sessionToken : an int for the session token
     * @param dateCreated  : a String for the date created, format must be:
     *                     "yyyy-MM-dd HH:mm:ss"
     * 
     * @return : a SessionTokenDataRow object
     */
    SessionTokenDataRow(int sessionToken, String dateCreated) {
        mSessionToken = sessionToken;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            mDateCreated = dateFormat.parse(dateCreated);
        } catch (ParseException e) {
            System.out.println("Error parsing date while constructing SessionTokenDataRow");
        }
    }
}