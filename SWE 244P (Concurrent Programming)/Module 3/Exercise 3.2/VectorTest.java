import java.util.Vector;

public class VectorTest {

    private volatile boolean running = true; //make running visible to all threads
    private volatile Vector<String> people = new Vector<String>(); //make people visible to all threads happen-before

    private synchronized void addPerson() { //synchornize this method so it cant add at the same time as the other methods
	people.add(RandomUtils.randomString());
    }

    private synchronized String getLast() { //synchornize this method so it cant print at the same time as the other methods
	int lastIndex = people.size() - 1;
	if (lastIndex >= 0)
	    return people.get(lastIndex);
	else return "";
    }

    private synchronized void deleteLast() { //synchornize this method so it cant delete a word being printed or added 
	int lastIndex = people.size() - 1;
	if (lastIndex >= 0)
	    people.remove(lastIndex);
    }

    public void run() {
	// Start getter thread
	new Thread(new Runnable () {
		public void run() {
		    Thread.currentThread().setName("Getter");
		    while (running) {
			String person = getLast();
			System.out.println("Last: " + person);
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
			deleteLast();
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
		Thread.sleep(200);
	    } catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}
	running = false;
    }

    public static void main(String[] args) {
	VectorTest hm = new VectorTest();
	hm.run();
    }

}
