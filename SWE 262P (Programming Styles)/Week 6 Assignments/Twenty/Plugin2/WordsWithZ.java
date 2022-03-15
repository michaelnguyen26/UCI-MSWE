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

public class WordsWithZ implements FrequencyExtract {

    public Scanner readFile(String args) {
        Scanner reader;

        try {
            File file;
            file = new File(args);
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            reader = null;
            System.out.println("File not found. Please re-enter the .txt file in the command line.");
        }
        return reader;
    }

    public HashMap<String, Integer> processWord(Scanner reader) {
        HashMap<String, Integer> wordcheck = new HashMap<>();

        while (reader.hasNext()) {
            String[] list_of_words = reader.nextLine().split("[^a-zA-Z0-9]+"); // regex: get everything that is not
            // a-z, A-Z, and 0-9 (+ means one or
            // more occurences)

            for (String word : list_of_words) {
                String lower = word.toLowerCase();
                Integer check = wordcheck.get(lower); // check if the word is in the hash map

                if (lower.contains("z")) {
                    if (check == null && lower.length() >= 2) {
                        wordcheck.put(lower, 1);
                    } else if (lower.length() >= 2) { // get words with two characters or more
                        wordcheck.put(lower, wordcheck.get(lower) + 1);
                    }

                }

            }
        }
        return wordcheck;
    }

    public HashMap<String, Integer> processStopWords(HashMap<String, Integer> listwords) {
        Scanner stop;
        File stopwords;

        try {
            stopwords = new File("../../stop_words.txt");
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
