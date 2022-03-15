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

/*
General Idea: Pass data as a parameter for processing and pass the NEXT function as a parameter that will use the
processed data
*/
public class Ten_2 {  
    public static void main(String[] args) {

        /*
         * Create a new object of TheOneT type since it's a class (blueprint), and since the bind method
         * takes IFunction as an argument and all of my classes implement IFunction, then pass a 
         * NEW class/object each time and val will update for "this" and will execute with these values for
         *  the next class that's instantiated.
         */

        new TheOneT(args[0]).
        bind(new readFileT()).
        bind(new processWordT()).
        bind(new filter_wordsT()).
        bind(new sortT()).
        printResult();
        
    }

}

     // Interface encapsulates the class to send as "functions" (classes) to other "functions" (classes)
     interface IFunctionT {
        Object call(Object arg); // return type Object
    }

    // Monad class implementation that wraps the functionalities (The only class)
    class TheOneT {

        Object val;

        TheOneT(Object v) { // constructor to initialize the object
            val = v;
        }

        public TheOneT bind(IFunctionT func) {
            val = func.call(val); // returns an object
            return this; // return this object to the next binded object
        }

        @SuppressWarnings("unchecked") // remove warning for type cast
        public void printResult() {
            List<Map.Entry<String, Integer>> finalResult = (List<Map.Entry<String, Integer>>) val;
            System.out.println("");
            System.out.print("--------------");
            System.out.println("\nFrequency List");
            System.out.println("--------------");
            int count = 0;

            for (Map.Entry<String, Integer> val : finalResult) {
                if (count < 25) { // get the top 25 words
                    System.out.println(val.getKey() + " - " + val.getValue());
                    count++;
                }
            }
            System.out.println("");
        }
    }


    // CLASS IMPLEMENTATIONS

    class readFileT implements IFunctionT {

        @Override
        public Object call(Object arg) { 

            Scanner reader;
            String filename = (String) arg;
            try {
                File file;
                file = new File(filename);
                reader = new Scanner(file);
            } catch (FileNotFoundException e) {
                reader = null;
                System.out.println("File not found. Please re-enter the .txt file in the command line.");
            }
            return reader;
        }

    }

    class processWordT implements IFunctionT {

        @Override
        public Object call(Object arg) {
            HashMap<String, Integer> wordcheck = new HashMap<>();
            Scanner reader = (Scanner) arg;

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
    }

    class filter_wordsT implements IFunctionT {

        @Override
        @SuppressWarnings("unchecked") // turn off the "check cast warning"
        
        public Object call(Object arg) {
            Scanner stop;
            File stopwords;
            HashMap<String, Integer> listwords = (HashMap<String, Integer>) arg;

            try {
                stopwords = new File("stop_words.txt");
                stop = new Scanner(stopwords);
            } catch (FileNotFoundException e) {
                stop = null;
                System.out.println("Cannot find stop_words.txt.");
            }

            while (stop.hasNext()) {
                String[] stopword = stop.nextLine().toString().split(",");
                for (int i = 0; i < stopword.length; i++) {
                    if (listwords.containsKey(stopword[i])) {
                        listwords.remove(stopword[i]);
                    }
                }
            }
            return listwords;
        }
    }

    class sortT implements IFunctionT {

        @Override
        @SuppressWarnings("unchecked") // turn off the "check cast warning"

        public Object call(Object arg) {
            List<Map.Entry<String, Integer>> listwords;
            HashMap<String, Integer> wordcheck = (HashMap<String, Integer>) arg;

            Set<Map.Entry<String, Integer>> set = wordcheck.entrySet(); // get key and values from hash map - Michael N.
            listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map -
                                                                        // Michael N.
            Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1,
                        Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue()); // sort list from greatest to least - Michael N.
                }
            });
            // End Source
            return listwords;
        }
    }

    class getResultT implements IFunctionT {

        @Override
        @SuppressWarnings("unchecked") // turn off the "check cast warning"
        public Object call(Object arg) {
            List<Map.Entry<String, Integer>> listwords = (List<Map.Entry<String, Integer>>) arg;
            return listwords;
        }
    }
