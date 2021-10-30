/*
 * @author Crista Lopes
   @edited for concurrency by Michael Nguyen
 * Simple word frequency program
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class FrequencyCountAnotherApproach {
    private static final List<String> stop_words = new ArrayList<String>();

    static final class Counter {
		private HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
		static int N_CPUS = Runtime.getRuntime().availableProcessors(); //get the number of cpus for num of threads

		private void process(Path filepath) {
			final ExecutorService exec = Executors.newFixedThreadPool(N_CPUS); //create a fixed thread pool with this number of cpus
		
			try {
				try (Stream<String> lines = Files.lines(filepath /*Paths.get(filename)*/ )) {
					lines.forEach(line -> { exec.execute(new Runnable(){ //execute the threadpool with Runnable()
							public void run(){
								process(line); //define abstract method run to process this statement for each thread
							}
						});
					});
				}
				exec.shutdown(); //do an orderly shutdown on the executor
				try{
					exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS); //wait until all tasks are completed
				}catch (InterruptedException e){
					return;
				}
			} catch (IOException e) {
					e.printStackTrace();
				}
		}

		// Keep only the non stop words with 3 or more characters

//Synchornize this method since put and add need to be accessed one at a time for visibility and mutual exclusion
		private synchronized void process(String line){  
			String[] words = line.split("\\W+");
			for (String word : words) {
				String w = word.toLowerCase();
				if (!stop_words.contains(w) && w.length() > 2) {
					if (frequencies.containsKey(w))
					frequencies.put(w, frequencies.get(w)+1);
					else
					frequencies.put(w, 1);
				}
			}
		}

		private List<Map.Entry<String, Integer>> sort() {
			Set<Map.Entry<String, Integer>> set = frequencies.entrySet();
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
												set);
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1,
						Map.Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
				}
			});
			return list;
		}

		private HashMap<String, Integer> getFrequencies() { 
			return frequencies; 
		}

		public void merge(Counter other) {
			other.getFrequencies().forEach((k, v) -> frequencies.merge(k, v, Integer::sum));
		}

		// Only the top 40 words that are 3 or more characters
		public String toString() {
			List<Map.Entry<String, Integer>> sortedMap = sort();
			StringBuilder sb = new StringBuilder("---------- Word counts (top 40) -----------\n");
			int i = 0;
			for (Map.Entry<String, Integer> e : sortedMap) {
			String k = e.getKey();
			sb.append(k + ":" + e.getValue() +"\n"); 
			if (i++ > 40)
				break;
			}
			return sb.toString();
		}

    }

		private static void loadStopWords() {
			String str = "";
			try {
				byte[] encoded = Files.readAllBytes(Paths.get("stop_words"));
				str = new String(encoded);    
			} catch (IOException e) {
				System.out.println("Error reading stop_words");
			}
			String[] words = str.split(",");
			stop_words.addAll(Arrays.asList(words));
		}

		private static void countWords(Path p, Counter c) {
			System.out.println("Started " + p);
			c.process(p);
			System.out.println("Ended " + p);
    	}

    public static void main(String[] args) {

		loadStopWords();
		Counter c = new Counter();

		long start = System.nanoTime();
		try {
			try (Stream<Path> paths = Files.walk(Paths.get("."))) {
				paths.filter(p -> p.toString().endsWith(".txt")).forEach(p -> countWords(p, c));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();

		long elapsed = end - start;

		System.out.println(c);
		System.out.println("Elapsed time: " + elapsed/1000000 + "ms");

    }
}
