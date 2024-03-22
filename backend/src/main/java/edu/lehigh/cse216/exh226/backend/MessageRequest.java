package edu.lehigh.cse216.exh226.backend;

/**
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor
 */

public class MessageRequest {
    /**
     * The username being provided by the web front end
     */
    public String mUsername;

    /**
     * The title being provided by the web front end
     */
    public String mTitle;

    /**
     * The content being provided by the web front end
     */
    public String mContent;
}
