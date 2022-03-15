import java.util.HashMap;
import java.util.Scanner;

public interface FrequencyExtract {
    Scanner readFile(String args);

    HashMap<String, Integer> processStopWords(HashMap<String, Integer> listwords);

    HashMap<String, Integer> processWord(Scanner reader);

}
