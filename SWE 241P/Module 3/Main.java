import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Paths;
import java.lang.System;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args){

        //////////// EXERCISE 1: QUESTION 3 & 4 ////////////

        LinkedListSet linkedlist = new LinkedListSet();
        BinaryTreeSet binaryTree = new BinaryTreeSet();
        HashSet myHashSet = new HashSet();

        try{          
            //create a reader object that reads the path to my text file
            Scanner reader = new Scanner(Paths.get("C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 241P/Practice M3/pride-and-prejudice.txt"));
            
            while(reader.hasNextLine()){
                //create a string array of split strings at the regex for each line in the text file
                String[] wordLists = reader.nextLine().split("[^a-zA-Z0-9]+");
                for(int i = 0; i < wordLists.length; i++){    //this loop will add each line into the data structure
                    linkedlist.add(wordLists[i]);
                    binaryTree.add(wordLists[i]);
                    myHashSet.add(wordLists[i]);
                }  
            }

            //when loop is finished print out the data structures respective size
            System.out.println("\n");
            System.out.println("Linked List Size: " + linkedlist.size());
            System.out.println("Binary Tree Size: " + binaryTree.size());
            System.out.println("Hash Set Size: " + myHashSet.size());
        } 
        catch(IOException e){//e is of type IOException and it will get thrown when an error of that type occurs
            e.printStackTrace();
        }
        


        //////////// EXERCISE 1: QUESTION 3 & 4 ////////////

        // try{   //variables defined here are only local to its scope in the {}
        //     int linkedListNoCount = 0;
        //     int hashMapNoCount = 0;
        //     int binaryTreeNoCount = 0;
            

        //     //create a reader object that reads the path to my text file
        //     Scanner reader = new Scanner(Paths.get("C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 241P/Practice M3/words-shuffled.txt"));
            
        //     while(reader.hasNextLine()){
        //         //create a string array of split strings at the regex for each line in the text file
        //         String[] wordLists = reader.nextLine().split("[^a-zA-Z0-9]+");
        //         for(int i = 0; i < wordLists.length; i++){    //this loop will add each line into the data structure
        //             if(!linkedlist.contains(wordLists[i])){
        //                 linkedListNoCount++;
        //             }
        //             if(!binaryTree.contains(wordLists[i])){
        //                 binaryTreeNoCount++;
        //             }
        //             if(!myHashSet.contains(wordLists[i])){
        //                 hashMapNoCount++;
        //             }
        //         }  
        //     }
        //     //when loop is finished print out the data structures count
        //     System.out.println("\n");
        //     System.out.println("Linked List Count: " + linkedListNoCount);
        //     System.out.println("Binary Tree List Count: " + binaryTreeNoCount);
        //     System.out.println("Hash Set Count: " + hashMapNoCount);
        // } 
        // catch(IOException e){//e is of type IOException and it will get thrown when an error of that type occurs
        //     e.printStackTrace();
        // }




        //////////// EXERCISE 1: QUESTION 5 ////////////

        // try{          
        //     //create a reader object that reads the path to my text file
        //     Scanner reader = new Scanner(Paths.get("C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 241P/Practice M3/pride-and-prejudice.txt"));
        //     FileWriter csvFile = new FileWriter("data(linkedlist).csv");
            
        //     while(reader.hasNextLine()){
        //         //create a string array of split strings at the regex for each line in the text file
        //         String[] wordLists = reader.nextLine().split("[^a-zA-Z0-9]+");
        //         for(int i = 0; i < wordLists.length; i++){    //this loop will add each line into the data structure
        //             long start = System.nanoTime();
        //             if(linkedlist.contains(wordLists[i]) == true){
        //                 long finish = System.nanoTime();
        //                 long timeDiff = finish - start;
        //                 csvFile.append(String.valueOf(timeDiff) + "\n");
        //             }
        //             // binaryTree.add(wordLists[i]);
        //             // myHashSet.add(wordLists[i]);
        //         }  
        //     }
        //     csvFile.close();

        //     //when loop is finished print out the data structures respective size
        //     System.out.println("\n");
        //     System.out.println("Linked List Size: " + linkedlist.size());
        //     System.out.println("Binary Tree Size: " + binaryTree.size());
        //     System.out.println("Hash Set Size: " + myHashSet.size());
        // } 
        // catch(IOException e){//e is of type IOException and it will get thrown when an error of that type occurs
        //     e.printStackTrace();
        // }



         //////////// EXERCISE 1: QUESTION 5-2 ////////////

        //  try{          
        //     //create a reader object that reads the path to my text file
        //     Scanner reader = new Scanner(Paths.get("C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 241P/Practice M3/words-shuffled.txt"));
        //     FileWriter csvFile = new FileWriter("data(hashset-search-long).csv");
            
        //     while(reader.hasNextLine()){
        //         //create a string array of split strings at the regex for each line in the text file
        //         String[] wordLists = reader.nextLine().split("[^a-zA-Z0-9]+");
        //         long start = System.nanoTime();

        //         for(int i = 0; i < wordLists.length; i++){    //this loop will add each line into the data structure
        //             if(myHashSet.contains(wordLists[i])){
        //                 long finish = System.nanoTime();
        //                 long timeDiff = finish - start;
        //                 csvFile.append(String.valueOf(timeDiff) + "\n");
        //             }
        //             // binaryTree.add(wordLists[i]);
        //             // myHashSet.add(wordLists[i]);
        //         }  
        //     }
        //     csvFile.close();

        //     //when loop is finished print out the data structures respective size
        //     System.out.println("\n");
        //     System.out.println("Linked List Size: " + linkedlist.size());
        //     System.out.println("Binary Tree Size: " + binaryTree.size());
        //     System.out.println("Hash Set Size: " + myHashSet.size());
        // } 
        // catch(IOException e){//e is of type IOException and it will get thrown when an error of that type occurs
        //     e.printStackTrace();
        // }




        
        ///////// INDIVIDUAL TESTS BEGINS HERE /////////

        // Scanner scanObj = new Scanner(System.in);

        ////////--------START OF LINKEDLIST TEST--------////////////

        // //TESTING: Add method
        // System.out.println("Enter a word: (blank to quit)");
        // String input = scanObj.nextLine();
        
        // while(!input.isEmpty()){
        //     linkedlist.add(input);
        //     input = scanObj.nextLine();
        // }
        // scanObj.close();        //(don't use yet if I need to use System.in again)
        // linkedlist.print(linkedlist.head);

        // //TESTING: contains method
        // System.out.println(linkedlist.contains("Sand"));


        // //TESTING: size method
        // System.out.println(linkedlist.size());

        ////////--------END OF LINKEDLIST TEST--------////////////





        ////////--------START OF BINARY TREE TEST--------////////////

        // BinaryTreeSet binaryTree = new BinaryTreeSet();

        // System.out.println("Enter a word: (blank to quit)");
        // String inputString = scanObj.nextLine();

        // while(!inputString.isEmpty()){
        //     binaryTree.add(inputString);
        //     inputString = scanObj.nextLine();
        // }
        
        // binaryTree.printInPreOrder(binaryTree.root);
        // System.out.println("\nSize of Binary Tree: " + binaryTree.size());


        ////////--------END OF BINARY TREE TEST--------////////////





        ///////--------START OF HASH SET TEST--------/////////

        // System.out.println("Enter a word: (blank to quit)");
        // String inputString = scanObj.nextLine();

        // while(!inputString.isEmpty()){
        //     myHashSet.add(inputString);
        //     inputString = scanObj.nextLine();
        // }

        // System.out.println(myHashSet.contains("Michael"));
        // System.out.println(myHashSet.size());
        // scanObj.close();

        ///////--------END OF HASH SET TEST--------/////////
    }
}