import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.StreamSupport;
import java.io.File;
import java.io.FileNotFoundException;


public class ThirtyTwo {

    // this class will handle the map reduce functionality 
    static class MapReduce{

        // read fileName and return the scanner object representation of the file
        public Scanner processFile(String fileName){
            Scanner reader;
    
            try{
                File file = new File(fileName);
                reader = new Scanner(file);
            }catch(FileNotFoundException e){
                reader = null;
                System.out.println(e);
            }
            return reader;
        }
        
        // partion the input into chunks of nlines and return the ArrayList<String> to be streamed
        public ArrayList<String> partition(Scanner reader, int nlines){ 
            int line_number = 1;
            String packOfWords = null; // initialize outside 

            // This will hold my stream of lines of words up to 200 lines
            ArrayList<String> lineOfWords = new ArrayList<>();


            while(reader.hasNext()){
                if(line_number == nlines){
                    packOfWords +=  " " + reader.nextLine().toLowerCase();
                    lineOfWords.add(packOfWords); // add words to the list which will be a stream of ALL of the words iterated to 200
                    packOfWords = ""; // reset packOfWords
                    line_number = 1; // reset line number
                } else{
                    packOfWords += " " + reader.nextLine().toLowerCase(); // add more to each line of words
                    line_number++;
                }
            }
            reader.close();

            // ArrayLists are streamable, therefore to simplify my implementation I will convert the list to a stream
            return lineOfWords;
        }
    
        public ArrayList<ArrayList<Object>> split_words(String batchOfWords){
            ArrayList<String> stoplist = new ArrayList<>();
            ArrayList<String> splitWords = new ArrayList<>();

            String[] list_of_words = batchOfWords.split("[^a-zA-Z0-9]+"); // regex: get everything that is not
            // a-z, A-Z, and 0-9 (+ means one or more occurences)

            for (String w : list_of_words)
                if (w.length() >= 2){
                    splitWords.add(w);
                }
    
            // load and add stop words 
            try{
                File file = new File("stop_words.txt"); 
                Scanner stop = new Scanner(file); 
    
                while (stop.hasNext()) {
                    String[] stopline = stop.nextLine().toString().toLowerCase().split(",");
    
                    for (int i = 0; i < stopline.length; i++) {
                        stoplist.add(stopline[i]);
                    }
                }
                stop.close();
    
            }catch(FileNotFoundException e){
                System.out.println(e);
            }
            
            return mapper(stoplist, splitWords);   
        }

        // method to exectue the worker of the mapper 
        public ArrayList<ArrayList<Object>> mapper(ArrayList<String> stoplist, ArrayList<String> splitWords){
            ArrayList<ArrayList<Object>> list_of_words = new ArrayList<>();  // an array list of array list objects
            ArrayList<String> filterWords = new ArrayList<>();
            ArrayList<Object> pair = new ArrayList<>();

            // filter to add non stop words
            for (String word : splitWords){ 
                if (!stoplist.contains(word)){
                    filterWords.add(word);
                }
            }

            // add each pair here since --> list_of_words is an arraylist of arraylist<object>
            for (String word: filterWords){
                pair = new ArrayList<>(); // instantiate a new arraylist for each pair to add to list_of_words
                pair.add(word);
                pair.add(1);
                list_of_words.add(pair);
            }
            return list_of_words;
        }
    
        /* Regroup Functionality: ArrayList<ArrayList<ArrayList<Object>>>
            Takes a list of lists of pairs of the form
            [[(w1, 1), (w2, 1), ..., (wn, 1)],
            [(w1, 1), (w2, 1), ..., (wn, 1)],
            ...]
            and returns a dictionary mapping each unique word to the
            corresponding list of pairs
        */
        public HashMap<String, ArrayList<ArrayList<Object>>> regroup(ArrayList<ArrayList<ArrayList<Object>>> split_list){
            HashMap<String, ArrayList<ArrayList<Object>>> wordcheck = new HashMap<>();
            ArrayList<ArrayList<Object>> resplit = new ArrayList<>();

            // for each mapping of the split_list
            for(ArrayList<ArrayList<Object>> words : split_list){
                for(ArrayList<Object> arr_object : words){

                    String keyValue = String.valueOf(arr_object.get(0));

                    // dictionary mapping each unique word
                    if(wordcheck.containsKey(keyValue))
                        wordcheck.get(keyValue).add(arr_object);
                    else{
                        resplit = new ArrayList<>(); // create new instance of array to add arraylist of arraylist<object>
                        resplit.add(arr_object);
                        wordcheck.put(keyValue, resplit);
                    }
                }
            }
            return wordcheck;
        }
    
        public HashMap<String, Integer> count_words(Map.Entry<String, ArrayList<ArrayList<Object>>> splitWords){
            int frequencyCount = 0;
            HashMap<String, Integer> wordcheck = new HashMap<>();

            for(ArrayList<Object> freqValue : splitWords.getValue()){
                // convert object to integer type and increment
                frequencyCount += Integer.valueOf((Integer) freqValue.get(1)); 
            }

            // add word and frequency to my hashmap
            wordcheck.put(splitWords.getKey(), frequencyCount);
            return wordcheck;
        }
    
        public List<Map.Entry<String, Integer>> sort(HashMap<String, Integer> wordcheck){
            List<Map.Entry<String, Integer>> listwords;

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
    
    public static void main(String[] args){
        ArrayList<ArrayList<ArrayList<Object>>> split = new ArrayList<>();
        HashMap<String, Integer> frequency = new HashMap<>();

        // instantiate my MapReduce class to handle map reduce functionality
        ThirtyTwo.MapReduce MapReduce = new MapReduce();
        
        /* 
        *  Reference: https://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
        *  Implementation: 
        *  partition the split words by converting the iterable to a stream using StreamSupport
        *  stream each value to my MapReduce method initiate_split()
        */

        Iterable<String> processedWords = MapReduce.partition(MapReduce.processFile(args[0]), 200);
        StreamSupport
        .stream(processedWords.spliterator(), false)
        .forEach(x -> split.add(MapReduce.split_words(x)));

        
        // An arraylist of an arraylist of objects as a "container" for the splitted words passed to regroup
        HashMap<String, ArrayList<ArrayList<Object>>> splittedWords = MapReduce.regroup(split); // execute the regrouping process

        for(Map.Entry<String, ArrayList<ArrayList<Object>>> word_split : splittedWords.entrySet()) {
            HashMap<String, Integer> freq = MapReduce.count_words(word_split);
            
            // get frequency from count_words
            for (Map.Entry<String, Integer> values : freq.entrySet()){
                frequency.put(values.getKey(), values.getValue());
            }
        }

        List<Map.Entry<String, Integer>> listwords = MapReduce.sort(frequency);

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