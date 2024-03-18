package edu.lehigh.cse216.exh226.backend.controllers;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.lehigh.cse216.exh226.backend.fields.Message;

public class MessageController {

    private static PreparedStatement mDeleteOne;
    private static PreparedStatement mSelectOne;
    private static PreparedStatement mSelectAll;
    private static PreparedStatement mUpdateOne;
    private static PreparedStatement mInsertOne;

    public MessageController(Connection mConnection) throws SQLException {
        mSelectAll = mConnection.prepareStatement("SELECT * FROM message ORDER BY PID DESC");
        mSelectOne = mConnection.prepareStatement("SELECT * FROM message WHERE PID=?");
        mUpdateOne = mConnection.prepareStatement("UPDATE message SET content = ? WHERE PID = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO message VALUES (default, ? , ?)");

    }

    // this one im having trouble with
    public int insertRow(String UID, String content) {
        int count = 0;
        try {
            mInsertOne.setString(1, UID);
            mInsertOne.setString(2, content);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<Message> selectAll() {
        ArrayList<Message> res = new ArrayList<Message>();
        try {
            ResultSet rs = mSelectAll.executeQuery(); // executeQuery i assumed executes SQL. So here it is selecting
                                                      // all via the PreparedStatement to select all
            while (rs.next()) {
                res.add(new Message(rs.getInt("PID"), rs.getString("UID"), null));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message selectOne(int PID) {
        Message res = null;
        try {
            mSelectOne.setInt(1, PID);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) { // Why do we do rs.next()?
                res = new Message(rs.getInt("PID"), rs.getString("UID"), rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int deleteMessage(int PID) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, PID);
            res = mDeleteOne.executeUpdate(); // Execute update differs from execute query
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateMessage(int PID, String content) {
        int res = -1;
        try {
            mUpdateOne.setInt(1, PID);
            mUpdateOne.setString(2, content);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
