/**
 * @author Nikita Goncharov
 * Database class that handles accessing and modifying the database
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Database {
    File db = new File("database.txt");
    Scanner fileInput;
    FileWriter fw;
    ArrayList<Student> students = new ArrayList<Student>();


    /**
     * Creates an ArrayList of students based on the database.txt file lines
     * @throws FileNotFoundException if the database.txt file is not located
     */
    public Database () throws FileNotFoundException {
        fileInput = new Scanner(db);
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get("database.txt"));
        }
        catch (Exception e) {
            //System.out.println(Paths.get("database.txt"));
            System.out.println("database.txt does not exist");
            System.out.println("Exiting...");
            System.exit(0);
        }
        //splitting current line into fragments
        List<String> currentLinesFragments;
        for (int i = 0; i < lines.size(); i++) {
            //System.out.println("Current Line: " + lines.get(i));
            currentLinesFragments = Arrays.asList(lines.get(i).split(" "));

            try {
                students.add(new Student(currentLinesFragments));
            }
            catch (Exception e) {
                System.out.println("Exiting...");
                System.exit(0);
            }


        }
    }

    /**
     * Shuts down the database, saving the ArrayList of students into the text file
     */
    public void shutdownDatabase () {

        try {
            fw = new FileWriter(db);

            for (Student student : students) {
                fw.write(student.firstName + " " + student.lastName + " " + student.schoolHouse + " " +
                        student.grade + " " + student.ID + " " + student.overallAverage + " " + student.housePointsScored + "\n");
            }
            fw.flush();
            fw.close();
        }
        catch (Exception e) {
            System.exit(0);
        }



        System.exit(0);
    }

    /**
     * Checks if thes student provided exists in the database
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     * @return boolean of true or false based on if the student exists in the databse
     */
    public boolean studentExists (String firstName, String lastName) {
        for (Student student : students)  {
            if ((student.firstName.equalsIgnoreCase(firstName)) && (student.lastName.equalsIgnoreCase(lastName))) return true;
        }
        return false;
    }

    /**
     * Outputs a single record of the database of the student provided
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void outputRecord (String firstName, String lastName) {
        for (Student student : students)  {
            if ((student.firstName.equalsIgnoreCase(firstName)) && (student.lastName.equalsIgnoreCase(lastName)))  {
                System.out.format("%-4s|%-35s|%-2s|%-4s|%-1s|%-5s", "ID", "Name", "Grade", "Average", "House", "House Points Scored");
                System.out.println();
                System.out.format("%-4s|%-35s|%-5s|%-7s|%-5s|%-19s", student.ID, student.firstName + " " + student.lastName,
                        student.grade, student.overallAverage, student.schoolHouse, student.housePointsScored);
                System.out.println();
            }
        }
    }

    /**
     * Outputs all records from the database
     */
    public void outputAllRecords () {
        System.out.println();
        System.out.format("%-4s|%-35s|%-2s|%-4s|%-1s|%-5s", "ID", "Name", "Grade", "Average", "House", "House Points Scored");
        for (Student student : students)  {
            System.out.println();
                System.out.format("%-4s|%-35s|%-5s|%-7s|%-5s|%-19s", student.ID, student.firstName + " " + student.lastName,
                        student.grade, student.overallAverage, student.schoolHouse, student.housePointsScored);
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Tally points the house of the student provided.
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void tallyPoints (String firstName, String lastName) {
        int sum = 0;

        char house = 0;

        for (Student student : students)  {
            if ((student.firstName.equalsIgnoreCase(firstName)) && (student.lastName.equalsIgnoreCase(lastName))) house = student.schoolHouse;
        }
        for (Student student : students) {
            if(student.schoolHouse == house) {
                sum+= student.housePointsScored;
            }
        }

        System.out.println(Character.toString(house).toUpperCase() + " has scored " + sum + " points.");

    }

    /**
     * Tally points of all houses and sort by amount
     */
    public void tallyAllHouses () {

        HashMap<String, Integer> tally = new HashMap<>();
        tally.put("A", 0);
        tally.put("C", 0);
        tally.put("W", 0);
        tally.put("N", 0);

        for (Student student : students) {
            switch (Character.toLowerCase(student.schoolHouse)) {
                case 'a':
                    tally.put("A", tally.get("A") + student.housePointsScored);
                    break;
                case 'c':
                    tally.put("C", tally.get("C") + student.housePointsScored);
                    break;
                case 'w':
                    tally.put("W", tally.get("W") + student.housePointsScored);
                    break;
                case 'n':
                    tally.put("N", tally.get("N") + student.housePointsScored);
                    break;
                default:
                    //nothing should be able to get here
                    break;
            }
        }
        ValueComparator comparePoints = new ValueComparator(tally);
        TreeMap<String, Integer> sortedTally = new TreeMap<>(comparePoints);
        sortedTally.putAll(tally);

        int i = 1;
        System.out.println("School houses in order of most points");
        for (Map.Entry<String, Integer> currentHouse : sortedTally.entrySet()) {
            System.out.println(i + ") " + currentHouse.getKey() + " house, with " + currentHouse.getValue() + " points");
            i++;
        }



    }

    /**
     * A custom comparator class that allows the sorting of the TreeMap by the value, in this case the house points
     */
    class ValueComparator implements Comparator<String> {
        Map<String, Integer> tally;

        public ValueComparator(Map<String, Integer> tally) {
            this.tally = tally;
        }
        public int compare(String s1, String s2) {
            if (tally.get(s1) > tally.get(s2)) return -1;
            else if (tally.get(s1) < tally.get(s2)) return 1;
            else {
                // if the tallies are equal then just return whichever one.
                return 1;
            }
        }
    }

    /**
     *
     * Used to get the student with a specific name
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     * @return the Student with the selected first and last name, and null if the student doesent exist
     */
    private Student studentWithName(String firstName, String lastName) {
        for (Student student : students)  {
            if ((student.firstName.equalsIgnoreCase(firstName)) && (student.lastName.equalsIgnoreCase(lastName))) {
                return student;
            }
        }
        return null;
    }

    /**
     * Edits the ID of the provided student
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void editID (String firstName, String lastName) {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        int newID = -1;

        while (running) {
            System.out.print("Input new ID: ");

            try {
                newID = Integer.parseInt(input.nextLine());
                if(String.valueOf(newID).length() != 4 || newID < 0) throw new Exception("wrong input");
            }
            catch (Exception e) {
                System.out.println("Incorrect input: ID must be a positive 4 digit number, please try again");
                continue;
            }

            studentWithName(firstName, lastName).ID = newID;
            return;
        }

    }

    /**
     * Edits and returns the name of the student
     * @param firstName is the original first name of the student
     * @param lastName is the original last name of the student
     * @return the full new new name of the student
     */
    String editName (String firstName, String lastName) {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        String newFirstName = "";
        String newLastName = "";


        while (running) {
            System.out.print("Please enter your modified name: ");

            try {
                newFirstName = input.next().toLowerCase();
                newLastName = input.next().toLowerCase();
                input.nextLine();

            }
            catch (Exception e) {
                System.out.println("Incorrect input, please try again");
                continue;
            }

            newFirstName = newFirstName.substring(0, 1).toUpperCase() + newFirstName.substring(1);
            newLastName = newLastName.substring(0, 1).toUpperCase() + newLastName.substring(1);

            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

            for (Student student : students)  {
                if ((student.firstName.equalsIgnoreCase(firstName)) && (student.lastName.equalsIgnoreCase(lastName)))  {
                    student.firstName = newFirstName;
                    student.lastName = newLastName;

                    System.out.format("%s %s changed to %s %s\n", firstName, lastName, newFirstName, newLastName);
                }
            }


            running = false;
        }
        return (newFirstName + " " + newLastName);
    }

    /**
     * Edits the grade of the student provided
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void editGrade (String firstName, String lastName) {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        int newGrade = -1;

        while (running) {
            System.out.print("Input new Grade: ");

            try {
                newGrade = Integer.parseInt(input.nextLine());

                if(newGrade >= 12 && newGrade <= 4) throw new Exception("wrong input");
            }
            catch (Exception e) {
                System.out.println("Incorrect input: Grade must be a number between 4 and 12, please try again");
                continue;
            }

            studentWithName(firstName, lastName).grade = newGrade;
            return;
        }

    }

    /**
     * Edits the overall average of the student provided
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void editOverallAverage (String firstName, String lastName) {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        double newAverage = -1.0;

        while (running) {
            System.out.print("Input new Overall Average: ");

            try {
                newAverage = Double.parseDouble(input.nextLine());

                if(newAverage >= 100.0 && newAverage <= 0.0) throw new Exception("wrong input");
            }
            catch (Exception e) {
                System.out.println("Incorrect input: Overall Average must be a number between 0.0 and 100.0, please try again");
                continue;
            }

            studentWithName(firstName, lastName).overallAverage = newAverage;
            return;
        }

    }

    /**
     * Edits the school house of the student provided
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void editSchoolHouse (String firstName, String lastName) {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        char newHouse = 0;
        String answer = "";

        while (running) {
            System.out.print("Input new House: ");

            try {
                answer = input.next().toLowerCase();
                input.nextLine();

                newHouse = answer.toLowerCase().charAt(0);

                if(answer.length() > 1)throw new Exception("wrong input");

                if(newHouse != 'a' && newHouse != 'c' && newHouse != 'n' && newHouse != 'w') throw new Exception("wrong input");
            }
            catch (Exception e) {
                System.out.println("Incorrect input: House must be either A, C, W or N, please try again");
                continue;
            }

            studentWithName(firstName, lastName).schoolHouse = Character.toUpperCase(newHouse);
            return;
        }

    }
    /**
     * Edits the house points scored of the student provided
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void editHousePoints (String firstName, String lastName) {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        int newHousePoints = -1;

        while (running) {
            System.out.print("Input new House Points: ");

            try {
                newHousePoints = Integer.parseInt(input.nextLine());
                if(newHousePoints < 0) throw new Exception("negative house points");
            }
            catch (Exception e) {
                System.out.println("Incorrect input: House Points must be a positive number, please try again");
                continue;
            }

            studentWithName(firstName, lastName).housePointsScored = newHousePoints;
            return;
        }

    }
    /**
     * Removes the student provided from the database
     * @param firstName is the first name of the student
     * @param lastName is the last name of the student
     */
    public void removeStudent (String firstName, String lastName) {
        students.remove(studentWithName(firstName,lastName));
    }

}


