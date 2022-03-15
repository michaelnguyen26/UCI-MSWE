import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Streams {
    public static void main(String[] args) {

        ArrayList<String> stop_list = new ArrayList<>();
        ArrayList<String> w_list = new ArrayList<>();

        try {
            // read entire file and add to lists in a single line --> readAllBytes from directory
            String stop = new String(Files.readAllBytes(Paths.get("stop_words.txt")));
            Collections.addAll(stop_list, stop.toLowerCase().split(","));
            String w = new String(Files.readAllBytes(Paths.get(args[0])));
            Collections.addAll(w_list, w.toLowerCase().split("[^a-zA-Z0-9]+"));
        } catch (IOException e) {
            System.out.println(e);
        }

        // remove all stop words in one line from the word list
        w_list.removeAll(stop_list);

        System.out.println("");
        System.out.print("--------------");
        System.out.println("\nFrequency List");
        System.out.println("--------------");
        
        w_list.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .sorted((o1,o2) -> o2.getValue().compareTo(o1.getValue())) // sort list from greatest to least - Michael N.
            .limit(26) // filter out words less than 1, so increment to 26
            .forEach(s -> {
                if(s.getKey().length() >= 2){
                    System.out.println(s.getKey() + " - " + s.getValue());
                }
            });
    }
}