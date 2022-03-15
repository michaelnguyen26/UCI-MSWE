import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Four {
  public static void main(String[] args) {

    /*
     * RUN IN SHELL COMMAND LINE USING:
     * cd Week2
     * java Four.java "/home/runner/Exercises/pride-and-prejudice.txt"
     */

    /*
     * OR RUN IN SHELL COMMAND LINE USING:
     * cd Week2
     * java Four.java ../pride-and-prejudice.txt
     */

    ArrayList<String> list_stop = new ArrayList<>();
    ArrayList<String> freq = new ArrayList<>();
    ArrayList<Integer> count_word = new ArrayList<>();

    try {
      FileReader file = new FileReader(args[0]);
      Scanner reader = new Scanner(file);
      FileReader stopwords = new FileReader("stop_words.txt");
      Scanner stop = new Scanner(stopwords);

      // split is okay here
      while (stop.hasNext()) {
        String[] stoplist = stop.nextLine().split(",");
        for (int i = 0; i < stoplist.length; i++) {
          list_stop.add(stoplist[i]);
        }
      }

      // process main text file
      int word_pair = 0;
      int indexOfWord = 0;
      int word_count = 0;

      boolean stopExists = false;
      boolean wordExists = false;


      while (reader.hasNext()) {

        String str = reader.nextLine() + " "; // add " " to get the last word in the line 
        String word = "";

        for (int i = 0; i < str.length(); i++) {
          char c = str.charAt(i);
          int asc = (int) c;
          if ((asc >= 48 && asc <= 57) || (asc >= 65 && asc <= 90) || (asc >= 97 && asc <= 122)) {
            word += c;
          } else if (c == ' ' || word != ""){ // word found or reached the end of a line so I need to process the next word

            word = word.toLowerCase();

            for (String stop_val : list_stop) {
              if (stop_val.toLowerCase().equals(word)) {
                stopExists = true;
              }
            }
            if (!stopExists) {
              if (freq.size() != 0) {
                for (String w : freq) {
                  if (w.equalsIgnoreCase(word)) {
                    wordExists = true;
                    break;
                  }
                }
                if(wordExists) {
                  indexOfWord = freq.indexOf(word); // get index associated with the word
                  word_count = count_word.get(indexOfWord); /// get frequency associated with the word
                  count_word.set(indexOfWord, word_count + 1); // at the same index, get the frequency and increment by 1
                  word = ""; // reset word
                  wordExists = false; // reset flag for next iteration
                } else {
                  if(word.length() >= 2){
                    freq.add(word);
                    count_word.add(word_pair + 1);
                    word = ""; // reset word
                  }             
                }
              } else { // add word for the first time
                  if(word.length() >= 2){
                    freq.add(word);
                    count_word.add(word_pair + 1);
                    word = ""; // reset word
                  }            
              }
            } else {
              stopExists = false;
              word = "";
            }
            word = ""; // reset word if word.length() is not > 2
          }
        }
      }

      // SORT HERE  (Selection Sort)  
      for(int j = 0; j < freq.size(); j++){
        int max = count_word.get(j); // choose max arbitrarily

        for(int k = j + 1; k < freq.size(); k++){
          if(count_word.get(k) > max){
            max = count_word.get(k); // get the max
          }      
        }
        // sort in descending order
        int temp = count_word.get(j);
        String tempWord = freq.get(j);
        int max_index = count_word.indexOf(max);

        freq.set(j, freq.get(max_index));
        count_word.set(j, max);

        freq.set(max_index, tempWord);  
        count_word.set(max_index, temp);
      }    
      // END SORT

      System.out.println("");
      System.out.print("--------------");
      System.out.println("\nFrequency List");
      System.out.println("--------------");

      for(int i = 0; i < freq.size(); i++){
        if(i < 25){
          System.out.println(freq.get(i) + " - " + count_word.get(i));
        }
      }
      System.out.println("");    

      stop.close();
      reader.close();

    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("\nMessage:\n\nPlease enter the path to the .txt files in the command line as: \n\n" +
          "               java Four.java \"../pride-and-prejudice.txt ../stop_words.txt\"");
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}