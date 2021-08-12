/**
 * @author Nikita Goncharov
 * Teacher Authentication class that handles the unique menu of the teacher, allowing all permissions
 */

import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class TeacherAuth {

    Database db;

    /**
     * Logs the teacher in and opens the menu, also handles the menu functions by
     * sending them to the database or functions in the current class
     * @param db the database in use
     */
    public TeacherAuth(Database db) {
        this.db = db;

        System.out.println("Logged in as a Teacher");
        System.out.println();

        boolean running = true;
        Scanner input = new Scanner(System.in);
        String answer = "";

        while (running) {
            System.out.println("Menu: ");
            System.out.println("(S) - View specific record");
            System.out.println("(V) - View all records");
            System.out.println("(E) - Edit specific record");
            System.out.println("(A) - Add record");
            System.out.println("(R) - Remove record");
            System.out.println("(P) - Tally house points");
            System.out.println("(X) - Exit");
            System.out.print("\u27A5");

            try {
                answer = input.next().toLowerCase();
                input.nextLine();
            }
            catch (Exception e) {
                System.out.println("Incorrect input, please try again");
                continue;
            }
            switch (answer) {
                case "s":
                    accessRecord();
                    break;
                case "v":
                    db.outputAllRecords();
                    break;
                case "e":
                    editRecord();
                    break;
                case "a":
                    addRecord();
                    break;
                case "r":
                    removeRecord();
                    break;
                case "p":
                    db.tallyAllHouses();
                    break;
                case "x":
                    db.shutdownDatabase();
                    break;
                default:
                    System.out.println("input: " + answer);
                    System.out.println("Incorrect input, please try again");
                    continue;
            }
        }



    }

    /**
     * A function to access a single record, with the first name and last name inputted in the function
     */
    void accessRecord () {
        String firstName = "";
        String lastName = "";
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while(running) {

            System.out.print("Enter student name: ");

            try {
                firstName = input.next().toLowerCase();
                lastName = input.next().toLowerCase();
                input.nextLine();

            }
            catch (Exception e) {
                System.out.println("Incorrect input, please try again");
                continue;
            }

            db.outputRecord(firstName, lastName);
            running = false;
        }

    }

    /**
     * A function for editing a record in the database.
     * Provides a full menu for each database input and sends selected actions to the database.
     */
    void editRecord () {
        String firstName = "";
        String lastName = "";
        Scanner input = new Scanner(System.in);
        boolean running = true;
        boolean running2 = true;
        String answer = "";

        while(running) {

            System.out.println("Please enter the name of the student to edit: ");

            try {
                firstName = input.next().toLowerCase();
                lastName = input.next().toLowerCase();
                input.nextLine();

                if(!db.studentExists(firstName, lastName)) throw new Exception("name not found");

            }
            catch (Exception e) {
                System.out.println("Incorrect input or non-existent name, please try again");
                continue;
            }

            running = false;

            while (running2) {
                System.out.println("What do you want to edit: ");
                System.out.println("(I) - ID");
                System.out.println("(N) - Name");
                System.out.println("(G) - Grade");
                System.out.println("(A) - Average");
                System.out.println("(H) - House");
                System.out.println("(P) - House Points Scored");
                System.out.println("(X) - Exit");
                System.out.print("\u27A5");

                try {
                    answer = input.next().toLowerCase();
                    input.nextLine();
                }
                catch (Exception e) {
                    System.out.println("Incorrect input, please try again");
                    continue;
                }
                switch (answer) {
                    case "i":
                        db.editID(firstName, lastName);
                        break;
                    case "n":
                        db.editName(firstName, lastName);
                        break;
                    case "g":
                        db.editGrade(firstName, lastName);
                        break;
                    case "a":
                        db.editOverallAverage(firstName, lastName);
                        break;
                    case "h":
                        db.editSchoolHouse(firstName, lastName);
                        break;
                    case "p":
                        db.editHousePoints(firstName, lastName);
                        break;
                    case "x":
                        running2 = false;
                        break;
                    default:
                        System.out.println("Incorrect input, please try again");
                        continue;
                }
                System.out.println("Done. ");
            }
        }


    }

    /**
     * Removes a record from the database, with the first name and last name inputted in the function
     */
    void removeRecord () {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        String firstName = "";
        String lastName = "";


        while (running) {
            System.out.print("Please enter the name of the student to remove: ");

            try {
                firstName = input.next().toLowerCase();
                lastName = input.next().toLowerCase();
                input.nextLine();
                if(!db.studentExists(firstName, lastName)) throw new Exception("name not found");

            } catch (Exception e) {
                System.out.println("Incorrect input or non-existent, please try again");
                continue;
            }

            db.removeStudent(firstName, lastName);

            System.out.println("Done. ");

            running = false;
        }
    }

    /**
     * Adds a record to the database.
     * Provides a description of how that is done and the format in which to input.
     */
    void addRecord () {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        String newStudent = "";


        while (running) {
            System.out.println("Enter the record in the format \"FirstName LastName House Grade ID OverallAverage HousePointsEarned\" or "+ (char) 27 + "[4mexit" + (char) 27 + "[0m.");
            System.out.println("Restrictions: ");
            System.out.println("  FirstName, LastName - only letters");
            System.out.println("  House - A, C, W, N");
            System.out.println("  Grade - number from 9 to 12");
            System.out.println("  ID - number from 1000 to 9999");
            System.out.println("  OverallAverage - decimal number from 0.0 to 100.0");
            System.out.println("  HousePointsEarned - positive number");

            try {

                newStudent = input.nextLine();

            }
            catch (Exception e) {
                System.out.println("Incorrect input, please try again");
                continue;
            }
            if (newStudent.equalsIgnoreCase("exit")) return;

            List<String> splitStudent = Arrays.asList(newStudent.split(" "));

            try {
                db.students.add(new Student(splitStudent));
            }
            catch (Exception e) {
                System.out.println("Incorrect input, please try again. ");
                continue;

            }

            System.out.println("Done.");
            running = false;
        }



    }
}
