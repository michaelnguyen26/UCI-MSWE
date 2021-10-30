import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExerciseM2 {
    public static void main(String[] args) {

        ExecutorService exec = Executors.newFixedThreadPool(10);
        System.out.println();

        for (String arg : args) { // iterate through files concurrently with the command line argument
            exec.execute(new Runnable() {
                public void run() {
                    System.out.println("Thread_" + Thread.currentThread().getId() + " is running " + arg);
                    FileReader inputStream;
                    int countline = 0;
                    BufferedReader input;

                    countline = 0; // reset counter
                    try {
                        inputStream = new FileReader(arg); // read file from command line argument
                        input = new BufferedReader(inputStream); // call the read .txt file into a bufferedreader
                        String line = input.readLine(); // read a line from the buffer

                        while (line != null) {
                            countline++; // increase line count
                            line = input.readLine(); // read next line
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println(arg + ": " + countline); // print results
                }
            });

        }
        exec.shutdown(); // shutdown threads when for loop is finished
        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS); // wait until all threads are finished and
                                                                     // terminate
        } catch (InterruptedException e) {
            e.getMessage();
        }
    }
}