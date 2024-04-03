// package edu.lehigh.cse216.exh226.backend.controllers;

// import java.sql.PreparedStatement;
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;

// import edu.lehigh.cse216.exh226.backend.fields.Interaction;

// public class InteractionController {

// private static PreparedStatement mDeleteOne;
// private static PreparedStatement mSelectOne;
// private static PreparedStatement mSelectAll;
// private static PreparedStatement mUpdateOne;
// private static PreparedStatement mInsertOne;

// public InteractionController(Connection mConnection) throws SQLException {
// mSelectAll = mConnection.prepareStatement("SELECT * FROM interaction ORDER BY
// PID DESC");
// mSelectOne = mConnection.prepareStatement("SELECT * FROM interaction WHERE
// PID=?");
// mUpdateOne = mConnection.prepareStatement("UPDATE interaction SET LoD = ?
// WHERE PID = ?");
// mInsertOne = mConnection.prepareStatement("INSERT INTO interaction VALUES (?,
// ? , ?)");

// }

// // this one im having trouble with
// public int insertRow(int PID, String UIID, boolean LoD) {
// int count = 0;
// try {
// mInsertOne.setInt(1, PID);
// mInsertOne.setString(2, UIID);
// mInsertOne.setBoolean(3, LoD);
// // count += mInsertOne.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return count;
// }

// public ArrayList<Interaction> selectAll() {
// ArrayList<Interaction> res = new ArrayList<Interaction>();
// try {
// ResultSet rs = mSelectAll.executeQuery(); // executeQuery i assumed executes
// SQL. So here it is selecting
// // all via the PreparedStatement to select all
// while (rs.next()) {
// res.add(new Interaction(rs.getInt("PID"), rs.getString("UIID"),
// rs.getBoolean("LoD")));
// }
// rs.close();
// return res;
// } catch (SQLException e) {
// e.printStackTrace();
// return null;
// }
// }

// public Interaction selectOne(int PID) {
// Interaction res = null;
// try {
// mSelectOne.setInt(1, PID);
// ResultSet rs = mSelectOne.executeQuery();
// if (rs.next()) { // Why do we do rs.next()?
// res = new Interaction(rs.getInt("PID"), rs.getString("UIID"),
// rs.getBoolean("LoD"));
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return res;
// }

// public int deleteInteraction(int PID) {
// int res = -1;
// try {
// mDeleteOne.setInt(1, PID);
// res = mDeleteOne.executeUpdate(); // Execute update differs from execute
// query
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return res;
// }

// int updateInteraction(int PID, Boolean LoD) {
// int res = -1;
// try {
// mUpdateOne.setInt(1, PID);
// mUpdateOne.setBoolean(2, LoD);
// res = mUpdateOne.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return res;
// }
// }
