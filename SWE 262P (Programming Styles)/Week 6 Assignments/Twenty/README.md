$ cd framework
$ javac *.java
$ jar cfm framework.jar manifest.mf *.class
$ cd ../Plugin1
$ javac -cp ../framework/framework.jar *.java
$ jar cf app.jar *.class
$ cd ../deploy
$ cp ../framework/*.jar ../Plugin1/*.jar .
$ java -jar framework.jar 
