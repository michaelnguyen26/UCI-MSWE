import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//REFERENCE: https://docs.oracle.com/javase/7/docs/api/java/nio/file/DirectoryStream.html
           //https://tutorials.jenkov.com/java-nio/path.html
           //https://web.mit.edu/6.031/www/sp19/classes/23-sockets-networking/
           //https://users.cs.duke.edu/~chase/cps196/slides/sockets.pdf

public class ClientTCP {
    public static void main(String[] args) {

        try{
            Socket socket = new Socket("localhost", 8010);

            BufferedReader readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writeToServer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            
            while(true){
                System.out.print("Client Message: ");
                String sendToServer = keyboardInput.readLine();

                if(sendToServer.equals("index")){
                    writeToServer.println(sendToServer); //use println since readline only sees \r\n
                    String line = readFromServer.readLine(); //read what the server says
                    while(!(line.equals("EOF"))){     //while the server sends data and does not reach EOF
                        System.out.println(line);       //print what the server sends
                        line = readFromServer.readLine();    //read the next output from the server
                    }
                    System.out.println("\nDone\n");
                }else if(sendToServer.contains("get ")){ //check if input contains get and " "
                    writeToServer.println(sendToServer);

                    if(readFromServer.readLine().equals("err")){ //read "err" if file does not exist
                        System.out.println(readFromServer.readLine()); //print no such file and continue the loop
                        continue;
                    }else{
                        System.out.println(readFromServer.readLine()); //read "ok"
                    }

                    String data = readFromServer.readLine();
                    while(data != null){     //while the server sends data
                        System.out.println(data);     //print what the server sends
                        data = readFromServer.readLine();    //read the next output from the server
                    }
                    writeToServer.close();
                    keyboardInput.close();
                    readFromServer.close();
                    socket.close();
                }else{
                    System.out.println("\nInvalid request: Please enter \"index\" or \"get\"\n");
                }
            }     
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }  
}