package edu.lehigh.cse216.team09.exh226.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

public class App {

    
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create tblData");
        System.out.println("  [D] Drop tblData");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [I] Invalidate ");
        System.out.println("  [R] Remove least accessed ");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }
    static void typeMenu() {
        System.out.println("Select Type Menu");
        System.out.println("  [M] Message tbl");
        System.out.println("  [U] User tbl");
        System.out.println("  [L] Likes Table");
        System.out.println("  [C] Comments Table");
    }

    static char prompt(BufferedReader in) {
        String actions = "TD1*-+~qIR?";
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    static char prompt2(BufferedReader in) {
        String actions = "MULDC?";
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Type");
        }
    }

    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
    public static void main(String[] argv) {
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");

        Database db = Database.getDatabase(ip, port, user, pass);
        if (db == null)
            return;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        char action;
        while (true) {
            action = prompt(in);
            switch (action) {
                case '?':
                    menu();
                    break;
                 case 'q':
                     break;
                 case 'T':
                //     db.createTable();
                    char createTableOption = prompt2(in);
                    switch (createTableOption) {
                        case 'M':
                            // Create Message Table
                             db.createMessageTable();
                            break;
                        case 'U':
                            // Create User Table
                             db.createUserTable();
                            break;
                        case 'L':
                            db.createUserLikesTable();
                            break;
                        case 'D':
                            db.createUserDislikesTable();
                            break;
                        case 'C':
                            db.createCommentsTable();
                            break;
                        default:
                            System.out.println("Invalid Table Creation Option");
                    }
                 break;
                 case '*':
                    char queryAllOption = prompt2(in);
                    ArrayList<Database.RowData> res;
                    switch (queryAllOption) {
                        case 'M':
                            // Create Message Table
                            res = db.selectAllMessages();
                            if (res != null) {
                                System.out.println("  Current Messages");
                                System.out.println("  -------------------------");
                                for (Database.RowData rd : res) {
                                    System.out.println(String.format("UserID:%s MessageID:%d Title:%s Content:%s Like:%d Dislike:%d FileID:%s", rd.mUserID, rd.mMessageID, rd.mTitle, rd.mContent, rd.mLikeCount, rd.mDislikeCount, rd.mFileID));
                                }
                            }
                            break;
                        case 'U':
                            // Create User Table
                             res = db.selectAllUsers();
                             if (res != null) {
                                System.out.println("  Current Users");
                                System.out.println("  -------------------------");
                                for (Database.RowData rd : res) {
                                    System.out.println(String.format("UserID:%s Username:%s Email:%s Bio:%s", rd.mUserID, rd.mUsername, rd.mEmail, rd.mBio));
                                }
                            }
                            break;
                        case 'L':
                            res = db.selectAllUserLikes();
                            if (res != null) {
                                System.out.println("  Current Likes");
                                System.out.println("  -------------------------");
                                for (Database.RowData rd : res) {
                                    System.out.println(String.format("UserID:%s MessageID:%d ", rd.mUserID, rd.mMessageID));
                                }
                            }
                        break;
                        case 'D':
                            res = db.selectAllUserDislikes();
                            if (res != null) {
                                System.out.println("  Current Dislikes");
                                System.out.println("  -------------------------");
                                for (Database.RowData rd : res) {
                                    System.out.println(String.format("UserID:%s MessageID:%d ", rd.mUserID, rd.mMessageID));
                                }
                            }
                        break;
                        case 'C':
                            res = db.selectAllComments();
                            if (res != null) {
                                System.out.println("  Current Comments");
                                System.out.println("  -------------------------");
                                for (Database.RowData rd : res) {
                                    System.out.println(String.format("CommentID:%d MessageID:%d UserId:%s Content:%s ", rd.mCommentID, rd.mMessageID, rd.mUserID, rd.mContent));
                                }
                            }
                        break;
                        default:
                            System.out.println("Invalid Table View All Option");
                    }
                    
                 break;
                 case 'D':
                    char dropOption = prompt2(in);
                    switch (dropOption) {
                        case 'M':
                            // Create Message Table
                            db.dropMessageTable();
                            break;
                        case 'U':
                            // Create User Table
                            db.dropUserTable();
                            break;
                        case 'L':
                            db.dropUserLikesTable();
                            break;
                        case 'D':
                            db.dropUserDislikesTable();
                            break;
                        case 'C':
                            db.dropCommentsTable();
                            break;
                        default:
                            System.out.println("Invalid Table Drop Option");
                    }
                 break;
                 case '1':
                 char selectOneOption = prompt2(in);
                 Database.RowData rd;
                 switch (selectOneOption) {
                    
                     case 'M':
                         // Create Message Table
                         int messageID = getInt(in, "Enter the message ID");
                         if(messageID != -1){
                         rd = db.selectOneMessage(messageID);
                         if (rd != null) {
                             System.out.println("  1 Message");
                             System.out.println(String.format("UserID:%s MessageID:%d Title:%s Content:%s Like:%d Dislike:%d FileID:%s", rd.mUserID, rd.mMessageID, rd.mTitle, rd.mContent, rd.mLikeCount, rd.mDislikeCount, rd.mFileID));
                            }
                        }
                         break;
                     case 'U':
                         // Create User Table
                         String userID = getString(in, "Enter userId");
                         if(userID != null){
                            rd = db.selectOneUser(userID);
                            if (rd != null) {
                                System.out.println(" User Row Data");
                                System.out.println(String.format("UserID:%s Username:%s Email:%s Bio:%s", rd.mUserID, rd.mUsername, rd.mEmail, rd.mBio));
                            }
                           }
                         break;
                     case 'L':
                     int LikeID = getInt(in, "Enter the message ID");
                     if(LikeID != -1){
                        rd = db.selectOneUserLike(LikeID);
                        if (rd != null) {
                            System.out.println("  UserLike Row Data");
                            System.out.println(String.format("UserID:%s MessageID:%d ", rd.mUserID, rd.mMessageID));
                        }
                        }
                     break;
                     case 'D':
                        int dislikeID = getInt(in, "Enter the message ID");
                        if(dislikeID != -1){
                            rd = db.selectOneUserDislike(dislikeID);
                            if (rd != null) {
                                System.out.println("  UserDislike Row Data");
                                System.out.println(String.format("UserID:%s MessageID:%d ", rd.mUserID, rd.mMessageID));
                            }
                            }
                        break;
                     case 'C':
                        String commentID = getString(in, "Enter commentID");
                        if(commentID != null){
                            rd = db.selectOneComment(commentID);
                            if (rd != null) {
                                System.out.println(" Comment Row Data");
                                System.out.println(rd.toString());
                            }
                        }
                     break;
                     default:
                         System.out.println("Invalid Table SelectOne Option");
                 }
                 break;
                 case '-':
                    char deleteOption = prompt2(in);
                    int rowDeleted;
                    switch (deleteOption) {
                        case 'M':
                            // Create Message Table
                            int deleteMessageID = getInt(in, "Enter the message ID");
                            if(deleteMessageID != -1){
                            rowDeleted = db.deleteMessage(deleteMessageID);
                            if (rowDeleted != -1) {
                                System.out.println("  " + rowDeleted + " rows deleted in messageTbl");
                            }
                            }
                            break;
                        case 'U':
                            // Create User Table
                            String userID = getString(in, "Enter userId");
                            if(userID != null){
                                rowDeleted = db.deleteUser(userID);
                                if (rowDeleted != -1) {
                                    System.out.println("  " + rowDeleted + " rows deleted in userTbl");
                                }
                            }
                            break;
                        case 'L':
                        int LikeID = getInt(in, "Enter the message ID");
                        if(LikeID != -1){
                            rowDeleted = db.deleteUserLike(LikeID);
                            if (rowDeleted != -1) {
                                System.out.println("  " + rowDeleted + " rows deleted in userLikeTbl");
                            }
                            }
                        break;
                        case 'D':
                            int DislikeID = getInt(in, "Enter the message ID");
                            if(DislikeID != -1){
                                rowDeleted = db.deleteUserDislike(DislikeID);
                                if (rowDeleted != -1) {
                                    System.out.println("  " + rowDeleted + " rows deleted in userLikeTbl");
                                }
                                }
                        break;
                        case 'C':
                            String commentID = getString(in, "Enter commentID");
                            if(commentID != null){
                                rowDeleted = db.deleteComment(commentID);
                                if (rowDeleted != -1) {
                                    System.out.println("  " + rowDeleted + " rows deleted in commentTbl");
                                }
                            }
                        break;
                        default:
                            System.out.println("Invalid Table Delete Option");
                    }

                 break;
                 case '+':
                 char addOption = prompt2(in);
                 int rowAdded;
                 String userID;
                 String username;
                 int messageID;
                 switch (addOption) {
                     case 'M':
                         // Create Message Table
                         userID = getString(in, "Enter the userID");
                         messageID = getInt(in, "Enter messageID");
                         String title = getString(in, "Enter the title");
                         String content = getString(in, "Enter the message");
                         int like = getInt(in, "Enter likeCount");
                         int dislikeCount = getInt(in, "Enter DislikeCount");
                         String fileID = getString(in, "Enter the fileID");
                         
                         rowAdded = db.insertMessage(userID, messageID, title, content, like, dislikeCount, fileID);
                         if (rowAdded != -1) {
                             System.out.println("  " + rowAdded + " rows added in messageTbl");
                         }
                         
                         break;
                     case 'U':
                         // Create User Table
                         userID = getString(in, "Enter userId");
                         username = getString(in, "Enter username");
                         String email = getString(in, "Enter email");
                         String bio = getString(in, "Enter user bio");

                         if(userID != null){
                             rowAdded = db.insertUser(userID, username, email, bio);
                             if (rowAdded != -1) {
                                 System.out.println("  " + rowAdded + " rows added in userTbl");
                             }
                         }
                         break;
                     case 'L':
                     username = getString(in, "Enter username");
                     messageID = getInt(in, "Enter the message ID");
                     if(messageID != -1){
                         rowAdded = db.insertUserLike(username, messageID);
                         if (rowAdded != -1) {
                             System.out.println("  " + rowAdded + " rows added in userLikeTbl");
                         }
                         }
                     break;
                     case 'D':
                        username = getString(in, "Enter username");
                        messageID = getInt(in, "Enter the message ID");
                        if(messageID != -1){
                            rowAdded = db.insertUserDislike(username, messageID);
                            if (rowAdded != -1) {
                                System.out.println("  " + rowAdded + " rows deleted in userLikeTbl");
                            }
                            }
                     break;
                     case 'C':
                         int commentID = getInt(in, "Enter commentID");
                         userID = getString(in, "Enter userId");
                         messageID = getInt(in, "Enter the message ID");
                         String commentContent =  getString(in, "Enter comment content");
                         if(commentID != -1){
                             rowAdded = db.insertComment(commentID, messageID, userID, commentContent);
                             if (rowAdded != -1) {
                                 System.out.println("  " + rowAdded + " rows added in commentTbl");
                             }
                         }
                     break;
                     default:
                         System.out.println("Invalid Table add Option");
                 }
                 break;   
                 case '~':
                 char updateOption = prompt2(in);
                 int rowUpdated;
                  switch (updateOption) {
                     case 'M':
                         // Create Message Table
                         messageID = getInt(in, "Enter messageID");
                         String title = getString(in, "Enter the title");
                         String content = getString(in, "Enter the message");
                         int like = getInt(in, "Enter likeCount");
                         int dislikeCount = getInt(in, "Enter DislikeCount");
                         String fileID = getString(in, "Enter the fileID");
                         rowUpdated = db.updateMessage(messageID, title, content, like, dislikeCount, fileID);
                         if (rowUpdated != -1) {
                             System.out.println("  " + rowUpdated + " rows updated in messageTbl");
                         }
                         
                         break;
                     case 'U':
                         // Create User Table
                         userID = getString(in, "Enter userId");
                         username = getString(in, "Enter username");
                         String email = getString(in, "Enter email");
                         String bio = getString(in, "Enter user bio");

                         if(userID != null){
                             rowUpdated = db.updateUser(userID, username, email, bio);
                             if (rowUpdated != -1) {
                                 System.out.println("  " + rowUpdated + " rows updated in userTbl");
                             }
                         }
                         break;
                     case 'L':
                        username = getString(in, "Enter username");
                        messageID = getInt(in, "Enter the message ID");
                        //  if(messageID != -1){
                        //      rowUpdated = db.updateUserLike;
                        //      if (rowUpdated != -1) {
                        //          System.out.println("  " + rowUpdated + " rows updated in userLikeTbl");
                        //      }
                        //      }
                     break;
                     case 'D':
                     username = getString(in, "Enter username");
                     messageID = getInt(in, "Enter the message ID");
                        //  if(messageID != -1){
                        //      rowUpdated = db.updateUserDislike;
                        //      if (rowUpdated != -1) {
                        //          System.out.println("  " + rowUpdated + " rows updated in userLikeTbl");
                        //      }
                        //      }
                     break;
                     case 'C':
                         String commentID = getString(in, "Enter commentID");
                         String commentContent =  getString(in, "Enter comment content");
                         String file = getString(in, "Enter file name");
                         if(commentID != null){
                             rowUpdated = db.updateComment(commentID, commentContent, file);
                             if (rowUpdated != -1) {
                                 System.out.println("  " + rowUpdated + " rows updated in commentTbl");
                             }
                         }
                     break;
                     default:
                         System.out.println("Invalid Table add Option");
                 }
                 
                 break;
                
                case 'I':
                //Need to add column in backend
                char invalidOption = prompt2(in);
                switch(invalidOption){
                    case 'M':
                        int ideaID = getInt(in, "Enter the message ID to invalidate");
                        if (ideaID != -1) {
                            if (db.invalidateIdea(ideaID)) {
                                System.out.println("Idea invalidated successfully.");
                            } else {
                                System.out.println("Failed to invalidate idea.");
                            }
                        }
                    break;
                    case 'C':
                        int commendID = getInt(in, "Enter the message ID to invalidate");
                        if (commendID != -1) {
                            if (db.invalidateIdea(commendID)) {
                                System.out.println("Comment invalidated successfully.");
                            } else {
                                System.out.println("Failed to invalidate Comment.");
                            }
                        }
                    break;
                    default : System.out.println("No invalidate option for this table or invalid table option");

                } 
                break;
                case 'R':
                    db.removeLeastRecentlyAccessedFile();
                break;
                default:
                    System.out.println("Invalid Command");
            }
            
            if (action == 'q') {
                break;
            }
        }
        db.disconnect();
    }
}

