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

// Define an interface with a void method to pass methods as parameters
interface IFunction {
    void call(Object arg, IFunction func);
}

/*
General Idea: Pass data as a parameter for processing and pass the NEXT function as a parameter that will use the
processed data
*/

class readFile implements IFunction {

    @Override
    public void call(Object arg, IFunction func) { // processWord passed as func initially

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
        func.call(reader, new filter_words()); // This says "processWord(reader, filter_words)
                                             // class, but reader and filter_words is processed in the processWord class"
    }

}

class processWord implements IFunction{

    @Override
    public void call(Object arg, IFunction func) {
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
        func.call(wordcheck, new sort()); // This says "pass as a parameter the wordcheck hashmap and filter_words,
                                        // therefore this actually says filter_words(wordcheck, new sort())
    }
}

class filter_words implements IFunction{

    @Override
    @SuppressWarnings("unchecked") // turn off the "check cast warning"
    public void call(Object arg, IFunction func) {
        Scanner stop;
        File stopwords;
        HashMap<String, Integer> listwords = (HashMap<String, Integer>) arg;

        try{
            stopwords = new File("stop_words.txt");
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

        func.call(listwords, new print());
    }
}

class sort implements IFunction{

    @Override
    @SuppressWarnings("unchecked") // turn off the "check cast warning"
    public void call(Object arg, IFunction func) {
        List<Map.Entry<String, Integer>> listwords;
        HashMap<String, Integer> wordcheck = (HashMap<String, Integer>) arg;

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

        func.call(listwords, new print());
    }
}

class print implements IFunction{

    @Override
    @SuppressWarnings("unchecked") // turn off the "check cast warning"
    public void call(Object arg, IFunction func) {
        List<Map.Entry<String, Integer>> listwords = (List<Map.Entry<String, Integer>>) arg;
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
        return; // return here so print doesn't print twice
    }
}

public class Nine {

    public static void main(String[] args) {

        // Instantiate a the readFile class and call its .call() method with the next function as a parameter
        new readFile().call(args[0], new processWord());
    }
}