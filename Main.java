import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int[][] graphMatrix = {

            //Graph Matrix of the Left Graph in Problem 5-1 (10x10 Matrix)

            //A  B  C  D  E  F  G  H  I  J
            {0, 1, 0, 1, 0, 0, 0, 0, 1, 0},  // A
            {1, 0, 1, 1, 1, 0, 0, 0, 0, 0},  // B
            {0, 1, 0, 0, 1, 1, 0, 0, 0, 0},  // C
            {1, 1, 0, 0, 1, 0, 1, 0, 0, 0},  // D
            {0, 1, 1, 1, 0, 1, 1, 1, 0, 0},  // E
            {0, 0, 1, 0, 1, 0, 0, 1, 0, 0},  // F
            {0, 0, 0, 1, 1, 0, 0, 1, 1, 1},  // G
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 1},  // H
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},  // I
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0},  // J
        }; 

        //DEPTH FIRST SEARCH

        DFS_Graph graphDFS= new DFS_Graph(graphMatrix);
        ArrayList<Character> dfsSearchResults = new ArrayList<>();

        dfsSearchResults = graphDFS.traverseDFS(graphMatrix);
        System.out.println();
        System.out.print("Depth First Search:   ");
        for(int i = 0; i < dfsSearchResults.size(); i++){
            System.out.print(dfsSearchResults.get(i) + " ");
        }

        //BREADTH FIRST SEARCH 

        BFS_Graph graphBFS = new BFS_Graph(graphMatrix);
        ArrayList<Character> bfsSearchResults = new ArrayList<>();

        bfsSearchResults = graphBFS.traverseBFS(graphMatrix);
        System.out.println();
        System.out.print("Breadth First Search: ");
        for(int i = 0; i < bfsSearchResults.size(); i++){
            System.out.print(bfsSearchResults.get(i) + " ");
        }
        System.out.println();
    }
}
