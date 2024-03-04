package edu.lehigh.cse216.exh226.backend;

import java.util.Date;

/**
 * DataRow holds a row of information. A row consists of:
 * an identifier, strings for "title" & "content", and a creation date
 * 
 * Because we will convert instances of the object to JSON, fields must be
 * public --> so no getters and setters are required
 */

public class DataRow {
    /**
     * The unique identifier associated with this element. Keyword final bc this
     * will never change for each instance
     */
    public final int mId;

    /**
     * The title for this row of data
     */
    public String mTitle;

    /**
     * The content for this row of data
     */
    public String mContent;

    /**
     * The creation date for this row of data. Once set it cannot be changed
     */
    public final Date mCreated;

    /**
     * Create a new DataRow with provided id and title/content, and assign a new
     * creation date based on the system clock when constructor is called
     * 
     * @param id      : the id to associate with this row. Assumed to be unique
     *                throughout the whole program
     * 
     * @param title   : The title string for this row of data
     * @param content : the content string for this row of data
     */
    DataRow(int id, String title, String content) {
        mId = id;
        mTitle = title;
        mContent = content;
        mCreated = new Date();
    }

    /**
     * Copy constructor to create one datarow from another
     */
    DataRow(DataRow data) {
        mId = data.mId;
        // NB: Strings and Dates are immutable, so copy-by-reference is safe
        mTitle = data.mTitle;
        mContent = data.mContent;
        mCreated = data.mCreated;
    }
}
