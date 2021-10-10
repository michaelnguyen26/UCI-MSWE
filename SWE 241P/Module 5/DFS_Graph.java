import java.util.ArrayList;
import java.util.Stack;

public class DFS_Graph{
    Stack<Character> stack;
    ArrayList<Character> checkDiscovered;
    int[][] graphMatrix;
    boolean[] alreadyVisited;

    int v;

    public DFS_Graph(int[][] matrix){
        v = matrix.length;
        checkDiscovered = new ArrayList<>();
        stack = new Stack<Character>(); //construct a new stack for the object
        alreadyVisited = new boolean[v];
    }


    public ArrayList<Character> initiateCharArray(int[][] matrix){
        ArrayList<Character> listOfLetters = new ArrayList<>();

        for(char i = 'A'; i <= 'J'; i++){ //create an array of characters from A --> J
            listOfLetters.add(i);
        }
        return listOfLetters;
    }

    public ArrayList<Character> traverseDFS(int[][] matrix){
        ArrayList<Character> letters = initiateCharArray(matrix); //Add characters A --> J
        int startChar = 0;
        stack.push(letters.get(startChar)); //Start with A at the top of the stack
        
        while(!stack.isEmpty()){
            graphMatrix = matrix;
            char peek = stack.peek(); //start i at the top of the stack
            int i = letters.indexOf(peek); //get the index row while there is still a char in the stack
            
            if(!alreadyVisited[i]){
                
                checkDiscovered.add(stack.pop());
                alreadyVisited[i] = true; //mark as visted for the next element at the top of the stack


                for(int j = graphMatrix[0].length - 1; j >= 0; j--){ //reverse the edges to pop off the nearest stack closest to the letter value
                    if(graphMatrix[i][j] == 1 && !alreadyVisited[j]){
                        if(stack.contains(letters.get(j))){ //remove character at specific index if there is a duplicate
                            stack.remove(letters.get(j));
                        }
                        stack.push(letters.get(j));
                    } 
                }    
            }
        }       
        return checkDiscovered;     
    } 
}