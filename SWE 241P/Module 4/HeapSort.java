//Reference: https://www.programiz.com/dsa/heap-sort
  
import java.util.ArrayList;

public class HeapSort {
  
    public ArrayList<String> heapSort(ArrayList<String> arr) {
        int size = arr.size();
    
        //Build max heap
        for (int i = size/2 - 1; i >= 0; i--) { //size/2 -1 gets the parent node or the first non leaf node
            heapify(arr, size, i); // makes sure all parent nodes are greater than its children nodes
        }
    
        //Heap sort
        for (int i = size - 1; i >= 0; i--) {
            String temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);
    
            //Heapify root element to maintain the max heap structure 
            heapify(arr, i, 0);
        }
        return arr;
    }
  
    public void heapify(ArrayList<String> arr, int n, int i) {
        // Find largest among root, left child, and right child
        int largest = i; //i is the root or parent
        int left = 2*i + 1; //left child 
        int right = 2*i + 2; //right child
    
        if (left < n && arr.get(left).compareTo(arr.get(largest)) > 0)
            largest = left;
    
        if (right < n && arr.get(right).compareTo(arr.get(largest)) > 0)
            largest = right;
    
        // Swap and continue heapifying if root is not largest
        if (largest != i) { //if laregest is not the parent or root
            String swap = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, swap);
    
            heapify(arr, n, largest);
        }
    }
}
