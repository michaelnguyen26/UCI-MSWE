import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CountFirstLetters implements FrequencyCount {

    public List<Map.Entry<String, Integer>> sort(HashMap<String, Integer> wordcheck) {
        // Source from Crista
        List<Map.Entry<String, Integer>> listwords;

        Set<Map.Entry<String, Integer>> set = wordcheck.entrySet(); // get key and values from hash map - Michael N.
        listwords = new ArrayList<Map.Entry<String, Integer>>(set); // list of key and values from hash map - Michael N.
        Collections.sort(listwords, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // sort list from greatest to least - Michael N.
            }
        });
        // End Source

        return listwords;
    }

    public void countLetters(HashMap<String, Integer> listwords) {
        HashMap<Character, Integer> letter = new HashMap<>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        ArrayList<Character> alphabetList = new ArrayList<>();
        for(int i = 0; i < alphabet.length; i++){
            alphabetList.add(alphabet[i]);
        }

        for (Map.Entry<String, Integer> val : listwords.entrySet()) {
            String lower = val.getKey().toLowerCase();
            Integer check = letter.get(lower.charAt(0)); // check if the word is in the hash map

            if (check == null) {
                letter.put(lower.charAt(0), 1);
            } else{ // get words with two characters or more
                letter.put(lower.charAt(0), letter.get(lower.charAt(0)) + 1);
            }
        }

        System.out.println("");
        System.out.print("-----------------------------");
        System.out.println("\nLetter Count for Unique Words");
        System.out.println("-----------------------------");

        for (Character ch : alphabetList) {
            System.out.println(ch + ": " + letter.get(ch));
        }
    }

    public void printResult(List<Map.Entry<String, Integer>> listwords) {

        System.out.println("");
        System.out.print("--------------");
        System.out.println("\nFrequency List");
        System.out.println("--------------");
        int count = 0;

        for (Map.Entry<String, Integer> val : listwords) {
            if (count < 25) { // get the top 25 words
                System.out.println(val.getKey() + " - " + val.getValue());
                count++;
            }
        }
        System.out.println("");
    }

    public void printResultWithZ(List<Map.Entry<String, Integer>> listwords) {

        System.out.println("");
        System.out.print("---------------------------------------");
        System.out.println("\nFrequency List (Non-Stop Words with Z)");
        System.out.println("---------------------------------------");
        int count = 0;

        for (Map.Entry<String, Integer> val : listwords) {
            if (count < 25) { // get the top 25 words
                System.out.println(val.getKey() + " - " + val.getValue());
                count++;
            }
        }
        System.out.println("");
    }

}
