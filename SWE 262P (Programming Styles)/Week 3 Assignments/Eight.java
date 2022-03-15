import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Eight {

    /* RUN java -Xss250M Eight.java "pride-and-prejudice.txt" to set the recursion limit */

    static ArrayList<String> recurse(FileReader f, String word, ArrayList<String> read_words, ArrayList<String> stop_words) throws IOException{
        int ch = f.read();
        if(ch == -1){
            return read_words; // base case for ending file parsing
        }
        
        char c = (char) ch;

        while(Character.isLetter(c)){ // character has to be letter only, no spaces or punctuation

            if((ch  >= 48 && ch  <= 57) || (ch  >= 65 && ch  <= 90) || (ch  >= 97 && ch  <= 122)){ // if it is a letter
                word += c;   
            }
            ch = f.read();

            if(ch == -1){ // need to check if end of file is read otherwise there is an infinite loop with -1
                if(!stop_words.contains(word.toLowerCase()) && word.length() >= 2){
                    read_words.add(word.toLowerCase());
                }
                return read_words;
            }

            c = (char) ch;
        }

        if(!stop_words.contains(word.toLowerCase()) && word.length() >= 2){
            read_words.add(word.toLowerCase());
        }
       
        word = ""; // reset word if it's not a char or digit and it's not a word from stop_list and word is > 2
        recurse(f, word, read_words, stop_words); // recursion step
        
        return read_words;   
    }
  public static void main(String[] args) {
      
      /* RUN IN SHELL COMMAND LINE */

      ArrayList<String> stop_list = new ArrayList<>();
      ArrayList<String> empty_word_list = new ArrayList<>();
      ArrayList<String> final_list = new ArrayList<>();

      try {
          FileReader reader = new FileReader(args[0]);
          String stop = new String(Files.readAllBytes(Paths.get("stop_words.txt")));
          Collections.addAll(stop_list, stop.toLowerCase().split(","));

          String w = "";
          final_list = recurse(reader, w, empty_word_list, stop_list);

          System.out.print("\n--------------");
          System.out.println("\nFrequency List");
          System.out.println("--------------");

          // From Seven.java to shorten the code length
            final_list.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .sorted((o1,o2) -> o2.getValue().compareTo(o1.getValue())) // sort list from greatest to least - Michael N.
            .limit(25) // filter already applied in recursion, so limit to 25 words
            .forEach(s -> {
                if(s.getKey().length() >= 2){
                    System.out.println(s.getKey() + " - " + s.getValue());
                }
            });         

      } catch (IOException e) {
          System.out.println(e.getMessage());
      } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("\nMessage:\n\nPlease enter the path to the .txt files in the command line as: \n\n" + 
          "               java -Xss250M Eight.java pride-and-prejudice.txt");
      }
  }
}