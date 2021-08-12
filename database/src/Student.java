/**
 * @author Nikita Goncharov
 * Student class of the record that is stored in the database
 */

import java.util.List;

public class Student {
    public String firstName;
    public String lastName;
    public char schoolHouse;
    public int grade;
    protected int ID;
    protected double overallAverage;
    public int housePointsScored;

    public final int dataCount = 7;

    /**
     *
     * @param data is a list of Strings that are the elements in each database line in the format
     *             FirstName LastName House Grade ID OverallAverage HousePointsEarned
     * @throws Exception if the program isn't able to interpret the list based on the requirements
     */
    public Student (List<String> data) throws Exception{
        if(data.size() != dataCount) {
            System.out.println("Error in database, please make sure there are 7 elements in each database line ");

            throw new Exception("Error in database");
        }

        try {
            this.firstName = data.get(0);
            this.lastName = data.get(1);

            String tempHouse = data.get(2).toLowerCase();
            if(tempHouse.length() != 1) throw new Exception("house entered incorrectly");
            else if(tempHouse.charAt(0) != 'a' && tempHouse.charAt(0) != 'c' && tempHouse.charAt(0) != 'w' && tempHouse.charAt(0) != 'n')
                throw new Exception("non-existent house");
            this.schoolHouse = data.get(2).charAt(0);

            this.grade = Integer.parseInt(data.get(3));
            this.ID = Integer.parseInt(data.get(4));

            this.overallAverage = Double.parseDouble(data.get(5));

            this.housePointsScored = Integer.parseInt(data.get(6));

        }
        catch (Exception e) {
            System.out.println("Error in one or more elements in the database, please ensure the correct input order");
            throw new Exception("Error in database");
        }

    }

}
