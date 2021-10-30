import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//REFERENCE: https://www.codejava.net/java-se/networking/java-udp-client-server-program-example
           //http://tutorials.jenkov.com/java-networking/udp-datagram-sockets.html
           //http://www.java2s.com/example/java/network/udp-send-object.html
           //https://docs.oracle.com/javase/7/docs/api/java/net/DatagramSocket.html

// SECOND APPROACH:
// import java.io.ByteArrayOutputStream;
// ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
// bufferOut.write(buffer);   //write the byte[] data to a BufferedOutputStream so they can all be sent out at once
// bufferOut.reset();    //reset buffer if user asks for "index" again
// bufferOut.close(); //close buffer after "get" is called

public class ServerUDP{

    public static final int PORT = 8010;

    public static void startServer(Path directory){
        ExecutorService exec = Executors.newFixedThreadPool(50); //fixed thread pool of 50 threads
        
        exec.execute(new Runnable(){ //execute multithreaded runnable interface
            public void run(){
                try{   
                    Scanner reader;
                    String data;
                    InetAddress localAddress = InetAddress.getLocalHost(); //get local address
                    // System.out.println(localAddress.toString());

                    DatagramSocket serverSocket = new DatagramSocket(PORT, localAddress); //listen on port 8010
                    serverSocket.setSoTimeout(20000); //timeout after 20 seconds
            
                    while(true){
                        byte[] buffer = new byte[512]; //create byte array of 512 bytes 
                        DatagramPacket clientRequest = new DatagramPacket(buffer, buffer.length);
                        
                        System.out.print("\nWaiting for a client request... ");
                        serverSocket.receive(clientRequest); //wait until client request is received
                        System.out.println("Client request received!");
        
                        InetAddress sendToClientAddress = clientRequest.getAddress(); //get client address
                        int sendToClientPort = clientRequest.getPort(); //get client port
        
                        String msgRequest = new String(clientRequest.getData(), 0, clientRequest.getLength(), "ASCII"); //convert byte data to string of type ASCII
                        // System.out.println(msgRequest); //check if index is received
                        
                        //Read "index"
                        if(msgRequest.equals("index")){
                            int count = 0;
                            try (DirectoryStream<Path> textFiles = Files.newDirectoryStream(directory, "*.txt")){    //get a directory stream of all the files ending in .txt
                                for (Path txt: textFiles){     //for each text file in the directory stream, print them out
                                    data = "\n" + txt.getFileName().toString();  //store text file name in variable "data" as a string, append a \n in the beginning so the list of files are readable
                                    buffer = data.getBytes();     //break down and convert data to a byte[]
                                    DatagramPacket sendToClient = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); //pack data for client
                                    serverSocket.send(sendToClient);   //send data to client
                                    count += 1;
                                }
                                String stop = "EOF"; //append "EOF" to the buffer to indicate end of file reading
                                buffer = stop.getBytes();  //convert "EOF" to bytes
                                DatagramPacket sendToClient = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); //pack data for client
                                serverSocket.send(sendToClient);   //send data to client

                                String rel = String.valueOf("\nTotal File Count on Server: " + count); //keep track of files sent
                                buffer = rel.getBytes();
                                DatagramPacket sendRel = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); 
                                serverSocket.send(sendRel);
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                        
                        //Get contents of text file
                        }else if(msgRequest.contains("get ")){
                            String txtFile = msgRequest.split(" ")[1]; //get text file name
                            Path txtDirectory = Paths.get(directory + "/" + txtFile); //append name at the end of directory
                            
                            if(!(Files.exists(txtDirectory))){ // error handling: check if file exists
                                String response = "err";
                                buffer = response.getBytes();
                                DatagramPacket sendToClient = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); //send data to client
                                serverSocket.send(sendToClient);
                                continue;
                            }
                            
                            //write "ok" to the buffer
                            String ok = "\nok\n";
                            buffer = ok.getBytes();
                            DatagramPacket sendOk = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); //send data to client
                            serverSocket.send(sendOk); //send data to client  
        
                            reader = new Scanner(txtDirectory); //create a scanner object for the desired textfile
                            int count = 0;

                            while(reader.hasNext()){
                                String line = reader.nextLine(); //read line from textfile
                                buffer = line.getBytes(); //break down and convert data to bytes to send as a packet
                                
                                DatagramPacket sendToClient = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); //send data to client
                                serverSocket.send(sendToClient); //send data to client  
                                count += 1;
                            }
                            String line = "EOF"; //append "EOF" to the buffer to indicate end of file reading
                            buffer = line.getBytes(); //convert "EOF" to bytes to send as a packet
                            DatagramPacket sendToClient = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); //pack data for client
                            serverSocket.send(sendToClient); //send data to client
                            
                            String rel = String.valueOf("\nTotal Line Count on Server: " + count); //keep track of files sent
                            buffer = rel.getBytes();
                            DatagramPacket sendRel = new DatagramPacket(buffer, buffer.length, sendToClientAddress, sendToClientPort); 
                            serverSocket.send(sendRel);

                            reader.close(); //close reader after "get" is called
                            serverSocket.close(); //close server after "get" is called    
                        }
                    }
                }catch(SocketException e){   //catch error for creating socket
                    System.out.println(e.getMessage());
                }catch(IOException e){    //catch error for receiving data
                    System.out.println(e.getMessage());
                }
            }
        });
        exec.shutdown();  //shutdown threads
        try{
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS); //wait until all threads finish before shutdown
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }   
    public static void main(String[] args) {
        int fileDirectory = args.length;
        if(fileDirectory < 1){
            System.out.println("\nCommand Line Error: Please enter at least one argument (args).");
            return;
        }

        //COPY THIS DIRECTORY ----> C:\Users\nguye\Desktop\UC Irvine (Graduate)\SWE 242P\Module 4\ServerData
        Path directoryPath = Paths.get(args[0]);
        // System.out.println(directoryPath);

        if(!(Files.exists(directoryPath))){
            System.out.println("Directory Error: Directory or data folder is missing");
            return;
        }
        startServer(directoryPath);
    }
}