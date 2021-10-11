public class HashSet {
    private LinkedListSet[] hashArray;
    private int keyRange;
    private int size = 0;
  
    public HashSet(){
        this.keyRange = 1000;
        this.hashArray = new LinkedListSet[this.keyRange];
        //LinkedListSet is an array, and in each index in the array is a linkedlist
    }
  
    public int hashFunction(String word) {
        return (word.length()%this.keyRange);
    }
  
    public boolean add(String word) {
        int bucketIndex = this.hashFunction(word);
        if(hashArray[bucketIndex] == null){
            hashArray[bucketIndex] = new LinkedListSet();
            hashArray[bucketIndex].add(word);
            size++;
            return true;
        }else{
            if(!hashArray[bucketIndex].contains(word)){
                hashArray[bucketIndex].add(word);
                size++;
                return true;
            }
        }
        return false;
    }

    public boolean contains(String word) {
        int bucketIndex = this.hashFunction(word);
        if(hashArray[bucketIndex] == null){
            return false;
        }
        else{
            if(hashArray[bucketIndex].contains(word)){
                return true;
            }
        }     
        return false;
    }

    public int size(){
        return size;
    }
}

