import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();

        try{
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started\n");

            while(true){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
                System.out.println("Server accepted new client and assigned handler to it\n");
            }

        }catch(IOException e){
            System.out.println("Error in accepting client or running client handler\n");
            e.printStackTrace();

        }finally {
            try{
                serverSocket.close();
            }catch(IOException e){
                System.out.println("Error closing server socket\n");
                e.printStackTrace();
            }
        }
    }
}
