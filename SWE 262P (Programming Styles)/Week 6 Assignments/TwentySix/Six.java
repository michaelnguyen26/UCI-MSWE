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

public class Six {

    // method declarations - static (global)
    public static Scanner readFile(String args){
        Scanner reader;

        try{
            File file;
            file = new File(args);
            reader = new Scanner(file);
        }catch(FileNotFoundException e){
            reader = null;
            System.out.println("File not found. Please re-enter the .txt file in the command line.");
        }
        return reader;
    }

    public static HashMap<String, Integer> processWord(Scanner reader) {
        HashMap<String, Integer> wordcheck = new HashMap<>();

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
        return wordcheck;
    }

    public static HashMap<String, Integer> removeStopWords(HashMap<String, Integer> listwords){
        Scanner stop;
        File stopwords;

        try{
            stopwords = new File("../stop_words.txt");
            stop = new Scanner(stopwords);
        }catch(FileNotFoundException e){
            stop = null;
            System.out.println("Cannot find stop_words.txt.");
        }
        
        while (stop.hasNext()) {
            String[] stopword = stop.nextLine().toString().split(",");
            for (int i = 0; i < stopword.length; i++) {
                if(listwords.containsKey(stopword[i])){
                    listwords.remove(stopword[i]);
                }
            }
        } 
        return listwords;
    }
    
    public static List<Map.Entry<String,Integer>> sort(HashMap<String, Integer> wordcheck) throws NullPointerException{
        // Source from Crista
        List<Map.Entry<String, Integer>> listwords;

        Set<Map.Entry<String, Integer>> set = wordcheck.entrySet(); // get key and values from hash map - Michael N.
        listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map - Michael N.
        Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // sort list from greatest to least - Michael N.
            }
        });
        // End Source

        return listwords;
    }

    public static void printResult(List<Map.Entry<String, Integer>> listwords){

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

        printResult(sort(removeStopWords(processWord(readFile(args[0]))))); 
    }
}