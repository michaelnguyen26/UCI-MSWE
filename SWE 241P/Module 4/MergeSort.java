import java.util.ArrayList;

public class MergeSort {
    private int midpoint;


    public ArrayList<String> mergeSort(ArrayList<String> arr, int begin, int finish){ 
        if(arr.size() == 1){ //basis case
            return arr;
        }

        if(begin < finish){
            midpoint = (finish-begin)/2;

            ArrayList<String> leftSection = sectionArray(arr, begin, midpoint);
            ArrayList<String> rightSection = sectionArray(arr, midpoint, finish);

            leftSection = mergeSort(leftSection, begin, leftSection.size());
            rightSection = mergeSort(rightSection, begin, rightSection.size());

            // System.out.println("Left: " + leftSection);
            // System.out.println("Right" + rightSection);

            arr = merge(leftSection, rightSection);
        }
        return arr;      
    }

    public ArrayList<String> merge(ArrayList<String> subLeft, ArrayList<String> subRight){
        ArrayList<String> resultArr = new ArrayList<>();
        int left = 0; 
        int right = 0;

        while(left < subLeft.size() && right < subRight.size()){
            if(subLeft.get(left).compareTo(subRight.get(right)) >= 0){
                resultArr.add(subRight.get(right));
                right++;
            }else{
                resultArr.add(subLeft.get(left));
                left++;
            }
        }
        while(subLeft.size() > left){
            resultArr.add(subLeft.get(left));
            left++;
        }

        while(subRight.size() > right){
            resultArr.add(subRight.get(right));
            right++;
        }
        return resultArr;
    }

    public ArrayList<String> sectionArray(ArrayList<String> arr, int startPoint, int endPoint){
        ArrayList<String> subArr = new ArrayList<>(); //Create a new memory address separate for subarray
        for(int i = startPoint; i < endPoint; i++){
            subArr.add(arr.get(i));
        }
        return subArr;
    }
}