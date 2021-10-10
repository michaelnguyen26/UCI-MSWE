import java.util.ArrayList;

public class Exercise3{
    public static void main(String[] args) {
        
        int[][] matrix = {
        {0,1,0},
        {1,0,1},
        {0,1,0},
    };

    // PART A:

    ArrayList<ArrayList<Integer>> adjacencyList = matrixToList(matrix, 3, 3);
    System.out.println("\nAdj. Matrix to Adj. List: " + adjacencyList);



    // PART B:

    int[][] matrix2 = listToMatrix(adjacencyList);

    // PRINT
    // int count = 0;
    // for(int i = 0; i < 3; i++){
    //     for(int j = 0; j < 2; j++){
    //         System.out.print(matrix2[i][j] + " ");
    //         count += 1;
    //         if(count == 2){
    //             System.out.println();
    //             count = 0;
    //         }
    //     }
    // }




    // PART C:
    System.out.println("Incidence Matrix to List: " + incidenceToList(matrix2, 3, 2));



}
    // PART A:

    public static ArrayList<ArrayList<Integer>> matrixToList(int[][] mat, int row, int col){
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();

        //Iterate through rows and columns
        for(int i = 0; i < row; i++){
            arr.add(new ArrayList<Integer>());
            for(int j = 0; j < col; j++){
                if(mat[i][j] == 1){
                    arr.get(i).add(j); //add the column value where there is an edge
                }
            }
        }
        return arr;
    }

    // PART B:

    public static int[][] listToMatrix(ArrayList<ArrayList<Integer>> list){
        int[][] matrix = new int[3][2]; //there are two edges in my matrix
        int edge = 0;

        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).size(); j++){ //iterate over the edges with the arraylist in the arraylist
                if(list.get(i).get(j) > i){     //Increment the list of lists until there is a value greater than i
                    matrix[i][edge] = 1;     //assign edge value to the row/vertex
                    matrix[list.get(i).get(j)][edge] = 1; //assign edge value to the row/vertex
                    edge++;
                }
            }
        }
        return matrix;
    }

    // PART C:

    public static ArrayList<ArrayList<Integer>> incidenceToList(int[][] matrix , int row, int col){
        ArrayList<ArrayList<Integer>> arrayList = new ArrayList<>();
        
        for(int j = 0; j < row; j++){
            arrayList.add(new ArrayList<Integer>()); //create an ArrayList<Integer> in my arrayList for each row that exists
        }

        for(int i = 0; i < col; i++){ //Check column one at a time
            int[] maxEdgeValue = new int[2]; //how much each edge can have in an edge column
            int indexOfEdge = 0;

            for(int j = 0; j < row; j++){
                if(matrix[j][i] == 1){//since each edge column can have two values, add it to my maxEdgeValue array
                                        //index at [j][i] since rows need to be checked before columns
                    maxEdgeValue[indexOfEdge] = j; //store index where an edge is found
                    indexOfEdge++;
                }
            }
            arrayList.get(maxEdgeValue[0]).add(maxEdgeValue[1]); //get the arraylist indexed at maxEdgeValue and add the edge value
            arrayList.get(maxEdgeValue[1]).add(maxEdgeValue[0]);
        }
        return arrayList;
    }
}