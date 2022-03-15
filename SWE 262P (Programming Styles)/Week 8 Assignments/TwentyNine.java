import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/* 
    Notes: Each class that extends "ActiveWFObject" causes a multi-level inheritance since
   "ActiveWFObject" extends Thread.
*/

public class TwentyNine{

    // since each class extends "ActiveWFObject", the first parameter can pass the class and the object[]
    public static void send(ActiveWFObject r, Object[] messageObject) {
        r._queueMessage(messageObject); // send object[] to _queueMessage()
    }

    // Start of WordFrequencyController

    static class WordFrequencyController extends ActiveWFObject{
        DataStorageManager swManager;

        public void _dispatch(Object[] messageObject){
            if(messageObject[0].equals("run")){
                run(new Object[]{messageObject[1]});
            }
            else if(messageObject[0].equals("top25")){
                _display(new Object[]{messageObject[1]});
            }
        }

        public void run(Object[] message){
            swManager = (DataStorageManager) message[0];
            send(swManager, new Object[]{"frequency", this});
        }

        @SuppressWarnings("unchecked")
        public void _display(Object[] msg){

            // cast the sorted list from the object[]
            List<Map.Entry<String, Integer>> listwords = (List<Map.Entry<String, Integer>>) msg[0];

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

            // Finished with execution process
            end();
            send(swManager, new Object[]{"done"});
        }
    }

    /* NOTES: Abstract Classes

    * An abstract class can have a mixture of abstract methods and regular methods. These classes cannot be 
    * instantiated, but rather inherited. This provides encapsulation of certain functionalities and only providing
    * functionalites that should be implemented by making a method abstract. This is because abstract methods
    * must be defined in the inheriting class. 

    */

    // Reference: https://stackoverflow.com/questions/541487/implements-runnable-vs-extends-thread-in-java
    static abstract class ActiveWFObject extends Thread {
        boolean should_stop = false;

        // wait for items to be in the queue to poll and it is not full when adding an element
        BlockingQueue<Object[]> blockingQueue = new LinkedBlockingQueue<>(); 

        @Override
        public void run() {
            while (!should_stop) {
                Object[] message = blockingQueue.poll(); // get object[] from queue

                if (message != null) {
                    _dispatch(message);
                    if (message[0].equals("done")) {
                        should_stop = true;
                    }
                }
            }
        }
      
        public void _queueMessage(Object[] msgToQueue) {
            blockingQueue.add(msgToQueue);
        }
        
        public void end() { // end processing
            should_stop = true;
        }

        abstract void _dispatch(Object[] messageObject); // must be implemented by inherited classes
    }

    // Start of DataStorageManager

    static class DataStorageManager extends ActiveWFObject {
        String fileName; 
        ArrayList<String> lineOfWords = new ArrayList<>();
        StopWordsManager swManager;

        public void _dispatch(Object[] messageObject){

            if(messageObject[0].equals("init")){
                _init(new Object[]{messageObject[1], messageObject[2]});
            }
            else if(messageObject[0].equals("frequency")){
                _process_words(new Object[]{messageObject[1]});
            }
            else{
                send(swManager, messageObject);
            }
        }

        public void _init(Object[] message){
            fileName = (String) message[0];
            swManager = (StopWordsManager) message[1];

            try{
                File file = new File(fileName);
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
        }

        public void _process_words(Object[] message){
            WordFrequencyController wfController = (WordFrequencyController) message[0];

            for(String word: lineOfWords){
                send(swManager, new Object[]{"filter", word}); // tell stop words to start filtering
            }
            send(swManager, new Object[]{"top25", wfController});
        }
    }


    // Start of WordFrequencyManager

    static class WordFrequencyManager extends ActiveWFObject {
        HashMap<String, Integer> wordcheck = new HashMap<>();
        List<Map.Entry<String, Integer>> listwords;
        
        public void _dispatch(Object[] messageObject){
            if(messageObject[0].equals("word")){
                _increment_count_words(new Object[]{messageObject[1]});
            }
            else if(messageObject[0].equals("top25")){
                _top25(new Object[]{messageObject[1]});
            }

        }
        
        public void _increment_count_words(Object[] msg){
            String word = (String) msg[0];
            if(wordcheck.containsKey(word)){
                wordcheck.put(word, wordcheck.get(word) + 1);
            }
            else{
                wordcheck.put(word, 1);
            }
        }

        public void _top25(Object[] message){
            List<Map.Entry<String, Integer>> sortedMap = sort_frequency();
            WordFrequencyController recepient = (WordFrequencyController) message[0];
            send(recepient, new Object[]{"top25",sortedMap});
        }

        public List<Map.Entry<String, Integer>> sort_frequency(){

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
            return listwords;
        }
    }

    // Start of StopWordsManager

    static class StopWordsManager extends ActiveWFObject {
        ArrayList<String> stoplist;
        WordFrequencyManager word_freqs_manager;

        public void _init(Object[] message){
            word_freqs_manager = (WordFrequencyManager) message[0];
            stoplist = new ArrayList<String>();

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
            }
            catch(FileNotFoundException e){
                System.out.println(e);
            }
        }

        public void _dispatch(Object[] messageObj){
            if(messageObj[0].equals("init")){
                _init(new Object[]{messageObj[1]});
            }
            else if(messageObj[0].equals("filter")){
                _filter(new Object[]{messageObj[1]});
            }
            else{
                send(word_freqs_manager, messageObj);
            }
        }

        public void _filter(Object[] message){
            String word = (String) message[0];
            if(!stoplist.contains(word) && word.length() >= 2){ // found a word to send 
                send(word_freqs_manager, new Object[]{"word", word});
            }
        }
    }

    // Main Method
    public static void main(String[] args){

        WordFrequencyManager wfManager = new WordFrequencyManager();
        
        StopWordsManager swManager = new StopWordsManager();
        send(swManager, new Object[]{"init", wfManager}); // send an object array with "init" and wfManager

        DataStorageManager storageManager = new DataStorageManager();
        send(storageManager, new Object[]{"init", args[0], swManager}); // send an object array with "init" and swManager

        WordFrequencyController wfController = new WordFrequencyController();
        send(wfController, new Object[]{"run", storageManager}); // send an object array with "run" and storageManager

        // Start each manager as a thread individually
        try{
            wfManager.start();
            swManager.start();
            storageManager.start();
            wfController.start();

        }catch(IllegalThreadStateException e){
            System.out.println(e);
        }  
    }
}