import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Iterators {

    static class ReadFile implements Iterator<String> {
        Scanner reader;
        String fileName;
    
        public ReadFile(String fileName) {
            this.fileName = fileName;
            try {
                File file;
                file = new File(fileName);
                reader = new Scanner(file);
            } catch (FileNotFoundException e) {
                reader = null;
                System.out.println("File not found. Please re-enter the .txt file in the command line.");
            }
        }
    
        public boolean hasNext() {      
            return reader.hasNext();
        }
    
        public String next() {
            return reader.nextLine().toLowerCase();
        }
    }
    
    static class ProcessWords implements Iterator<String[]> {
        Iterator<String> previousObject;
        HashMap<String, Integer> wordcheck = new HashMap<>();
        String[] list_of_words = null;
    
        public ProcessWords(Iterator<String> iteratorObject) {
            this.previousObject = iteratorObject;
        }
    
        public boolean hasNext() {
            return previousObject.hasNext();
        }
    
        public String[] next() {
            if (this.hasNext()) {
                list_of_words = previousObject.next().split("[^a-zA-Z0-9]+");        
            }
            return list_of_words;
        }
    }
    
    static class RemoveStopWords implements Iterator<HashMap<String, Integer>> {
    
        Iterator<String[]> previousObject;
        ArrayList<String> stop_list = new ArrayList<>();
        HashMap<String, Integer> wordcheck = new HashMap<>();
        ArrayList<String> pull_words = new ArrayList<>();
        
        Scanner stop;
        File stopwords;

        public RemoveStopWords(Iterator<String[]> iteratorObject) {
            this.previousObject = iteratorObject;
            loadFilter();
        }
    
    
        // This method will read the stop_words file for filtering
        public void loadFilter() {
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
                    stop_list.add(stopword[i]);
                }
            }
    
        }
    
        
        public boolean hasNext() {
            return previousObject.hasNext();
        }
    
        public HashMap<String, Integer> next() {
            // request more words here
            
            int i = 1;
            while(i % 1400 != 0 && previousObject.hasNext()){
                Collections.addAll(pull_words, previousObject.next());
                i++;
            }

            // process the requested words
            for (String word : pull_words) {
                if(!stop_list.contains(word)){
                    Integer check = wordcheck.get(word); // check if the word is in the hash map
    
                    if (check == null && word.length() >= 2) {
                        wordcheck.put(word, 1);
                    } else if (word.length() >= 2) { // get words with two characters or more
                        wordcheck.put(word, wordcheck.get(word) + 1);
                    }
    
                }
                
            }
            pull_words.clear(); // need to clear so words do not repeat
            
            return wordcheck;
        }
    
    }
    
    static class SortWords implements Iterator<List<Map.Entry<String, Integer>>> {
    
        List<Map.Entry<String, Integer>> listwords;
        Iterator<HashMap<String, Integer>> previousObject;
    
        public SortWords(Iterator<HashMap<String, Integer>> iteratorObject){
            this.previousObject = iteratorObject;
        }
    
        public boolean hasNext() {
            return previousObject.hasNext();
        }
    
        public List<Map.Entry<String, Integer>> next() {
            
            if(previousObject.hasNext()){
                // Source from Crista
                Set<Map.Entry<String, Integer>> set = previousObject.next().entrySet(); // get key and values from hash map - Michael N.
                listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map - Michael N.
                Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> o1,
                            Map.Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue()); // sort list from greatest to least - Michael N.
                    }
                });
                // End Source
            }
            return listwords;
        }
    }
    
    public static void main(String[] args) {

        Iterators.ReadFile initiateFile = new ReadFile(args[0]);
        Iterators.ProcessWords processWords = new ProcessWords(initiateFile);
        Iterators.RemoveStopWords removeWords = new RemoveStopWords(processWords);
        Iterators.SortWords sortedWords = new SortWords(removeWords);
        
        int iterationCount = 0;
        while(sortedWords.hasNext()){
            iterationCount++;
            System.out.println("");
            System.out.print("----------------------------");
            System.out.println("\nFrequency List: Iteration " + iterationCount);
            System.out.println("----------------------------");
                
            List<Map.Entry<String, Integer>> listwords = sortedWords.next();
            int count = 0;
            
            for (Map.Entry<String, Integer> val : listwords) {
                if (count < 25) { // get the top 25 words
                    System.out.println(val.getKey() + " - " + val.getValue());
                    count++;
                }
            }
            System.out.println("");
        }
        System.out.println("Total Iterations for Result: " + iterationCount);
        System.out.println("");

    }
}