import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Stream;

public class JarClasses {

  // Source from Crista //
  private static List<String> findAllClassesInJar(JarFile jar) {
      Stream<JarEntry> stream = jar.stream();

      return stream
        .filter(entry -> entry.getName().endsWith(".class"))
        .map(entry -> getFQN(entry.getName()))
        .sorted()
        .collect(Collectors.toList());
  }

  private static String getFQN(String resourceName) {
      return resourceName.replaceAll("/", ".").substring(0, resourceName.lastIndexOf('.'));
  }
  // End Source from Crista //


  // My helper function
  private static void getMethodSignature(Method m){
    /* Check if Methods are public, static, private, or protected
    * and since a method has multiple fields the "else if" clases is
    * emitted
    */
    if(Modifier.isPublic(m.getModifiers())){;
      publicMethod++;
    }
    if(Modifier.isStatic(m.getModifiers())){
      staticMethod++;
    }
    if(Modifier.isPrivate(m.getModifiers())){
      privateMethod++;
    }
    if(Modifier.isProtected(m.getModifiers())){
      protectedMethod++;
    }
  }

  // Helper function to reset counts
  private static void resetCount(){
    fieldsCount = 0;
    publicMethod = 0;
    privateMethod = 0;
    protectedMethod = 0;
    staticMethod = 0;
  }

  // Declare Counters for Methods and Fields
  static int fieldsCount = 0;
  static int publicMethod = 0;
  static int privateMethod = 0;
  static int protectedMethod = 0;
  static int staticMethod = 0;

  /* Color Text for Readability in the Console
  * Source: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
  */
  public static final String CYAN_BOLD = "\033[1;36m"; 
  public static final String RESET = "\033[0m"; // reset color so text is consistent
  

  public static void main(String[] args) {
    
    // Source from Crista
    String jarFile = args[0];
    JarFile jar = null;
    ClassLoader cl = null;

    // Use URL Class to Load in the Jar File
    try {
      File file = new File(jarFile); // command line argument jar file --> file object

      URL url = file.toURI().toURL(); // convert file to a uniform resource locator
      URL[] urls = new URL[]{url}; // an array of urls from url --> url to my jar file

      cl = new URLClassLoader(urls); // create new class loader based on the url (the jar file)
    } catch (MalformedURLException e) { System.out.println(e); }



    try {
      jar = new JarFile(jarFile); // jar file reader
      List<String> classNames = findAllClassesInJar(jar); // get class names from jar file reader and sort in alphabetical order
      System.out.println();
      
      for (String name : classNames) {
        Class<?> cls = cl.loadClass(name);
     // End Source from Crista

        System.out.println("----------" + CYAN_BOLD + name + RESET + "----------");

        if (cls != null) {
          Method[] methods = cls.getDeclaredMethods();
          for (Method m: methods){
            getMethodSignature(m); // helper function to get method signatures if public, private, etc.
          }

          System.out.println("Public Methods: " + publicMethod);
          System.out.println("Private Methods: " + privateMethod);
          System.out.println("Protected Methods: " + protectedMethod);
          System.out.println("Static Methods: " + staticMethod);

          resetCount(); // reset global counts

          Field[] fields = cls.getDeclaredFields();
          for (Field f: fields){
            fieldsCount++;
          }
          System.out.println("Fields: " + fieldsCount);         
       }
      }
      System.out.println();
    } catch (IOException e) {
      System.out.println();
      System.out.println(e);
    }catch(ClassNotFoundException e){
      System.out.println();
      System.out.println(e);
    }
  }
}