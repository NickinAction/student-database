/**
 * @author Nikita Goncharov
 * The main class where the program begins and creates the necessary authentication interfaces for the login information provided
 */

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

class Main {
    static Scanner input;

    /**
     * Handles the first part of the login and creates the appropriate authentication menu
     * @param args standard java
     * @throws IOException if the file of the database is not found
     */
    public static void main(String[] args) throws IOException {
        input = new Scanner(System.in);
        Database db = new Database();
        String login;

        boolean running = true;

        while (running) {

            System.out.println("Ashbury Student Database");
            System.out.println("Please log in as a " + (char) 27 + "[4mstudent" + (char) 27 + "[0m or a " + (char) 27
                    + "[4mteacher" + (char) 27 + "[0m or " + (char) 27 + "[4mexit" + (char) 27 + "[0m: ");
            System.out.print("\u27A5");

            login = input.nextLine().toLowerCase();
            try {
                switch (login) {
                    case "student":
                        StudentAuth studentAuth = new StudentAuth(db);
                        break;
                    case "teacher":
                        TeacherAuth teacherAuth = new TeacherAuth(db);
                        break;
                    case "exit":
                        running = false;
                        System.out.println("Exiting...");
                        db.shutdownDatabase();

                    default:
                        System.out.println("Incorrect input, please try again.");
                }

            } catch (Exception e) {

            }
        }
    }
}