import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

// Source from Crista
class Main {
  public static void main(String[] args) {

    // Global Variables for the "for" loops
    HashMap<String, Integer> processStopWords = null;
    boolean checkForZ = false;
    boolean checkForCount = false;

    // Get Config File
    Properties prop = new Properties();
    String fileName = "C:/Users/nguye/Desktop/UC Irvine (Graduate)/SWE 262P/Week 6/Assignments/Twenty/framework/frequency.config";

    try (FileInputStream fis = new FileInputStream(fileName)) {
      prop.load(fis);

    } catch (FileNotFoundException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }


    String path = prop.getProperty("jarPath");
    String[] jar = prop.getProperty("jarName").split(",");

    // End of Global Variables for the "for" loops

    // Extract Words Plugin
    System.out.println();
    System.out.print("Enter a class name to extract words \"NormalExtraction, WordsWithZ\": ");
    Scanner in = new Scanner(System.in);
    String extractName = in.nextLine();

    System.out.println();
    System.out.print("Enter a class name to count words \"CountFrequency, CountFirstLetters\": ");
    String countName = in.nextLine();
    in.close();

    String jarExtract = null;
    String jarCount = null;

    if (extractName.equals("NormalExtraction")) {
      jarExtract = jar[0];
    } else if (extractName.equals("WordsWithZ")) {
      jarExtract = jar[1];
      checkForZ = true;
    }

    if (countName.equals("CountFrequency")) {
      jarCount = jar[2];
    } else if (countName.equals("CountFirstLetters")) {
      jarCount = jar[3];
      checkForCount = true;
    }

    System.out.println(
        "\nLoading and instantiating " + "Plugin " + extractName.strip() + " from .config file" + "...");

    Class cls = null;
    URL classUrl = null;
    try {
      // Find classses in the given jar file
      classUrl = new URL(path + jarExtract.strip());
    } catch (Exception e) {
      e.printStackTrace();
    }
    URL[] classUrls = { classUrl };
    URLClassLoader cloader = new URLClassLoader(classUrls);
    try {
      cls = cloader.loadClass(extractName.strip());
    } catch (Exception e) {
      e.printStackTrace();
    }
    // End Source from Crista

    if (cls != null) {
      try {
        FrequencyExtract freq = (FrequencyExtract) cls.newInstance();
        Scanner reader = freq.readFile(args[0]);
        HashMap<String, Integer> processWord = freq.processWord(reader);
        processStopWords = freq.processStopWords(processWord);
      } catch (Exception e) {
        processStopWords = null;
        e.printStackTrace();
      }
    }

    // Count Words Plugin (Count the Frequency as an inner loop for each extraction
    // above!)

    System.out.println(
        "\nLoading and instantiating " + "Plugin " + countName.strip() + " from .config file" + "...");

    Class countCls = null;
    URL countClassUrl = null;
    try {
      // Find classses in the given jar file
      countClassUrl = new URL(path + jarCount.strip()); // STRIP IS NEEDED TO REMOVE SPACE FROM THE NAME
    } catch (Exception e) {
      e.printStackTrace();
    }
    URL[] countClassUrls = { countClassUrl };
    URLClassLoader countCloader = new URLClassLoader(countClassUrls);
    try {
      countCls = countCloader.loadClass(countName.strip()); // STRIP IS NEEDED TO REMOVE SPACE FROM THE NAME
    } catch (Exception e) {
      e.printStackTrace();
    }
    // End Source from Crista

    if (countCls != null) {
      try {

        if (checkForZ && checkForCount) {
          FrequencyCount countFreq = (FrequencyCount) countCls.newInstance();
          countFreq.countLetters(processStopWords);
          checkForCount = false;
          checkForZ = false;
        }

        else if (checkForZ) {
          FrequencyCount countFreq = (FrequencyCount) countCls.newInstance();
          List<Map.Entry<String, Integer>> result = countFreq.sort(processStopWords);
          countFreq.printResultWithZ(result);
          checkForZ = false;
        }

        else if (checkForCount) {
          FrequencyCount countFreq = (FrequencyCount) countCls.newInstance();
          countFreq.countLetters(processStopWords);
          checkForCount = false;
        } else {
          FrequencyCount countFreq = (FrequencyCount) countCls.newInstance();
          List<Map.Entry<String, Integer>> result = countFreq.sort(processStopWords);
          countFreq.printResult(result);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    // End of Count Words Plugin
  }
}