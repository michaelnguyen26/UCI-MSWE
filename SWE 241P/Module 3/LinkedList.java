class ListNode {
    int val;
    ListNode next; //next is a ListNode reference, but since no object is created, it has a null reference. 

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class LinkedList{
    ListNode head; //Create a reference variable head that references ListNode, but no object is created.
    ListNode tail; 

    public LinkedList() {
        head = null; //Reference vairables can only be null. Therefore, assign head a "null" reference in the construtor method
        tail = null;
    }


    public void add(int value){
        if(head == null){
            head = new ListNode(value); //head has a value and a next value of null
            tail = head; //Tail references the same address as head!
            if(tail == head){
                System.out.println("\nReference variables are the same!");
            }
        }else{
            tail.next= new ListNode(value);
            tail = tail.next;
        }
    }

    public void print(ListNode head) {
        ListNode current = head;
        System.out.print("My Linked List: ");
        while (current != null) {
            System.out.print(current.val + " ");
            current = current.next;
        }
        System.out.println();
    }
    
    public ListNode reverse(ListNode head){
        ListNode prev = null; 
        ListNode current = head;
        ListNode p;


        if(head == null){ //check if there are no values in the linked list
            return null;
        }

        if(head.next == null){ //check if the next value after the head exists or not, if it doesn't return the head
            return head;
        }

        //While(the next node in the linked list is not null){
            //reverse the direction of the arrows by referencing the head node null first
            //keep the unlinked nodes together and reverse there direction one by one
            //at the last node it will equal null, so link the node to the previous value when the loop ends
        //}

        while(current != null){
            p = current.next;  //2 (p is the actual object)
            current.next = prev; //null (attribute of current)
            prev = current; 
            current = p;   
        }
        return prev;
    }

    public int findMiddle(ListNode head){
        int counter = 0;
        int checkpoint = 0;
        ListNode current = head;
        
        while(current != null){//Find the size of the linked list
            counter += 1;
            current = current.next;
        }

        current = head; //Reinitialize the current to be head again
        while(checkpoint < counter/2){//If I am not at the halfway point, then keep traversing the linked list
            checkpoint += 1;
            current = current.next;       
        }
        return current.val;
    }
    
    public static void main(String[] args) {
        LinkedList myLinkedList = new LinkedList();

        for(int i = 1; i <= 14; i++){
            myLinkedList.add(i);
        }

        myLinkedList.print(myLinkedList.head);
        ListNode reverseList = myLinkedList.reverse(myLinkedList.head);
        myLinkedList.print(reverseList);
        // myLinkedList.reverse(reverseList);
        // myLinkedList.print(myLinkedList.head);
        // System.out.println(myLinkedList.findMiddle(myLinkedList.head));
    }
}