//References: https://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html
            //https://tutorialspoint.dev/language/java/gfact-51-java-scanner-nextchar

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;

public class Main{

    public static LocalTime getTime(){
        LocalTime time = LocalTime.now();
        return time;
    }

    private static class runnableThread implements Runnable{ //Create a runnable class for a thread
        LocalTime currentTime;
        
        public void run(){
            
            while(true){ //keep looping through the thread and ping the time every 2 seconds
                currentTime = getTime();
                Long threadID = Thread.currentThread().getId();
                System.out.printf("Hello World! I'm thread %d. The time is %tT\n", threadID, currentTime); //%tT formats the time

                try{
                    Thread.sleep(2000); //sleep always needs to be in a try catch statment
                }catch(InterruptedException e){
                    System.out.printf("Thread %d Interrupted!\n", threadID);
                    return;
                }
            }
        }
    }
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        HashMap<Long, Thread> myMap = new HashMap<>();
        Thread t;

        while(true){
            System.out.println();
            System.out.println("Here are your options: ");
            System.out.println("a) Create a new thread");
            System.out.println("b) Stop a given thread");
            System.out.println("c) Stop all threads and exit this program\n");
            char choice = Character.valueOf(input.next().charAt(0)); //next() function returns the next token/word in the input as a string and charAt(0) function returns the first character in that string.

            if(choice == 'a'){
                //create a new object t in memory each time Thread t is executed

                t = new Thread(new runnableThread()); //runnableThread is static so I do not have to do new Main().new runnableThread() 
                t.start(); //create and start on the new thread, if used .run() --> this will run on the current thread main    
                myMap.put(t.getId(), t);   //dont need to call an instance of currentThread() since currentThread() is a static method
            }
            else if(choice == 'b'){
                //interrupt a specific thread here

                Long idKill = input.nextLong(); //get id to kill in the thread      
                Thread interruptThread = myMap.get(idKill); //interrupt the thread at given id
                interruptThread.interrupt();
            }
            else if(choice == 'c'){
                //insert kill threads here

                Thread killThread;
                for(Long i: myMap.keySet()){ //for each long thread id, kill each thread
                    killThread = myMap.get(i);
                    killThread.interrupt();
                }
                break;
            }
        }
        input.close();
    }
}
