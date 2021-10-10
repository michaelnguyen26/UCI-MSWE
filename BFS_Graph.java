import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;

public class BFS_Graph {
    Queue<Character> queue;
    ArrayList<Character> checkDiscovered;
    int[][] graphMatrix;
    boolean alreadyChecked[];

    int v;

    public BFS_Graph(int[][] matrix){
        v = matrix.length;
        checkDiscovered = new ArrayList<>();
        queue = new LinkedList<>(); //linkedlist implements the queue interface
        alreadyChecked = new boolean[v];
    }


    public ArrayList<Character> initiateCharArray(int[][] matrix){
        ArrayList<Character> listOfLetters = new ArrayList<>();

        for(char i = 'A'; i <= 'J'; i++){ //create an array of characters from A --> J
            listOfLetters.add(i);
        }
        return listOfLetters;
    }


    public ArrayList<Character> traverseBFS(int[][] matrix){
        ArrayList<Character> letters = initiateCharArray(matrix); //Add characters A --> J
        int startChar = 0;
        
        queue.add(letters.get(startChar)); //Start with A
        alreadyChecked[startChar] = true;  //Mark A as checked
        
        while(!queue.isEmpty()){
            graphMatrix = matrix;
            char peek = queue.peek(); //start i at the front of the queue
            int i = letters.indexOf(peek); //check row while there is still a queue

            for(int j = 0; j < graphMatrix[0].length; j++){ //start at column closest to the letter value start
                if(graphMatrix[i][j] == 1 && !alreadyChecked[j]){ //if there is an edge and not already checked
                    queue.add(letters.get(j));
                    alreadyChecked[j] = true;
                }     
            }
            checkDiscovered.add(queue.remove()); //remove the front of the queue
        } 
        return checkDiscovered;     
    }
}