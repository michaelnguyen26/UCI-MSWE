import java.util.HashMap;
import java.util.Vector;

public class HashMapTest {

    private volatile boolean running = true; //make running visible to the other threads
    private volatile HashMap<String, Integer> people = new HashMap<String, Integer>();

    private synchronized void addPerson() {
		people.put(RandomUtils.randomString(), RandomUtils.randomInteger());
    }

    private synchronized void deletePeople(String pattern) { //synchornize so this method cant delete while another thread is printing
		Vector<String> hasPattern = new Vector<String>();
		for (String key : people.keySet()) {
			if (key.contains(pattern))
			hasPattern.add(key);
		}
		for (String key : hasPattern)
			people.remove(key);
    }

    private synchronized void printPeople() { //synchornize so this method cant print a person being deleted 
		for (HashMap.Entry<String, Integer> entry : people.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		System.out.println("-----------------------------------------");
    }

    public void run() {
	// Start printer thread
	new Thread(new Runnable () {
		public void run() {
		    Thread.currentThread().setName("Printer");
		    while (running) {
				printPeople();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
		    }
		}
	    }).start();

	// Start deleter thread
	new Thread(new Runnable () {
		public void run() {
		    Thread.currentThread().setName("Deleter");
		    while (running) {
				deletePeople("a");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
		    }
		}
	    }).start();

	// This thread adds entries
	for (int i = 0; i < 100; i++) {
	    addPerson();
	    try {
			Thread.sleep(500);
	    } catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}
	running = false;
    }

    public static void main(String[] args) {
		HashMapTest hm = new HashMapTest();
		hm.run();
    }

}
