import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/* @author Michael Nguyen
 * SWE 262P - UC Irvine MSWE Program
*/

// Define the database class to build the database object 

class Database {

    /**
     * @param conn - The connection object to the database
     * @param dbPath - The path to where the database will be created or used if already exists
     */

    public static void buildDatabase(Connection conn, String dbPath) {

        try {

            // create a connection to the existing database or build a new database
            conn = DriverManager.getConnection(dbPath);
            System.out.println("\n>> Connection to SQLite has been established!");

            // create a statement object to hold queries
            Statement query = conn.createStatement();

            /* create a table for frequency --> key_word is the words with a data type of variable string length of 255
            *                               --> count is its frequency with a data type of INTEGER
            */
            String createFreqTable = "CREATE TABLE frequency (key_word VARCHAR(255) UNIQUE, count INTEGER)";

            // statement object (query) executes the update statement
            query.executeUpdate(createFreqTable); 

            System.out.println(">> Database Successfully Built!");

        } catch (SQLException ex) {
            System.out.println();
            System.out.println(">> Databse Exists: This Database will be Used for Processing!");
        }
    }
}

public class TwentySix {

    /** 
     * @param conn - The connection object to the database 
    */

    public static void queryResult(Connection conn) {
        Statement selectStatement;
        ResultSet rs;

        try {
            /* Select the words from the "key_word" column and the "count" column from the frequency table
            * ORDER BY (or sort) by the count (frequency) in descending order and limit to only the top 25 words 
            */ 
            String select = "SELECT key_word AS Word, count FROM frequency ORDER BY count DESC LIMIT 25";

            // this "binds" a statement object from my connected database to the Statement object
            selectStatement = conn.createStatement(); 

            // retrieve the result from the executed query and store it in my ResultSet object
            rs = selectStatement.executeQuery(select); 

            System.out.println();
            System.out.print("--------------");
            System.out.println("\nFrequency List");
            System.out.println("--------------");

            // while my ResultSet object still contains values...print the results from the query
            while (rs.next()) { 
                System.out.println(rs.getString("Word") + " - " + rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    /** 
     * @param conn - The connection object to the database
     * @param data - The data to be loaded into the database of type <String, Integer> 
    */

    public static void addData(HashMap<String, Integer> data, Connection conn) {
        PreparedStatement queryStatement; // prepared statement used for multiple executions (i.e. the question mark)
        
        // Insert the data into my frequency table in columns "key_word" and "count"
        String statement = "INSERT INTO frequency (key_word, count) " +
                "VALUES (?, ?)";

        try {
            queryStatement = conn.prepareStatement(statement);

            for (Map.Entry<String, Integer> values : data.entrySet()) {
                queryStatement.setString(1, values.getKey()); // add the word into the first question mark which coincides with "key_word"
                queryStatement.setInt(2, values.getValue()); // add the frequency into the second question mark which coincides with "count"
                queryStatement.executeUpdate(); // execute the update query 
            }
        } catch (SQLException e) {
            System.out.println(">> Unique Words Successfully added to the Table!");
        }
    }
    
    /** 
     * @param fileName - The file name of the data to be processed 
    */
    public static HashMap<String, Integer> processWords(String fileName) {
        Scanner reader;
        Scanner stop;
        File stopwords;
        HashMap<String, Integer> wordcheck = new HashMap<>();

        // Read File

        try {
            File file;
            file = new File(fileName);
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            reader = null;
            System.out.println("File not found. Please re-enter the .txt file in the command line.");
        }

        // Process Unique Words

        while (reader.hasNext()) {
            String[] list_of_words = reader.nextLine().split("[^a-zA-Z0-9]+"); // regex: get everything that is not
            // a-z, A-Z, and 0-9 (+ means one or
            // more occurences)

            for (String word : list_of_words) {
                String lower = word.toLowerCase();
                Integer check = wordcheck.get(lower); // check if the word is in the hash map

                if (check == null && lower.length() >= 2) {
                    wordcheck.put(lower, 1);
                } else if (lower.length() >= 2) { // get words with two characters or more
                    wordcheck.put(lower, wordcheck.get(lower) + 1);
                }
            }
        }

        // Process Stop Words

        try {
            stopwords = new File("../stop_words.txt");
            stop = new Scanner(stopwords);
        } catch (FileNotFoundException e) {
            stop = null;
            System.out.println("Cannot find stop_words.txt.");
        }

        while (stop.hasNext()) {
            String[] stopword = stop.nextLine().toString().split(",");
            for (int i = 0; i < stopword.length; i++) {
                if (wordcheck.containsKey(stopword[i])) {
                    wordcheck.remove(stopword[i]);
                }
            }
        }

        return wordcheck;
    }

    public static void main(String[] args) {

        Connection connection = null; // create a connection object
        String path = null;

        // db parameters
        String url = "jdbc:sqlite:C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 262P/Week 6/Assignments/TwentySix/freq.db";

        // create a connection and build the database
        Database.buildDatabase(connection, url);

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url);

            // path to file name
            path = args[0];

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nError: Please enter ../pride-and-prejudice.txt in the command line or shell.");
        } catch (SQLException e) {
            System.out.println(e);
        }

        // Add the Data to the Database
        addData(processWords(path), connection);

        // Query the Result
        queryResult(connection);
    }
}