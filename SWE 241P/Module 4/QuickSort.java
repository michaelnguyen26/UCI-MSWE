//Reference: https://www.programiz.com/dsa/quick-sort

import java.util.ArrayList;

public class QuickSort{

    public int partition(ArrayList<String> array, int low, int high) {
      
        String pivot = array.get(high);
        
        int behind = low - 1; //set pointer behind my low point
    
        for (int j = low; j < high; j++) {//Loop and find if a value is greater then swap the smaller value behind
            if (array.get(j).compareTo(pivot) <= 0){

                behind++; //behind stays at the last known largest number and when a smaller number is found then increase and swap
        
                String temp = array.get(behind);  //keep last known small value in a temp value
                array.set(behind, array.get(j)); //swap large value behind with small value at j
                array.set(j, temp); //set value at j as the large value
            }
    
        } 
        String temp = array.get(behind + 1); //when low passes high, get the index in front of behind
        array.set(behind + 1, array.get(high)); //swap pivot with where the index of behind was + 1
        array.set(high, temp); //set the pivot with the value of the index of behind + 1
    
        return (behind + 1);
    }
  
    public ArrayList<String> quickSort(ArrayList<String> array, int low, int high) {
        if (low < high) {
    
            int pi = partition(array, low, high);
            
            quickSort(array, low, pi - 1); 
            quickSort(array, pi + 1, high);
        }

        return array;
    }
}
