/**
 * @author Nikita Goncharov
 * Student Authentication class that handles the unique menu of the student, only allowing certain permissions
 */

import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class StudentAuth {

    public String sessionFirstName = "";
    public String sessionLastName = "";
    Database db;

    /**
     * Logs the student in based on the name they provide and opens the menu.
     * @param db the database in use
     */
    public StudentAuth(Database db) {
        this.db = db;
        login();
        openMenu();
    }

    /**
     * Logs in the student and saves their login into the session
     * @postconditions sessionFirstName and sessionLastName are set to the login of the student.
     */
    void login () {
        boolean running = true;
        String authFirstName = "";
        String authLastName = "";
        Scanner input = new Scanner(System.in);

        while(running) {
            System.out.println("Please enter your " + (char)27 +"[4mfirst name" + (char)27 +"[0m followed by your " + (char)27 +"[4mlast name" + (char)27 +"[0m:");
            System.out.print("\u27A5");

            authFirstName = input.next().toLowerCase();
            authLastName = input.next().toLowerCase();
            input.nextLine();


            if (!db.studentExists(authFirstName, authLastName)) {
                System.out.println("Non-existent name or incorrect input, please try again");
            }
            else {
                sessionFirstName = authFirstName;
                sessionLastName = authLastName;
                running = false;
            }
        }

    }

    /**
     * Opens the menu with student permissions, and manages the actions by sending them to the database
     */
    void openMenu () {

        boolean running = true;
        Scanner input = new Scanner(System.in);
        String answer;
        while (running) {

            System.out.println("Menu: ");
            System.out.println(" (V) - View my record");
            System.out.println(" (H) - View my houses' points tally");
            System.out.println(" (E) - Edit my name");
            System.out.println(" (X) - Exit");
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
                case "v":
                    db.outputRecord(sessionFirstName, sessionLastName);
                    break;
                case "h":
                    db.tallyPoints(sessionFirstName, sessionLastName);
                    break;
                case "e":
                    editName(sessionFirstName, sessionLastName);
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
     * Edits the name of the student and saves into the session
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     * @postconditions sessionFirstName and sessionLastName are set to the new login of the student.
     */
    void editName (String firstName, String lastName) {
        String newName = db.editName(firstName, lastName);

        List<String> splitNames = Arrays.asList(newName.split(" "));

        sessionFirstName = splitNames.get(0);
        sessionLastName = splitNames.get(1);

    }

}
