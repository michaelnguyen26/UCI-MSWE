import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FrequencyCount {
    List<Map.Entry<String,Integer>> sort(HashMap<String, Integer> wordcheck);

    void printResult(List<Map.Entry<String, Integer>> listwords);

    void printResultWithZ(List<Map.Entry<String, Integer>> listwords);

    void countLetters(HashMap<String, Integer> listwords);
}
