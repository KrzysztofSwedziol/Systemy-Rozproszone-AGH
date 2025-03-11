import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try{
            final Socket clientSocket = new Socket("localhost", 12345);
            System.out.println("Client TCP socket created and connected to server");
            final PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            //UDP
            boolean UDPMode = false;
            boolean firstMessageWasSent = false;
            final DatagramSocket clientSocketUDP = new DatagramSocket();
            final InetAddress serverAddressUDP = InetAddress.getByName("localhost");

            //Multicast
            boolean multicastMode = false;
            String multicastAddress = "230.0.0.1";
            int port = 5000;
            MulticastSocket multicastSocket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(multicastAddress);
            multicastSocket.joinGroup(group);

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

            //UDP receive thread
            new Thread(() -> {
                try{
                    while(true){
                        byte[] receiveBuffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        clientSocketUDP.receive(receivePacket);
                        String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(msg);
                    }
                }catch(IOException e) {
                    System.out.println("Error reading from UDP socket on client side \n");
                    e.printStackTrace();
                }
            }).start();

            //Multicast receive thread
            new Thread(() -> {
                try{
                    while(true){
                        byte[] receiveBuffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        multicastSocket.receive(receivePacket);
                        String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(msg);
                    }
                }catch(IOException e){
                    System.out.println("Error receiving multicast message \n");
                    e.printStackTrace();
                }
            }).start();

            while(true) {
                String msg = consoleReader.readLine();
                if(msg.equals("U")){
                    if(UDPMode == false){
                        UDPMode = true;
                        if(firstMessageWasSent == false){
                            byte[] sendBuffer = "First message to server so that it can remember client data".getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddressUDP, 12345);
                            clientSocketUDP.send(sendPacket);
                            firstMessageWasSent = true;
                        }
                        System.out.println("Changed communication mode to UDP");
                    }else{
                        UDPMode = false;
                        System.out.println("Changed communication mode to TCP");
                    }
                }else if (msg.equals("M")){
                    if(multicastMode == false){
                        multicastMode = true;
                        System.out.println("Changed communication mode to multicast");
                    }else{
                        multicastMode = false;
                        System.out.println("Changed communication mode to non-multicast");
                    }
                }else{
                    if(UDPMode == false && multicastMode == false){
                        out.println(msg);
                    }else if (UDPMode == true && multicastMode == false){
                        byte[] sendBuffer = msg.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddressUDP, 12345);
                        clientSocketUDP.send(sendPacket);
                    }else if (UDPMode == false && multicastMode == true){
                        String multicastMessage = "Multicast message: " + msg;
                        DatagramPacket packet = new DatagramPacket(multicastMessage.getBytes(), multicastMessage.length(), group, port);
                        multicastSocket.send(packet);
                    }else{
                        String multicastMessage = "Multicast message: " + msg;
                        DatagramPacket packet = new DatagramPacket(multicastMessage.getBytes(), multicastMessage.length(), group, port);
                        multicastSocket.send(packet);

                        byte[] sendBuffer = msg.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddressUDP, 12345);
                        clientSocketUDP.send(sendPacket);
                    }
                }
            }

        }catch(Exception e){
            System.out.println("Error on the client side socket creation/connecting to server/inserting message \n");
            e.printStackTrace();
        }
    }
}
