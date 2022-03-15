import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* 
    Notes: Add data produced in the hash map from processed words and add them to a "data space"
    this data space is then pulled and read to produce the final hash map that is sorted and printed
*/

public class Thirty{

    // I made this class that extends the thread class to create multiple threads in the pool
    static class Process_Words extends Thread{ 

        // containers for the data space
        BlockingQueue<String> wordStorageContainer = new LinkedBlockingQueue<>();
        BlockingQueue<HashMap<String, Integer>> frequencyContainer = new LinkedBlockingQueue<>();

        ArrayList<String> stoplist = new ArrayList<>();
    
        /*
            Reference: 
            https://edstem.org/us/courses/16295/discussion/1088339
            https://www.geeksforgeeks.org/functional-interfaces-java/
            https://stackoverflow.com/questions/541487/implements-runnable-vs-extends-thread-in-java

            Second Approach: Runnable is a Functional Interface

            --> Runnable init = () -> {} ... lambda that takes no parameters and returns a runnable object     
        */

        public void run(){ 
            HashMap<String, Integer> wordcheck = new HashMap<>();
            
            // load and add stop words 
            try{
                File file = new File("stop_words.txt"); 
                Scanner stop = new Scanner(file); 
    
                while (stop.hasNext()) {
                    String[] stopline = stop.nextLine().toString().split(",");
    
                    for (int i = 0; i < stopline.length; i++) {
                        stoplist.add(stopline[i]);
                    }
                }
                stop.close();
    
            }catch(FileNotFoundException e){
                System.out.println(e);
            }

            try {
                // process words from queue 
                while (!wordStorageContainer.isEmpty()) {
                    String readWord;

                    // wait at least 1 second to poll from queue before giving up
                    readWord = wordStorageContainer.poll(1, TimeUnit.SECONDS); 
                
                    if (!stoplist.contains(readWord)) {
                        if(wordcheck.containsKey(readWord)){
                            wordcheck.put(readWord, wordcheck.get(readWord) + 1);
                        }
                        else{
                            wordcheck.put(readWord, 1);
                        }
                    }
                }

                // add each line of word to the data space for polling in the main method
                frequencyContainer.put(wordcheck); 

            }catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
    
    // Main function
    public static void main(String[] args) throws Exception {
        Thirty.Process_Words frequencyProcessor = new Process_Words();

        HashMap<String, Integer> finalFrequencyCount = new HashMap<>();
        List<Map.Entry<String, Integer>> listwords;

        ArrayList<String> lineOfWords = new ArrayList<>();
        ArrayList<Thread> threadPool = new ArrayList<>();

        try{
            File file = new File(args[0]);
            Scanner reader = new Scanner(file);

            while (reader.hasNext()) {
                String[] list_of_words = reader.nextLine().split("[^a-zA-Z0-9]+"); // regex: get everything that is not
                // a-z, A-Z, and 0-9 (+ means one or
                // more occurences)

                for (String word : list_of_words) {
                    lineOfWords.add(word.toLowerCase());
                }
            }
            reader.close();
        }
        catch(IOException e){
            System.out.println(e);
        }

        for(String word: lineOfWords){
            if(word.length() >= 2){
                frequencyProcessor.wordStorageContainer.add(word);
            }
        }

        // create 10 threads to work
        for(int i = 0; i < 10; i++){ 
            threadPool.add(new Thread(frequencyProcessor));
        }

        for(Thread thread: threadPool){
            thread.start();
        }

        for(Thread thread: threadPool){
            thread.join();
        }

        // pull from data space and add to the final frequency counter
        while (!frequencyProcessor.frequencyContainer.isEmpty()) {
            HashMap<String, Integer> words = frequencyProcessor.frequencyContainer.poll();

            for(Map.Entry<String, Integer> val : words.entrySet()){
                String word = val.getKey();

                if(finalFrequencyCount.containsKey(word)){
                    finalFrequencyCount.put(word, finalFrequencyCount.get(word) + val.getValue()); // increase existing count
                }
                else{
                    finalFrequencyCount.put(word, val.getValue()); // add for the first time
                }
            }
        }
        
        // Source from Crista
        Set<Map.Entry<String, Integer>> set = finalFrequencyCount.entrySet(); // get key and values from hash map - Michael N.
        listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map - Michael N.
        Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // sort list from greatest to least - Michael N.
            }
        });
        // End Source

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
}