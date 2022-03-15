import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Five {

    // define global variables to be used in the entire program
    static File file;
    static File stopwords;
    static Scanner reader;
    static Scanner stop;
    static HashMap<String, Integer> wordcheck = new HashMap<>();
    static ArrayList<String> list = new ArrayList<>();
    static List<Map.Entry<String, Integer>> listwords;

    // global method declarations
    public static void readFile(String args) throws ArrayIndexOutOfBoundsException, FileNotFoundException, NullPointerException{
        file = new File(args);
        stopwords = new File("stop_words.txt");
        reader = new Scanner(file);
        stop = new Scanner(stopwords);
    }

    public static void removeStopWords() throws NullPointerException{

        while (stop.hasNext()) {
            String[] stoplist = stop.nextLine().toString().split(",");
            for (int i = 0; i < stoplist.length; i++) {
                list.add(stoplist[i]);
            }
        }
    }

    public static void processWord() {
        while (reader.hasNext()) {
            String[] list_of_words = reader.nextLine().split("[^a-zA-Z0-9]+"); // regex: get everything that is not
            // a-z, A-Z, and 0-9 (+ means one or
            // more occurences)

            for (String word : list_of_words) {
                String lower = word.toLowerCase();
                Integer check = wordcheck.get(lower); // check if the word is in the hash map

                if (check == null) {
                    wordcheck.put(lower, 1);
                } else if (!(list.contains(lower)) && lower.length() >= 2) { // get words with two characters or
                                                                             // more
                    wordcheck.put(lower, wordcheck.get(lower) + 1);
                }
            }
        }
    }

    public static void sort() throws NullPointerException{
        // Source from Crista
        Set<Map.Entry<String, Integer>> set = wordcheck.entrySet(); // get key and values from hash map - Michael N.
        listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map - Michael N.
        Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // sort list from greatest to least - Michael N.
            }
        });
        // End Source
    }

    public static void printResult(){

        System.out.println("");
        System.out.print("--------------");
        System.out.println("\nFrequency List");
        System.out.println("--------------");
        int count = 0;

        for (Map.Entry<String, Integer> val : listwords) {
            if (count < 25) { // get the top 25 words
                System.out.println(val.getKey() + " - " + val.getValue());
                count++;
            }
        }
        System.out.println("");
    }

    public static void main(String[] args){

        /* RUN IN SHELL COMMAND LINE */

        try {
            readFile(args[0]);
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nMessage:\n\nPlease enter the path to the .txt files in the command line as: \n\n" +
                    "               java Five.java ../pride-and-prejudice.txt");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            removeStopWords();
            processWord();
        }catch(NullPointerException e){
            System.out.println("\nError - Initialized data without any data. Re-run the program with the correct command line arguments.");
        }
        
        sort();
        printResult();
 
        try{
            stop.close(); // defined as global so close() here is okay
            reader.close(); // defined as global so close() here is okay
        }catch (NullPointerException e){
            System.out.println("Error - Cannot close scanner. Re-run the program with the correct command line arguments.");
        } 
    }
}