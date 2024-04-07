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
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    static char prompt(BufferedReader in) {
        String actions = "TD1*-+~q?";
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
                    db.createTable();
                    
                    break;
                case 'D':
                    db.dropTable();
                    break;
                case '1':
                    int id = getInt(in, "Enter the row ID");
                    if (id != -1) {
                        Database.RowData res = db.selectOne(id);
                        if (res != null) {
                            System.out.println("  [" + res.mId + "] " + res.mSubject);
                            System.out.println("  --> " + res.mMessage);
                        }
                    }
                    break;
                case '*':
                    ArrayList<Database.RowData> res = db.selectAll();
                    if (res != null) {
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (Database.RowData rd : res) {
                            System.out.println("  [" + rd.mId + "] " + rd.mSubject);
                        }
                    }
                    break;
                case '-':
                    id = getInt(in, "Enter the row ID");
                    if (id != -1) {
                        int rowsDeleted = db.deleteRow(id);
                        if (rowsDeleted != -1) {
                            System.out.println("  " + rowsDeleted + " rows deleted");
                        }
                    }
                    break;
                case '+':
                    String subject = getString(in, "Enter the subject");
                    String message = getString(in, "Enter the message");
                    if (!subject.equals("") && !message.equals("")) {
                        int rowsAdded = db.insertRow(subject, message);
                        System.out.println(rowsAdded + " rows added");
                    }
                    break;
                case '~':
                    id = getInt(in, "Enter the row ID");
                    if (id != -1) {
                        String newMessage = getString(in, "Enter the new message");
                        int rowsUpdated = db.updateOne(id, newMessage);
                        if (rowsUpdated != -1) {
                            System.out.println("  " + rowsUpdated + " rows updated");
                        }
                    }
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
