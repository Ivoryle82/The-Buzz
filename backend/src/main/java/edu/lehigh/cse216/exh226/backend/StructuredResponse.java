package edu.lehigh.cse216.exh226.backend;

/**
 * StructuredResponse provides a common format for success and failure messages
 * with an optional payload of type Object that can be converted into JSON
 * 
 * NB: since this will be converted into JSON, all fields must be public
 */
public class StructuredResponse {
    /**
     * The status is a String that the application can use to quickly determine
     * if the response indicates an error. Values will include "ok" and "error".
     * Subject to change
     */
    public String mStatus;

    /**
     * The message is only useful when there is an error or when there is null data
     */
    public String mMessage;

    /**
     * Any JSON-friendly object can be referenced here, so that we can have a rich
     * reply to the client
     * 
     * This will be the data queried from the SQL database, Its important to
     * understand that this field is important to understand. But seriously, when
     * doing anything in the routes the data in here is needed to display the
     * database contents and also for other functionalities.
     * 
     * EX: Logging in route, will return the SQL data for the user from the userTbl.
     * This will have the username which will have to be stored in a variable for
     * other interactions (which will require web front end to pass that variable in
     * the request parameters)
     * 
     * EX: Liking a message route, will return the SQL data for the message in
     * messageTbl, important for updating the message to display the updated like
     * count
     */
    public Object mData;

    /**
     * StructuredResponse: constructs a response object that is sent back to the
     * web front end in the response.body after the request is processed.
     * 
     * ============================INFO FOR WEB FRONT END=========================
     * When the Gson library converts to JSON from Object the keys are 'mStatus',
     * 'mMessage', 'mData'
     * For 'mData' it will create a JSON object to represent the ArrayList, (NOTE:
     * ALL JSON OBJETS FUNCTION AS HASHMAPS), to get the element of the ArrayList
     * response.mData['elementKey']
     * 
     * @param status  : The status of the response, typically "ok" or "error"
     * @param message : The message to go along with an error status
     * @param data    : An object that will contain information queried from the
     *                database. This information is extremely useful see above
     *                examples
     */
    public StructuredResponse(String status, String message, Object data) {
        mStatus = (status != null) ? status : "invalid";
        mMessage = message;
        mData = data;
    }
}
