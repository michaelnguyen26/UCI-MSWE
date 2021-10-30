import java.util.concurrent.*;

public class Main3 {

   private static void nap(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void addProc(HighLevelDisplay d) {

        for(int i=0; i <= 20; i++) {
            d.addRow("Thread A:  " + i);
            nap(500);
            d.addRow("Thread B:  " + i);
            nap(500);
            
        }

   }

    private static void deleteProc(HighLevelDisplay d) {
        for(int i = 0; i < 80; i++){
            d.deleteRow(0);
            nap(500);
        }     
    }

    public static void main(String [] args) {
        final HighLevelDisplay d = new JDisplay2();

        new Thread () {
            public void run() {
                addProc(d);
            }
        }.start();


        new Thread () {
            public void run() {
                deleteProc(d);
            }
        }.start();

    }
}