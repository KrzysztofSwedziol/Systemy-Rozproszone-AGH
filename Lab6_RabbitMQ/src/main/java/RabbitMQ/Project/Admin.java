package RabbitMQ.Project;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Admin implements Runnable {
    public static void main(String[] args) {
        HashMap<String, Integer> allItems = new HashMap<>();
        allItems.put("Oxygen", 3);
        allItems.put("Water", 2);
        allItems.put("Backpack", 4);
        allItems.put("Flashlight", 5);
        allItems.put("Food", 4);
        allItems.put("Powerbank", 6);
        allItems.put("Clothes", 7);

        Admin admin = new Admin(allItems);
        Thread thread = new Thread(admin);
        thread.start();
    }
    private Connection connection;
    private Channel channel;
    private HashMap<String, Integer> allItems;
    private static final String EXCHANGE_NAME = "ORDER_TOPIC_ROUTER";
    private static final String ADMIN_TEAM_EXCHANGE_NAME = "ADMIN_TEAM_EXCHANGE_NAME";
    private static final String ADMIN_PROVIDER_EXCHANGE_NAME = "ADMIN_PROVIDER_EXCHANGE_NAME";
    private static final String ADMIN_FANOUT_EXCHANGE_NAME = "ADMIN_FANOUT_EXCHANGE_NAME";

    public Admin(HashMap<String, Integer> allItems){
        this.allItems = allItems;
    }

    public void run() {
        this.setupCommunication();
        this.setupExchangers();
        this.declareQueues();
        this.sendRandomMessages();
    }

    public void setupCommunication(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupExchangers(){
        try {
            this.channel.exchangeDeclare(this.EXCHANGE_NAME, "topic");
            this.channel.exchangeDeclare(ADMIN_TEAM_EXCHANGE_NAME, "topic");
            this.channel.exchangeDeclare(ADMIN_PROVIDER_EXCHANGE_NAME, "topic");
            this.channel.exchangeDeclare(ADMIN_FANOUT_EXCHANGE_NAME, "fanout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupSingleQueue(String productName, int orderTime){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Admin noticed order message : ");
            System.out.println(message);
        };
        String QUEUE_NAME = "Admin_" + productName;
        try {
            this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            this.channel.queueBind(QUEUE_NAME, this.EXCHANGE_NAME, productName + ".*");
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void declareQueues(){
        for(Map.Entry<String, Integer> entry : this.allItems.entrySet()){
            this.setupSingleQueue(entry.getKey(), entry.getValue());
        }

    }

    public void sendRandomMessages(){
        String teamMessage = "Admin message to teams";
        String providerMessage = "Admin message to providers";
        String fanoutMessage = "Admin fanout message";
        System.out.println("Starting to send random team, provider and fanout messages");
        Random rand = new Random();

        while(true){
            int randSeconds = rand.nextInt(10) + 1;
            try {
                channel.basicPublish(this.ADMIN_TEAM_EXCHANGE_NAME, "admin_team_message.team_message", null, teamMessage.getBytes(StandardCharsets.UTF_8));
                channel.basicPublish(this.ADMIN_PROVIDER_EXCHANGE_NAME, "admin_provider_message.provider_message", null, providerMessage.getBytes(StandardCharsets.UTF_8));
                channel.basicPublish(this.ADMIN_FANOUT_EXCHANGE_NAME, "", null, fanoutMessage.getBytes(StandardCharsets.UTF_8));
                try {
                    Thread.sleep(randSeconds * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
