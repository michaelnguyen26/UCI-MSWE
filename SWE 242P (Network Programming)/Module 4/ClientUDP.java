import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

//REFERENCE: https://www.codejava.net/java-se/networking/java-udp-client-server-program-example
           //http://tutorials.jenkov.com/java-networking/udp-datagram-sockets.html
           //http://www.java2s.com/example/java/network/udp-send-object.html
           //https://docs.oracle.com/javase/7/docs/api/java/net/DatagramSocket.html

public class ClientUDP {

    private final static int SERVER_PORT = 8010;

    public static void main(String[] args) {
        DatagramSocket clientSocket;

        while(true){
            System.out.print("\nClient Message: ");
            
            try{
                InetAddress serverAddress = InetAddress.getLocalHost();
                // System.out.println(serverAddress);
                clientSocket = new DatagramSocket(); //assign random port

                BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in)); //read buffered input from the keyboard
                String keyIn = keyboardInput.readLine(); //read input from user

                byte[] buffer = new byte[512]; //create byte buffer of 512 bytes to send and receive data
                
                if(keyIn.equals("index")){
                    int count = 0;  //keep track of files sent
                    byte[] indexAsByte = keyIn.getBytes(); //convert "index" to a byte[] to send as a DatagramPacket that the server can process and execute the correct command
                    DatagramPacket indexRequest = new DatagramPacket(indexAsByte, indexAsByte.length, serverAddress, SERVER_PORT);
                    clientSocket.send(indexRequest);
                    DatagramPacket result;

                    while(true){
                        result = new DatagramPacket(buffer, buffer.length); //create a buffer result to store the result
                        clientSocket.receive(result);    //wait to receive result from server and store it in buffer[512]

                        String indexResult = new String(result.getData(), 0, result.getLength(), "ASCII"); //convert results sent as DatagramPacket to a string of ASCII
                        
                        if(indexResult.equals("EOF")){ //end of results when "EOF" is read
                            result = new DatagramPacket(buffer, buffer.length); 
                            clientSocket.receive(result);  //receive total line count from server

                            String relResult = new String(result.getData(), 0, result.getLength(), "ASCII"); //convert results sent as DatagramPacket to a string of ASCII
                        
                            System.out.println(relResult);
                            System.out.println("Total Received from Server: " + count);
                            
                            result.setLength(buffer.length);
                            break;
                        }
                        result.setLength(buffer.length);  //reset buffer length
                        System.out.println(indexResult); //print results
                        count += 1;             
                    }
                    
                }else if(keyIn.contains("get ")){
                    byte[] getAsByte = keyIn.getBytes(); //convert "get" to byte[] to send as DatagramPacket that the server can process and execute the correct command
                    DatagramPacket packet = new DatagramPacket(getAsByte, getAsByte.length, serverAddress, SERVER_PORT);
                    clientSocket.send(packet);
                    
                    String getResult;  //declare outside to verify the scope is seen beyond the "while" loop
                    int count = 0;
                    DatagramPacket result;

                    while(true){
                        result = new DatagramPacket(buffer, buffer.length); //create a buffer result to store the result
                        clientSocket.receive(result);    //wait to receive result from server and store it in buffer[512]
                        
                        getResult = new String(result.getData(), 0, result.getLength(), "ASCII");

                        if(getResult.equals("\nok\n")){  //account for the "ok" which consumes one line for the conut
                            System.out.println(getResult);
                            continue;
                        }

                        if(getResult.equals("err")){
                            System.out.println("\nError: File does not exist");
                            break;
                        }

                        if(getResult.equals("EOF")){  //end loop when "EOF" is read
                            System.out.println("\nThank you. UDP server socket closed!");
                            break;
                        }

                        System.out.println(getResult);  //print result
                        count += 1;
                        result.setLength(buffer.length); //reset length of buffer
                    }
                    if(getResult.equals("err")){   //if err is read continue the loop for next client request
                        continue;
                    }else if(getResult.equals("EOF")){   //break the final loop to get to the closing the socket
                        clientSocket.receive(result); 
                        getResult = new String(result.getData(), 0, result.getLength(), "ASCII");
                        
                        System.out.println(getResult);
                        System.out.println("Total Received from Server: " + count);
                        break;
                    }
                }else{
                    System.out.println("\nEnter a valid request \"index\" or \"get\"");
                    continue;
                }
            }catch(SocketException e){
                System.out.println(e.getMessage());
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
        clientSocket.close();
    }
}