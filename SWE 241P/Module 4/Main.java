import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.lang.System;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Main{
   public static void main(String[] args) {
        SelectionSort sort = new SelectionSort();
        InsertionSort iSort = new InsertionSort();
        MergeSort mergeSort = new MergeSort();
        QuickSort qSort = new QuickSort();
        HeapSort hSort = new HeapSort();
        ArrayList<String> arr = new ArrayList<>();

         //Initialize sorted arrays
        //  ArrayList<String> selectionArray = new ArrayList<>();
         ArrayList<String> insertionArray = new ArrayList<>();
        //  ArrayList<String> mergeArray = new ArrayList<>();
        //  ArrayList<String> quickArray = new ArrayList<>();
        //  ArrayList<String> heapArray = new ArrayList<>();

        // EXPERIMENT TESTING //
            
        try{          
            //create a reader object that reads the path to my text file
            FileWriter csvFile = new FileWriter("data_selection_sort.txt");
            Scanner reader = new Scanner(Paths.get("C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 241P/Practice M4/pride-and-prejudice.txt"));
            
            while(reader.hasNextLine()){
                String[] wordLists = new String[reader.nextLine().length()];

                //create a string array of split strings at the regex for each line in the text file
                wordLists = reader.nextLine().split("[^a-zA-Z0-9]+");

                for(int i = 0; i < wordLists.length; i++){
                    arr.add(wordLists[i]);
                }            
            }

            ArrayList<String> copyArr;
            insertionArray = arr;
            
            for(int k = 0; k < 10; k++){
                copyArr = new ArrayList<>(insertionArray);
                long start = System.nanoTime();
                sort.ssort(copyArr);

                long finish = System.nanoTime();
                long timeDiff = finish - start;
                csvFile.append(String.valueOf(timeDiff) + "\n");
            }
            csvFile.close();



            //Sort the arrays

            // selectionArray = arr;
            // insertionArray = arr;
            // mergeArray = arr;
            // quickArray = arr;
            // heapArray = arr;

            // Print the arrays
            // for(int j = 0; j < arr.size(); j++){
            //     System.out.print(insertionArray.get(j) + " ");
            // }

        } 
        catch(IOException e){//e is of type IOException and it will get thrown when an error of that type occurs
            e.printStackTrace();
        }


        ////////// SELECTION SORT TEST //////////
        
        // ArrayList<String> listOfStrings = new ArrayList<>();
        // listOfStrings.add("destiny"); 
        // listOfStrings.add("candle");
        // listOfStrings.add("break");
        // listOfStrings.add("apple");
        // listOfStrings.add("zebra");
        // listOfStrings.add("mars");
        // listOfStrings.add("interstellar");
        // listOfStrings.add("computer");

        // listOfStrings = sort.ssort(listOfStrings);

        // System.out.println();
        // for(int i = 0; i < listOfStrings.size(); i++){
        //     System.out.print(listOfStrings.get(i) + "  ");
        // }
        // System.out.println();


        ////////// INSERTION SORT TEST //////////
        
        // ArrayList<String> listOfStrings = new ArrayList<>();
        // listOfStrings.add("destiny"); 
        // listOfStrings.add("candle");
        // listOfStrings.add("break");
        // listOfStrings.add("apple");
        // listOfStrings.add("zebra");
        // listOfStrings.add("mars");
        // listOfStrings.add("interstellar");
        // listOfStrings.add("computer");

        // listOfStrings = iSort.iSort(listOfStrings);

        // System.out.println();
        // for(int i = 0; i < listOfStrings.size(); i++){
        //     System.out.print(listOfStrings.get(i) + "  ");
        // }
        // System.out.println();

        ////////// MERGE SORT TEST //////////

        // ArrayList<String> listOfStrings = new ArrayList<>();
        // listOfStrings.add("destiny"); 
        // listOfStrings.add("candle");
        // listOfStrings.add("break");
        // listOfStrings.add("apple");
        // listOfStrings.add("zebra");
        // listOfStrings.add("mars");
        // listOfStrings.add("interstellar");
        // listOfStrings.add("computer");

        // listOfStrings = mergeSort.mergeSort(listOfStrings, 0 , listOfStrings.size());

        // System.out.println();
        // for(int i = 0; i < listOfStrings.size(); i++){
        //     System.out.print(listOfStrings.get(i) + "  ");
        // }
        // System.out.println();


        ////////// QUICKSORT SORT TEST //////////

        // ArrayList<String> listOfStrings = new ArrayList<>();
        // listOfStrings.add("destiny"); 
        // listOfStrings.add("candle");
        // listOfStrings.add("break");
        // listOfStrings.add("apple");
        // listOfStrings.add("zebra");
        // listOfStrings.add("mars");
        // listOfStrings.add("interstellar");
        // listOfStrings.add("computer");

        // listOfStrings = qSort.quickSort(listOfStrings, 0 , listOfStrings.size() -1);

        // System.out.println();
        // for(int i = 0; i < listOfStrings.size(); i++){
        //     System.out.print(listOfStrings.get(i) + "  ");
        // }
        // System.out.println();

        

        ////////// HEAPSORT SORT TEST //////////
        
        // ArrayList<String> listOfStrings = new ArrayList<>();
        // listOfStrings.add("destiny"); 
        // listOfStrings.add("candle");
        // listOfStrings.add("break");
        // listOfStrings.add("apple");
        // listOfStrings.add("zebra");
        // listOfStrings.add("mars");
        // listOfStrings.add("interstellar");
        // listOfStrings.add("computer");

        // listOfStrings = hSort.heapSort(listOfStrings);

        // System.out.println();
        // for(int i = 0; i < listOfStrings.size(); i++){
        //     System.out.print(listOfStrings.get(i) + "  ");
        // }
        // System.out.println();
    } 
}