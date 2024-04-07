package edu.lehigh.cse216.exh226.backend.fields;

import java.util.Date;

/**
 * DataRow holds a row of information. A row consists of:
 * an identifier, strings for "title" & "content", and a creation date
 * 
 * Because we will convert instances of the object to JSON, fields must be
 * public --> so no getters and setters are required
 */

public class Interaction {
    /**
     * This Will be the post id of the post being interacted with
     */
    public final int PID;

    /**
     * This is the id of the user who interacted with the post
     */
    public final String UIID;

    /**
     * This is the interaction true or false for like or dislike
     */
    public boolean LoD;

    /**
     * The creation date for this row of data. Once set it cannot be changed
     */
    public final Date mCreated;

    /**
     * Create a row of interactions
     * 
     * @param PID  This is the unqiue postId
     * @param UIID : the userID of the person who interacted
     * @param LoD  : the like or dislike
     */
    public Interaction(int PID, String UIID, boolean LoD) {
        this.PID = PID;
        this.UIID = UIID;
        this.LoD = LoD;
        mCreated = new Date();
    }

    /**
     * Copy constructor to create one datarow from another
     */
    Interaction(Interaction data) {
        PID = data.PID;
        UIID = data.UIID;
        LoD = data.LoD;
        mCreated = data.mCreated;
    }
}
