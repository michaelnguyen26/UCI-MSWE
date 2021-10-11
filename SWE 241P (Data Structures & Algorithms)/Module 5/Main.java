import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int[][] graphMatrix = {

            //Graph Matrix of the Left Graph in Problem 5-1 (10x10 Matrix)
            
            //0  1  2  3  4  5  6  7  8  9
            //A  B  C  D  E  F  G  H  I  J
            {0, 1, 0, 1, 0, 0, 0, 0, 1, 0},  // A (0)
            {1, 0, 1, 1, 1, 0, 0, 0, 0, 0},  // B (1)
            {0, 1, 0, 0, 1, 1, 0, 0, 0, 0},  // C (2)
            {1, 1, 0, 0, 1, 0, 1, 0, 0, 0},  // D (3)
            {0, 1, 1, 1, 0, 1, 1, 1, 0, 0},  // E (4)
            {0, 0, 1, 0, 1, 0, 0, 1, 0, 0},  // F (5)
            {0, 0, 0, 1, 1, 0, 0, 1, 1, 1},  // G (6)
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 1},  // H (7)
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},  // I (8)
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0},  // J (9)
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
