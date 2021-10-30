import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

//REFERENCE: https://docs.oracle.com/javase/7/docs/api/java/nio/file/DirectoryStream.html
           //https://tutorials.jenkov.com/java-nio/path.html
           //https://web.mit.edu/6.031/www/sp19/classes/23-sockets-networking/
           //https://users.cs.duke.edu/~chase/cps196/slides/sockets.pdf

public class ServerTCP {
    public final static int PORT = 8010;

    public static void startServer(Path directory){
                ExecutorService exec = Executors.newFixedThreadPool(50); //create a threadpool with 50 threads

                exec.execute(new Runnable(){
                    public void run(){
                        try{
                            ServerSocket server = new ServerSocket(PORT);   //create a server from the ServerSocket class that listens on port 21
                            System.out.println("\nServer listening for a client request on Port " + server.getLocalPort() + "...");   
                            
                            //This socket is used to receive data and write data TO the client from this SERVER socket
                            Socket socketConnection = server.accept(); 
                            System.out.println("Client connected to server!");   
                            
                            //Create a reader and writer object for the SERVER to receive requests from the client and write to the client
                            BufferedReader readFromClient = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));   //read from client through server socket
                            PrintWriter writeToClient = new PrintWriter(new BufferedOutputStream(socketConnection.getOutputStream()), true);    //write to client from server socket (true will autoflush)
        
                            while(true){            
                                String clientMessage = readFromClient.readLine();    //read from client input stream
                    
                                if(clientMessage.equals("index")){     
                                    try (DirectoryStream<Path> textFiles = Files.newDirectoryStream(directory, "*.txt")){    //get a directory stream of all the files ending in .txt
                                        for (Path txt: textFiles){     
                                            writeToClient.println("\n" + txt.getFileName().toString());     //write text files name as a string to the client
                                        }
                                        writeToClient.println("EOF"); //write EOF to the client when done looping through the files to indicate completion
                                    } catch (IOException e) {
                                        System.out.println(e);
                                    }
                                }else if(clientMessage.contains("get ")){   //check if input contains get and " "
                                    String textFile = clientMessage.split(" ")[1];   
                                    Path txtDirectory = Paths.get(directory + "/" + textFile); /

                                    // System.out.println(txtDirectory.toString()); --> test to see if directory appends correctly
                                    
                                    if(!(Files.exists(txtDirectory))){ // error handling: check if file exists
                                        writeToClient.println("err");
                                        writeToClient.println("Error: No such file");
                                        continue;
                                    }

                                    writeToClient.println("\n\nok\n");
                                    Scanner reader = new Scanner(txtDirectory);
                                    while(reader.hasNext())
                                    {
                                        writeToClient.println(reader.nextLine());
                                    }

                                    //SECOND WAY TO READ THE TEXT FILE:
                                    // for(String listOfData: Files.readAllLines(txtDirectory)){ //Files.readAllLines reads all lines from a file, when paired with for-each it will read each line one by one 
                                    //     writeToClient.println(listOfData);
                                    // } 
                                           
                                    writeToClient.println("\nServer Response: Thank you, server disconnected!"); //dont need to call sysout in client since this will be in the input stream from the for loop
                                    reader.close();            
                                    writeToClient.close();
                                    readFromClient.close();
                                    server.close();
                                }
                            }      
                        }catch (IOException e){
                            System.out.println(e.getMessage());
                        }               
                    }
                }); 
                exec.shutdown();
                try{
                    exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                }catch(InterruptedException e){
                    System.out.println(e.getMessage());
                } 
            }

    public static void main(String[] args){
        int fileDirectory = args.length; 
        if(fileDirectory < 1){ //if no args is given then thrown an error and exit the program
            System.out.println("\nCommand Line Error: You need to enter a valid directory in the command line (args).");
            return;
        }

        Path directoryPath = Paths.get(args[0]); //args already provides a single quote (") at the beginning. Use quotes for args with spaces
        // System.out.println(directoryPath);

        if(!(Files.exists((directoryPath)))){ //check if folder exists
            System.out.println("Directory Error: Directory or data folder is missing");
            return;
        }
        startServer(directoryPath);
    }
}
