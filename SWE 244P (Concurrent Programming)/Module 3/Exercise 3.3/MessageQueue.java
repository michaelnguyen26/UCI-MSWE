import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Scanner;

public class MessageQueue {
    private static int n_ids;

    public static void main(String[] args) {
	BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
	Scanner input = new Scanner(System.in);
	
	//Part 2 & 3
	// Producer p1 = new Producer(queue, n_ids++);
	// Consumer c1 = new Consumer(queue, n_ids++);
	// new Thread(p1).start();
	// new Thread(c1).start();

	// Consumer c2 = new Consumer(queue, n_ids++);
	// Producer p2 = new Producer(queue, n_ids++);
	// new Thread(c2).start();
	// new Thread(p2).start();

	//Part 4
	System.out.print("Enter Consumers: ");
	int valConsumers = Integer.valueOf(input.nextLine());

	System.out.println();
	System.out.print("Enter Producers: ");
	int valProducers = Integer.valueOf(input.nextLine());

	CopyOnWriteArrayList<Producer> producerList = new CopyOnWriteArrayList<>(); //create a synced arraylist
	

	for(int j = 0; j < valProducers; j++){ //create the number of producers in a list
		Producer producer = new Producer(queue, n_ids);
		producerList.add(producer);
	}

	for(Producer startProducer: producerList){ //for each producer, start them in the list
		new Thread(startProducer).start();
	}
	
	for(int i = 0; i < valConsumers; i++){ //create the number of consumers and start them
		Consumer consumer = new Consumer(queue, n_ids++);
		new Thread(consumer).start();
	}
	//END PART 4
	
	try {
	    Thread.sleep(10000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	int diff = valConsumers - valProducers; //problem arises when consumer>producer, so get the diff and stop each consumer>producer

	if(diff > 0){
		for(int throttleConsumer = 0; throttleConsumer < diff; throttleConsumer++){ //for each consumer greater than the producer, stop the consumer
			try {
				queue.put(new Message("stop")); //stop the queue with stop since consumer stops when "stop" is read
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	for(Producer stopProducer: producerList){ //for each producer stop the producer
		stopProducer.stop();
	}
	// p1.stop();
	// p2.stop();
	input.close();
    }
}