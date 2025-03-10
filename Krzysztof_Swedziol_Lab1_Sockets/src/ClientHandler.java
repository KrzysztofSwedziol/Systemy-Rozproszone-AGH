import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private String clientName;
    private PrintWriter out;
    private BufferedReader in;
    private ArrayList<ClientHandler> clientHandlers;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clientHandlers) {
        this.clientSocket = clientSocket;
        this.clientHandlers = clientHandlers;
        try{
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(IOException e){
            System.out.println("Error in client handler constructor out/in\n");
            e.printStackTrace();
        }

    }
    public void run(){
        this.getClientName();
        this.listenForClientMessage();
    }

    public void getClientName(){
        try{
            out.println("Insert your client name: ");
            this.clientName = in.readLine();
            this.broadcastMessage("Joined the chat");
        }catch(IOException e){
            System.out.println("Error in getting client name, handler side\n");
            e.printStackTrace();
        }
    }

    public void listenForClientMessage(){
        while(clientSocket.isConnected()){
            try{
                String msg = in.readLine();
                this.broadcastMessage(msg);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message){
        for(ClientHandler clientHandler : clientHandlers){
            clientHandler.receiveForeignMessage(message, this.clientName);
        }
    }

    public void receiveForeignMessage(String message, String clientName){
        out.println(clientName + ": " + message);
    }
}
