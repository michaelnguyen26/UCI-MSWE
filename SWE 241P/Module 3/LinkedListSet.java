class StringListNode{
    String val;
    StringListNode next;

    StringListNode(String x){
        val = x;
        next = null;
    }
}

public class LinkedListSet{
    private StringListNode head;
    public StringListNode tail;
    private int counterSize;

    public LinkedListSet(){
        head = null;
        tail = null;
    }

    public boolean add(String word){
        if(contains(word)){
            return false;
        }
        if(head == null) {
            StringListNode newNode = new StringListNode(word);
            head = newNode;
            tail = head;
            return true;
        }

        StringListNode newNode = new StringListNode(word);
        tail.next = newNode;
        tail = tail.next;

        return true;
    }
    
    public void print(){
        StringListNode curr = head;
        while(curr != null){
            System.out.print(curr.val + "->");
            curr = curr.next;
        }
        System.out.println();
    }

    public boolean contains(String word){
        StringListNode curr = head; //curr has a scope inside this method only, this does not relate to curr in other methods
        
        while(curr != null){
            if(curr.val.equals(word)){
                return true;
            }
            curr = curr.next; 
        }
        return false;
    }

    public int size(){
        StringListNode curr = head;
        counterSize = 0;

        while(curr != null){
            counterSize += 1;
            curr = curr.next;
        }

        return counterSize;
    }
}