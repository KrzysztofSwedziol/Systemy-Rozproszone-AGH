import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        //UDP
        final ArrayList<ClientInfo> clientsUDP = new ArrayList<>();

        try{
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started, TCP Socket created successfully");

            //udp
            final DatagramSocket serverSocketUDP = new DatagramSocket(12345);
            byte[] receiveData = new byte[1024];
            System.out.println("UDP Socket created successfully");

            new Thread(() -> {
                while(true){
                    try{
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocketUDP.receive(receivePacket);
                        String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        InetAddress clientAddress = receivePacket.getAddress();
                        int clientPort = receivePacket.getPort();
                        System.out.println("Received message from: " + clientAddress + " " + clientPort + ": " + msg);
                        ClientInfo clientInfo = new ClientInfo(clientAddress, clientPort);
                        byte[] messageToSend = (clientAddress + " " + clientPort + ": " + msg).getBytes();

                        if(!clientsUDP.contains(clientInfo)){
                            clientsUDP.add(clientInfo);
                        }else{
                            for(ClientInfo client : clientsUDP){
                                InetAddress address = client.address();
                                int port = client.port();
                                DatagramPacket sendPacket = new DatagramPacket(messageToSend, messageToSend.length, address, port);
                                serverSocketUDP.send(sendPacket);
                            }
                        }

                    }catch(IOException e){
                        System.out.println("Error receiving udp packet from client \n");
                        e.printStackTrace();
                    }
                }

            }).start();

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
