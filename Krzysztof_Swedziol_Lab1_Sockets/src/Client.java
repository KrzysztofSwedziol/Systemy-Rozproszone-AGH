import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try{
            final Socket clientSocket = new Socket("localhost", 12345);
            System.out.println("Client socket created and connected to server\n");
            final PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            new Thread(() -> {
                while(clientSocket.isConnected()) {
                    try{
                        String msg = in.readLine();
                        System.out.println(msg);
                    }catch(Exception e) {
                        System.out.println("Error reading from socket on client side \n");
                        e.printStackTrace();
                    }
                }
            }).start();

            while(clientSocket.isConnected()) {
                String msg = consoleReader.readLine();
                out.println(msg);
            }

        }catch(Exception e){
            System.out.println("Error on the client side socket creation/connecting to server/inserting message \n");
            e.printStackTrace();
        }
    }
}
