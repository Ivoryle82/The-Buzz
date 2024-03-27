// package edu.lehigh.cse216.exh226.backend.controllers;

// import java.sql.PreparedStatement;
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;

// import edu.lehigh.cse216.exh226.backend.fields.User;

// public class UserController {

// private static PreparedStatement mDeleteOne;
// private static PreparedStatement mSelectOne;
// private static PreparedStatement mSelectAll;
// private static PreparedStatement mUpdateOne;
// private static PreparedStatement mInsertOne;

// public UserController(Connection mConnection) throws SQLException {
// mSelectAll = mConnection.prepareStatement("SELECT * FROM user ORDER BY UID
// DESC");
// mSelectOne = mConnection.prepareStatement("SELECT * FROM user WHERE UID=?");
// mUpdateOne = mConnection.prepareStatement("UPDATE user SET Bio = ? WHERE UID
// = ?");
// mInsertOne = mConnection.prepareStatement("INSERT INTO user VALUES (?, ?)");

// }

// // this one im having trouble with
// public int insertRow(String UID, String Bio) {
// int count = 0;
// try {
// mInsertOne.setString(1, UID);
// mInsertOne.setString(2, Bio);
// // count += mInsertOne.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return count;
// }

// public ArrayList<User> selectAll() {
// ArrayList<User> res = new ArrayList<User>();
// try {
// ResultSet rs = mSelectAll.executeQuery(); // executeQuery i assumed executes
// SQL. So here it is selecting
// // all via the PreparedStatement to select all
// while (rs.next()) {
// res.add(new User(rs.getString("UID"), rs.getString("Bio")));
// }
// rs.close();
// return res;
// } catch (SQLException e) {
// e.printStackTrace();
// return null;
// }
// }

// public User selectOne(String UID) {
// User res = null;
// try {
// mSelectOne.setString(1, UID);
// ResultSet rs = mSelectOne.executeQuery();
// if (rs.next()) { // Why do we do rs.next()?
// res = new User(rs.getString("UID"), rs.getString("Bio"));
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return res;
// }

// public int deleteMessage(String UID) {
// int res = -1;
// try {
// mDeleteOne.setString(1, UID);
// res = mDeleteOne.executeUpdate(); // Execute update differs from execute
// query
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return res;
// }

// int updateMessage(String UID, String Bio) {
// int res = -1;
// try {
// mUpdateOne.setString(1, UID);
// mUpdateOne.setString(2, Bio);
// res = mUpdateOne.executeUpdate();
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return res;
// }
// }
