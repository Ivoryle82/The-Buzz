package edu.lehigh.cse216.exh226.backend;

import java.util.ArrayList;

/**
 * DataStore provides access to a set of objects, and makes sure that each has a
 * unique identifier that remains unique even after the object is declared
 * 
 * We follow the convention that member fields of a class have names that start
 * w/ a lowercase m and are in camelCase
 * 
 * NB: The methods of DataStore are synchronized, since they will be used from
 * web framework and there may be multiple concurrent accesses to the DataStore
 */

public class DataStore {
    /**
     * The rows of data in our DataStore
     */
    private ArrayList<DataRow> mRows;

    /**
     * A counter for keeping track of the next ID to assign to a new row
     */
    private int mCounter;

    // private Database db;

    /**
     * Construct the DataStore by resetting its counter and creating the ArrayList
     * for the rows of data
     */
    DataStore() {
        mCounter = 0;
        mRows = new ArrayList<>();
        // db = database;
    }

    /**
     * Add a new row to the DataStore
     * 
     * Note: we return -1 on error. In more robust code, returning -1 is not
     * sufficient error handling. Better to use exceptions
     * 
     * @param title   : The title for this newly added row
     * @param content : The content for this row
     * @return : the ID of teh new row, or -1 if no row was created
     */
    public synchronized int createEntry(String title, String content) {
        if (title == null || content == null) {
            return -1;
        }

        // NB: We can safely assume that id is greater than the largest index in mRows,
        // and thus we can use the index-based add() method
        int id = mCounter++;
        DataRow data = new DataRow(id, title, content);
        mRows.add(id, data);

        return id;
    }

    /**
     * Get one complete row from the DataStore using its ID to select it
     * 
     * @param id : the id of the row to select
     * @return A copy of the data in the row, if it exists, or null otherwise
     */
    public synchronized DataRow readOne(int id) {
        if (id >= mRows.size()) {
            return null;
        }
        DataRow data = mRows.get(id);
        if (data == null) {
            return null;
        }
        return new DataRow(data);
    }

    /**
     * Get all of the ids and titles that are present in the DataStore
     * 
     * @return An ArrayList with all of the data
     */
    public synchronized ArrayList<DataRowLite> readAll() {
        ArrayList<DataRowLite> data = new ArrayList<>();
        // NB: We copy the data, so that our ArrayList only has ids and titles
        for (DataRow row : mRows) {
            if (row != null) {
                data.add(new DataRowLite(row));
            }
        }
        return data;
    }

    /**
     * updateOne() : updates an existing entry
     * 
     * @param id      : the id of the row to select
     * @param title   : the string for the new title
     * @param content : the string for the new content
     * 
     * @return a copy of the data in the row , so we can return what we updated
     *         afterwards
     */
    public synchronized DataRow updateOne(int id, String title, String content) {
        if (title == null || content == null) {
            return null; // do not update if no input given
        }
        if (id >= mRows.size()) {
            return null; // id is invalid
        }
        DataRow data = mRows.get(id);
        if (data == null) {
            return null;
        }
        // Update and then return a copy of the data
        data.mTitle = title;
        data.mContent = content;
        return new DataRow(data);
    }

    /**
     * Delete a row from the DataStore
     * 
     * @param id : The Id of the row to delete
     * @return true if the row was deleted, false otherwise
     */
    public synchronized boolean deleteOne(int id) {
        // Deletion fails for invalid Id or an Id that has already been deleted
        if (id >= mRows.size()) {
            return false;
        }
        if (mRows.get(id) == null) {
            return false;
        }

        // Delete by setting to null, so that any Ids used by other clients will
        // continue to refer to their current ArrayList index
        mRows.set(id, null);
        return true;
    }

    // NB: The synchronized type makes it so all methods with the synchronized
    // keyword
    // Only run one at a time, that way only one thread can access DataStore at any
    // Point in time
}
