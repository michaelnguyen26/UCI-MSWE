public class TrafficController {
    private volatile boolean checkIfCarIsPresent; //volatile makes this variable seen to all threads, should be used on mutable variables like this boolean var

    public synchronized void enterLeft(){
        while(checkIfCarIsPresent){
           try { 
                wait(); //the wait method must be in a synchronized block and try catch block, it pauses the current thread that is running until a notify() or notifyAll()
            } 
            catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        checkIfCarIsPresent = true; //if car not present, then make it true so the other threads can know

    }
    public synchronized void enterRight() {
        while(checkIfCarIsPresent){
            try{ 
                wait();
            } 
            catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        checkIfCarIsPresent = true;
    }
    public synchronized void leaveLeft(){
        checkIfCarIsPresent = false; //If car leaves, set the flag to false and notify all threads to wake up and acquire the lock 
        notifyAll();
    }
    public synchronized void leaveRight() {
        checkIfCarIsPresent = false;
        notifyAll();
    }

}