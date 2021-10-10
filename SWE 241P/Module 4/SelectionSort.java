import java.util.ArrayList;

public class SelectionSort{
    private int minIndex;
    private String tempVal;

    //For i = 0 to arr.length choose the first as the minimum
    //For i + 1 compare their values and swap if greater than min

    public ArrayList<String> ssort(ArrayList<String> arr){
        for(int i = 0; i < arr.size(); i++){
            minIndex = i;   //get first index and set to min to compare with values in the rest of the array

            for(int j = i + 1; j < arr.size(); j++){
                if(arr.get(minIndex).compareTo(arr.get(j)) > 0){   //If arr[minIndex] is greater than arr[j], then arr[j] is smaller and swap their positions. Update minIndex to compare small values with other values that may be smaller than itself.
                    minIndex = j;      //Get index of min value, if not this loop is in the scope of the for loop and will not exist if not executed
                }
            }
            //Swap here, if not needed it will just swap with itself
            tempVal = arr.get(i);   //Store value to not lose when swapping
            arr.set(i, arr.get(minIndex)); //Swap min if exists, otherwise it will be itself 
            arr.set(minIndex, tempVal); //Swap high to where min was located
        }
        return arr;
    }
}