import java.util.ArrayList;

public class InsertionSort {
    private int prev;
    private String ahead;

    //Loop from i = 1 to arr.size() and compare the first value with its neighbor behind
    //Leave as sorted if sorted, otherwise swap 


    public ArrayList<String> iSort(ArrayList<String> arr){

        for(int i = 1; i < arr.size(); i++){
            ahead = arr.get(i);  //check the value ahead first
            prev = i - 1;
            
            while(prev >= 0 && ahead.compareTo(arr.get(prev)) < 0){
                arr.set(prev + 1, arr.get(prev)); //swap the index ahead with the value behind
                prev = prev - 1;  //decrease prev to compare with values behind it
            }
            arr.set(prev + 1, ahead); //array positioned ahead will be the value that is being checked
        }
        return arr;
    }
}
