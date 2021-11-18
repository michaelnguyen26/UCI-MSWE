//@author 
//Michael Nguyen

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.HashMap;

//REFERENCES: https://www.tutorialspoint.com/jdbc/jdbc-create-database.htm
//            https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-statements.html#connector-j-examples-execute-select
//            https://www.mysqltutorial.org/mysql-jdbc-tutorial/
//            https://docs.oracle.com/javase/tutorial/jdbc/basics/tables.html

class Database{

    public void createDatabase(){
        Connection connection = null; // create a connection object to connect to mysql server

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=rootpassword"); // get connection to sql server
            
            // create a statement object to hold queries    
            Statement query = connection.createStatement();    

            // create database if it does not exist
            String createDatabase = "CREATE DATABASE IF NOT EXISTS student_course_schedule";     
           
           // create table for students if it does not exist
            String createStudentTable = 
            "CREATE TABLE IF NOT EXISTS student_course_schedule.students"+
            "(student_id INTEGER PRIMARY KEY AUTO_INCREMENT,"+
            "first_name VARCHAR(25) NOT NULL,"+
            "last_name VARCHAR (25) NOT NULL)"; 

            // create table for courses if it does not exist
            String createCourseTable = "CREATE TABLE IF NOT EXISTS student_course_schedule.courses"+
            "(course_id INTEGER PRIMARY KEY AUTO_INCREMENT,"+
            "course_name VARCHAR(20) NOT NULL UNIQUE,"+    // only unique courses (no duplicate courses)
            "course_day VARCHAR (20) NOT NULL,"+
            "course_time VARCHAR(15) NOT NUll)";   

            // create a linking table for the many:many relationship between students and courses if it does not exist
            String createLinkingTable = "CREATE TABLE IF NOT EXISTS student_course_schedule.link_student_course"+
            "(student_id INT NOT NULL,"+ 
            "course_id INT NOT NULL,"+  
            "FOREIGN KEY (student_id) REFERENCES students (student_id),"+ // foreign key for student_id
            "FOREIGN KEY (course_id) REFERENCES courses (course_id))";    // foreign key for course_id

            query.executeUpdate(createDatabase); // statement object executes query to create database
            query.executeUpdate(createStudentTable); // statement object executes query to create student table
            query.executeUpdate(createCourseTable); // statement object executes query to create course table
            query.executeUpdate(createLinkingTable); // statement object executes query to create linking table 
            
            System.out.println("\nDatabase Successfully Built!");
       
        }catch (SQLException ex) {    
            System.out.println();  
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}

public class StudentDatabase {
    public static void main(String[] args) {
        
        ResultSet resultSet = null; // create a resultSet object to hold the results from queries 
        Scanner input = new Scanner(System.in);
        HashMap<Integer, String> courseMap = new HashMap<>();  // hash map to hold id for auto-gen columns and the time

        // create database object
        Database database = new Database();
        database.createDatabase();

        try{
            
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=rootpassword");
            PreparedStatement queryStatement;
            Statement selectStatement;
            int response;

            while(true){
                System.out.println("\n");
                System.out.println("--------------- Menu ---------------\n");
                System.out.println("1) Enroll New Students into the Program");
                System.out.println("2) Insert New Courses");
                System.out.println("3) Enroll Students into Courses");
                System.out.println("4) View Which Students Are in Each Course");
                System.out.println("5) View Which Courses Each Student is Enrolled In");
                System.out.println("6) View Which Courses and What Times Each Course is for a Given Student on a Given Day of the Week");
                System.out.println("7) Display Database Information for All Courses and Students");
                System.out.println("8) Erase Database");
                System.out.println("9) Exit");
                
                System.out.print("\nSelection: ");
                
                try{
                    response = Integer.valueOf(input.nextLine());
                }
                catch(NumberFormatException e){
                    System.out.println("\nError: Enter a Valid Selection (1 - 9)");
                    continue;
                }

                if(response == 1){
                   
                    String fullName = "";
                    System.out.println("\nType \"x\" when finished and enter each name as (First, Last)\n");

                    while(true){
                        System.out.print("Enroll New Student(s): ");  
                        fullName = input.nextLine();

                        if(fullName.equalsIgnoreCase("x")){     // when user is finished stop processing   
                            break;
                        }

                        String[] splitName = fullName.split(",");

                        String studentToDatabase = 
                        "INSERT INTO student_course_schedule.students (first_name, last_name) "+ // student_id is auto incremented so it is not needed here
                        "VALUES (?, ?)"; // use a prepared statement with ? that adds the value specified later
                        
                        try{

                            queryStatement = connection.prepareStatement(studentToDatabase); //prepareStatement can execute with parameters and prevents SQL injection
                            queryStatement.setString(1, splitName[0]);     // split for first name and set in the 1st position indicated in the INSERT statement
                            queryStatement.setString(2, splitName[1]);   // split for last name and set in the 2nd position indicated in the INSERT statement
                            queryStatement.executeUpdate();     // execute the insert statement
                            System.out.printf("\n>>> Successfully Added %s%s", splitName[0], splitName[1] + " to the database!\n\n");
                        
                        }catch(SQLException e){
                            System.out.println("Error Adding Student to the Database - Try again.");
                            continue;
                        }catch(ArrayIndexOutOfBoundsException e){
                            System.out.println("\nError: Enter a Valid Name in the Format (First, Last)\n");
                            continue;
                        }                       
                    }
                    
                }
                else if(response == 2){
                    String courseName = "";
                    String courseDay = "";
                    String courseTime = "";
                    System.out.println("\nType \"x\" when finished\n");

                    while(true){

                        System.out.print("Enter New Course(s): "); 
                        courseName = input.nextLine();

                        if(courseName.equalsIgnoreCase("x")){  // when user is finished stop processing 
                            break;
                        }

                        System.out.println("\n\nFormat Days: MON, TUES, WED, etc.");
                        System.out.printf("Enter the Day for Course \"%s\": ", courseName);
                        courseDay = input.nextLine();

                        System.out.println("\n\nFormat Time: HH:MM - HH:MM");
                        System.out.printf("Enter the Time for Course \"%s\": ", courseName);
                        courseTime = input.nextLine();       

                        String courseToDatabase = 
                        "INSERT INTO student_course_schedule.courses (course_name, course_day, course_time) "+ // course_id is auto incremented so it is not needed here in the column list
                        "VALUES (?, ?, ?)"; // use a prepared statement with ? that adds the value specified later

                        queryStatement = connection.prepareStatement(courseToDatabase, Statement.RETURN_GENERATED_KEYS); //prepareStatement can execute with parameters and prevents SQL injection
                        queryStatement.setString(1, courseName);     // add course_name into the 1st position indicated in the INSERT statement                
                        queryStatement.setString(2, courseDay);     // add course_day into the 2nd position indicated in the INSERT statement 
                        queryStatement.setString(3, courseTime);   // add course_time into the 3rd position indicated in the INSERT statement 

                        try{
                            queryStatement.executeUpdate();     // execute the insert statement
                            System.out.printf("\n>>> Successfully Added: %s\n>>> Day: %s\n>>> Time: %s\n\n", courseName, courseDay, courseTime);
                        }catch(SQLException e){
                            System.out.println("\nError: Duplicate Course in the Database");
                            System.out.printf(">>> Course %s was not added!\n\n", courseName);
                            continue;
                        }

                        ResultSet mapResult;
                        mapResult = queryStatement.getGeneratedKeys();  // get result from query
                        int last_inserted_id;

                        if(mapResult.next()){    // check value in query   
                            last_inserted_id = mapResult.getInt(1);  // get the id associated with the row added
                            courseMap.put(last_inserted_id, courseTime);  // add to hashmap
                        }  
                        
                        // TEST HASHMAP
                        // for (int key: courseMap.keySet()) {
                        //     String value = courseMap.get(key).toString();
                        //     System.out.println("Key: " + key + " Value: " + value);
                        // }
                    }
                }
                else if(response == 3){
                    String assignCourseToStudent = "";

                    System.out.print("\nEnter Student ID and Course ID to Add(Student ID, Course ID): "); 
                    assignCourseToStudent = input.nextLine();

                    String[] splitCourseToStudent = assignCourseToStudent.split(", ");

                    //query statement
                    String enrollStudentInCourse = 
                        "INSERT INTO student_course_schedule.link_student_course (student_id, course_id) "+ // course_id is auto incremented so it is not needed here
                        "SELECT ?, ? "+      // select student_id and course_id and use a prepared statement with ? that adds the value specified later
                        "WHERE NOT EXISTS "+
                        "(SELECT * "+    // select all columns and if there is a match do not insert the row otherwise the row doesn't exist and insert the row
                        "FROM student_course_schedule.link_student_course lsc "+
                        "JOIN student_course_schedule.courses c "+
                        "ON c.course_id = lsc.course_id "+
                        "WHERE lsc.student_id = ? AND c.course_id = ? AND c.course_time = ?)"; // use a prepared statement with ? that adds the value specified later
                    

                    try{              
                        queryStatement = connection.prepareStatement(enrollStudentInCourse);
                        queryStatement.setInt(1, Integer.valueOf(splitCourseToStudent[0]));  // convert to int and assign student ID in 1st position of ? 
                        queryStatement.setInt(2, Integer.valueOf(splitCourseToStudent[1])); // convert to int and assign course ID in 2nd position of ?
                        queryStatement.setInt(3, Integer.valueOf(splitCourseToStudent[0]));  // convert to int and assign student ID in 3rd position of ? 
                        queryStatement.setInt(4, Integer.valueOf(splitCourseToStudent[1]));   // convert to int and assign course ID in 4th position of ?
                        queryStatement.setString(5, courseMap.get(Integer.valueOf(splitCourseToStudent[1]))); // convert to int and assign course ID in 5th position of ? as a string, since the time is a string type

                        queryStatement.executeUpdate();     // execute the insert statement
                        System.out.printf("\n>>> Successfully Enrolled Student ID %s to Course ID %s\n", String.valueOf(splitCourseToStudent[0]), String.valueOf(splitCourseToStudent[1]));
                        System.out.println(">>> Note: Duplicate Course Entries Will Be Overidden");

                    }catch(SQLException e){
                        System.out.println("\nError: Student ID or Course ID Does Not Exist");
                        System.out.printf(">>> Student ID %s was not enrolled in Course ID %s\n", String.valueOf(splitCourseToStudent[0]), String.valueOf(splitCourseToStudent[1]));
                        continue;

                    }catch(NumberFormatException e){
                        System.out.println("\nError: Invalid Command - Try Requesting Another Response");
                        continue;
                    }
                    
                }
                else if(response == 4){
                    System.out.print("\nEnter Student ID to View Current Courses: ");
                    String studentID = input.nextLine();

                    String studentToCourses = 
                        "SELECT CONCAT(first_name, last_name) AS Student, course_name AS Course "+
                        "FROM student_course_schedule.link_student_course lsc JOIN student_course_schedule.courses c "+ // join linking table with courses
                        "ON c.course_id = lsc.course_id "+
                        "JOIN student_course_schedule.students s "+  // join linking table with students 
                        "ON s.student_id = lsc.student_id "+
                        "WHERE s.student_id = " + Integer.valueOf(studentID)+ // convert id to integer, input.nextInt() throws an error because of the first response in the beginning
                        " ORDER BY Student";  
                        //^^ add space here to make space between the WHERE clause
                    
                    selectStatement = connection.createStatement();
                    resultSet = selectStatement.executeQuery(studentToCourses);

                    System.out.println();
                    System.out.println("------------------------------------------");
                    System.out.println("Student Name                     Courses");
                    System.out.println("------------------------------------------");
                    while (resultSet.next()){
                        System.out.println(resultSet.getString("Student") + "\t\t\t" + 
                                           resultSet.getString("Course"));                           
                    }
                    System.out.println();
                    
                }
                else if(response == 5){

                    System.out.print("\nEnter Course ID to View Students in this Current Course: ");
                    String courseID = input.nextLine();

                    String coursesToStudents = 
                        "SELECT CONCAT(first_name, last_name) AS Student, course_name AS Course "+
                        "FROM student_course_schedule.link_student_course lsc JOIN student_course_schedule.courses c "+ // join linking table with courses
                        "ON c.course_id = lsc.course_id "+
                        "JOIN student_course_schedule.students s "+  // join linking table with students 
                        "ON s.student_id = lsc.student_id "+ 
                        "WHERE c.course_id = " + Integer.valueOf(courseID)+ // convert id to integer, input.nextInt() throws an error because of the first response in the beginning
                        " ORDER BY Course";
                        //^^ add space here to make space between the WHERE clause

                    selectStatement = connection.createStatement();
                    resultSet = selectStatement.executeQuery(coursesToStudents);

                    // Print Results
                    System.out.println();
                    System.out.println("----------------------------------------------");
                    System.out.println("Courses                          Student Name");
                    System.out.println("----------------------------------------------");
                    while (resultSet.next()){
                        System.out.println(resultSet.getString("Course") + "\t\t\t" + 
                                           resultSet.getString("Student"));                                
                    }
                    System.out.println();
                }
                else if(response == 6){
                    System.out.print("\nEnter Student ID to View Course Day/Times: ");
                    String courseID = input.nextLine();

                    String coursesToStudents = 
                        "SELECT CONCAT(first_name, last_name) AS Student, course_name AS Course, "+
                        "course_day AS \"Day\", course_time AS \"Time\" "+
                        "FROM student_course_schedule.link_student_course lsc JOIN student_course_schedule.courses c "+ // join linking table with courses
                        "ON c.course_id = lsc.course_id "+
                        "JOIN student_course_schedule.students s "+  // join linking table with students 
                        "ON s.student_id = lsc.student_id "+ 
                        "WHERE s.student_id = " + Integer.valueOf(courseID)+ // convert id to integer, input.nextInt() throws an error because of the first response in the beginning
                        " ORDER BY Course";
                        //^^ add space here to make space between the WHERE clause

                    selectStatement = connection.createStatement();
                    resultSet = selectStatement.executeQuery(coursesToStudents);

                    // Print Results
                    System.out.println();
                    System.out.println("---------------------------------------------------------------------------------------");
                    System.out.println("Courses                  Student Name                Day                    Time");
                    System.out.println("---------------------------------------------------------------------------------------");
                    while (resultSet.next()){
                        System.out.println(resultSet.getString("Course") + "\t\t" + 
                                           resultSet.getString("Student")+ "\t\t" +
                                           resultSet.getString("Day") + "\t\t" + 
                                           resultSet.getString("Time"));                                
                    }
                    System.out.println();

                }  
                else if(response == 7){

                    String allStudents =
                        "SELECT student_id, CONCAT(first_name, last_name) AS Student "+
                        "FROM student_course_schedule.students "+
                        "ORDER BY student_id";

                    String allCourses = 
                        "SELECT course_id, course_name AS Course, course_time AS Time "+
                        "FROM student_course_schedule.courses "+
                        "ORDER BY course_id";

                    Statement selectStudents = connection.createStatement();
                    ResultSet resultSetStudents = null;
                    resultSetStudents = selectStudents.executeQuery(allStudents);

                    Statement selectCourses = connection.createStatement();
                    ResultSet resultSetCourses = null;
                    resultSetCourses = selectCourses.executeQuery(allCourses);

                    // Print Results
                    System.out.println();
                    System.out.println("-------------------------------");
                    System.out.println("Student ID        Students");
                    System.out.println("-------------------------------");
                    while (resultSetStudents.next()){
                        System.out.println(resultSetStudents.getString("student_id") + "\t\t" + 
                                           resultSetStudents.getString("Student"));
                                                                         
                    }
                    
                    System.out.println("\n");
                    System.out.println("------------------------------------------------------");
                    System.out.println("Course ID        Courses                    Time");
                    System.out.println("------------------------------------------------------");
                    while(resultSetCourses.next()){
                        System.out.println(resultSetCourses.getString("course_id") + "\t\t" + 
                                           resultSetCourses.getString("Course") + "\t\t" +
                                           resultSetCourses.getString("Time"));
                    }
                    
                    System.out.println();
                }
                else if(response == 8){
                    String dropDatabase = "DROP DATABASE student_course_schedule";

                    try{
                        Statement selectDatabase = connection.createStatement();
                        selectDatabase.executeUpdate(dropDatabase);
                        System.out.println(">>> Database Dropped Sucessfully!");
                    }catch(SQLException e){
                        System.out.println("Error: Database may already not exist!");
                    }

                }
                else if(response == 9){
                    input.close();    
                    break;
                }
            }       
        }catch(SQLException e){
            System.out.println(e.getSQLState());
        }
    }
}