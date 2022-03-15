import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {
  public static void main(String[] args) {
      
      /* RUN IN SHELL COMMAND LINE */

      ArrayList<String> list = new ArrayList<>();
      HashMap<String, Integer> wordcheck = new HashMap<>();

      try {
          File file = new File(args[0]);
          File stopwords = new File("stop_words.txt");
          Scanner reader = new Scanner(file);
          Scanner stop = new Scanner(stopwords);

          while (stop.hasNext()) {
              String[] stoplist = stop.nextLine().toString().split(",");
              for (int i = 0; i < stoplist.length; i++) {
                  list.add(stoplist[i]);
              }
          }

          // Check if stop words are added to the list correctly
          // for(int i = 0; i < list.size(); i++){
          // System.out.println(list.get(i));
          // }

          while (reader.hasNext()) {
              String[] list_of_words = reader.nextLine().split("[^a-zA-Z0-9]+"); // regex: get everything that is not
                              // a-z, A-Z, and 0-9 (+ means one or
                              // more occurences)

              for (String word : list_of_words) {
                  String lower = word.toLowerCase();
                  Integer check = wordcheck.get(lower); // check if the word is in the hash map

                  if (check == null) {
                      wordcheck.put(lower, 1);
                  } else if (!(list.contains(lower)) && lower.length() >= 2) { //get words with two characters or more
                      wordcheck.put(lower, wordcheck.get(lower) + 1);
                  }
              }
          }

          // Source from Crista
          Set<Map.Entry<String, Integer>> set = wordcheck.entrySet(); // get key and values from hash map - Michael N.
          List<Map.Entry<String, Integer>> listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map - Michael N.
          Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());  // sort list from greatest to least - Michael N.
            }
          });
          // End Source

          int count = 0;
          
          System.out.println("");
          System.out.print("--------------");
          System.out.println("\nFrequency List");
          System.out.println("--------------");

          for (Map.Entry<String, Integer> val : listwords) {
              if(count < 25){ // get the top 25 words
                  System.out.println(val.getKey() + " - " + val.getValue());
                  count++;
              }
          } 
          System.out.println("");

          stop.close();
          reader.close();

      } catch (NullPointerException e) {
          System.out.println(e.getMessage());
      } catch (FileNotFoundException e) {
          System.out.println(e.getMessage());
      } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("\nMessage:\n\nPlease enter the path to the .txt files in the command line as: \n\n" + 
          "               java Main.java pride-and-prejudice.txt");
      }
  }
}