//     break;
                // case 'D':
                //     db.dropTable();
                //     break;
                // case '1':
                //     int id = getInt(in, "Enter the row ID");
                //     if (id != -1) {
                //         Database.RowData res = db.selectOne(id);
                //         if (res != null) {
                //             System.out.println("  [" + res.mId + "] " + res.mSubject);
                //             System.out.println("  --> " + res.mMessage);
                //         }
                //     }
                //     break;
                // case '*':
                //     ArrayList<Database.RowData> res = db.selectAll();
                //     if (res != null) {
                //         System.out.println("  Current Database Contents");
                //         System.out.println("  -------------------------");
                //         for (Database.RowData rd : res) {
                //             System.out.println("  [" + rd.mId + "] " + rd.mSubject);
                //         }
                //     }
                //     break;
                // case '-':
                //     id = getInt(in, "Enter the row ID");
                //     if (id != -1) {
                //         int rowsDeleted = db.deleteRow(id);
                //         if (rowsDeleted != -1) {
                //             System.out.println("  " + rowsDeleted + " rows deleted");
                //         }
                //     }
                //     break;
                // case '+':
                //     String subject = getString(in, "Enter the subject");
                //     String message = getString(in, "Enter the message");
                //     if (!subject.equals("") && !message.equals("")) {
                //         int rowsAdded = db.insertRow(subject, message);
                //         System.out.println(rowsAdded + " rows added");
                //     }
                //     break;
                // case '~':
                //     id = getInt(in, "Enter the row ID");
                //     if (id != -1) {
                //         String newMessage = getString(in, "Enter the new message");
                //         int rowsUpdated = db.updateOne(id, newMessage);
                //         if (rowsUpdated != -1) {
                //             System.out.println("  " + rowsUpdated + " rows updated");
                //         }
                //     }
                //     break;