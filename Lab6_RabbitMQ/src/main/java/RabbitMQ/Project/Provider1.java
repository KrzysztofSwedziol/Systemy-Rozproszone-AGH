package RabbitMQ.Project;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


public class Provider1 implements Runnable {
    public static void main(String[] args) {
        HashMap<String, Integer> allItems = new HashMap<>();
        allItems.put("Oxygen", 3);
        allItems.put("Water", 2);
        allItems.put("Backpack", 4);
        allItems.put("Flashlight", 5);
        allItems.put("Food", 4);
        allItems.put("Powerbank", 6);
        allItems.put("Clothes", 7);

        HashMap<String, Integer> firstProviderInventory = new HashMap<>();
        firstProviderInventory.put("Oxygen", allItems.get("Oxygen"));
        firstProviderInventory.put("Water", allItems.get("Water"));
        firstProviderInventory.put("Backpack", allItems.get("Backpack"));
        firstProviderInventory.put("Flashlight", allItems.get("Flashlight"));
        firstProviderInventory.put("Food", allItems.get("Food"));

        Provider1 provider1 = new Provider1(firstProviderInventory, "PositiveEnergyProviders");
        Thread thread = new Thread(provider1);
        thread.start();
    }

    private String providerName;
    private HashMap<String, Integer> inventory; //String is name of product and int is amount of seconds to process the order
    private Connection connection;
    private Channel channel;
    private int requestCounter;
    private static final String EXCHANGE_NAME = "ORDER_TOPIC_ROUTER";
    private static final String COMPLETION_EXCHANGE_NAME = "COMPLETION_DIRECT_ROUTER";
    private static final String ADMIN_PROVIDER_EXCHANGE_NAME = "ADMIN_PROVIDER_EXCHANGE_NAME";
    private static final String ADMIN_FANOUT_EXCHANGE_NAME = "ADMIN_FANOUT_EXCHANGE_NAME";
    private String ADMIN_PROVIDER_QUEUE;
    private String ADMIN_FANOUT_QUEUE;

    public Provider1(HashMap<String, Integer> inventory, String name) {
        this.inventory = inventory;
        this.providerName = name;
        this.requestCounter = 0;
        this.ADMIN_PROVIDER_QUEUE = "ADMIN_PROVIDER_QUEUE" + "_" + this.providerName;
        this.ADMIN_FANOUT_QUEUE = "ADMIN_FANOUT_QUEUE" + "_" + this.providerName;
    }

    public void run() {
        this.setupCommunication();
        this.setupExchangers();
        this.declareQueues();
        this.setupAdminProviderMessage();
        this.setupAdminFanoutMessage();
    }

    public void setupCommunication(){
        System.out.println("Creating Connection and Channel");
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
        System.out.println("Creating Exchangers");
        try {
            this.channel.exchangeDeclare(this.EXCHANGE_NAME, "topic");
            this.channel.exchangeDeclare(this.COMPLETION_EXCHANGE_NAME, "direct");
            this.channel.exchangeDeclare(this.ADMIN_PROVIDER_EXCHANGE_NAME, "topic");
            this.channel.exchangeDeclare(this.ADMIN_FANOUT_EXCHANGE_NAME, "fanout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupSingleQueue(String productName, int orderTime){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Provider " + this.providerName + " received order");
            System.out.println("Providers order id : " + this.requestCounter);
            System.out.println(message);
            this.requestCounter++;
            String[] messageArr = message.split(" ");
            String teamName = messageArr[3];
            try {
                Thread.sleep(orderTime * 1000);
                String messageCompletion = "Completed order for : " + teamName + " for product : " + productName + " by " + this.providerName;
                channel.basicPublish(COMPLETION_EXCHANGE_NAME, teamName + ".order_completion", null, messageCompletion.getBytes(StandardCharsets.UTF_8));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        String QUEUE_NAME = "PROVIDER_" + productName;
        try {
            this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Declared queue: " + QUEUE_NAME);
            System.out.println("Binding queue " + QUEUE_NAME + " to " + this.EXCHANGE_NAME + " with key " + productName + ".#");
            this.channel.queueBind(QUEUE_NAME, this.EXCHANGE_NAME, productName + ".#");
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
            System.out.println("Listening on queue: " + QUEUE_NAME + " with routing key: " + productName + ".#");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void declareQueues(){
        System.out.println("Declaring Queues");
        for(Map.Entry<String, Integer> entry : this.inventory.entrySet()){
            this.setupSingleQueue(entry.getKey(), entry.getValue());
        }

    }

    public void setupAdminFanoutMessage(){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received Admin Fanout Message: " + message);
        };
        try {
            this.channel.queueDeclare(this.ADMIN_FANOUT_QUEUE, false, false, false, null);
            this.channel.queueBind(this.ADMIN_FANOUT_QUEUE, this.ADMIN_FANOUT_EXCHANGE_NAME, "admin_fanout_message.#");
            this.channel.basicConsume(this.ADMIN_FANOUT_QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupAdminProviderMessage(){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received Admin Provider Message: " + message);
        };
        try {
            this.channel.queueDeclare(this.ADMIN_PROVIDER_QUEUE, false, false, false, null);
            this.channel.queueBind(this.ADMIN_PROVIDER_QUEUE, this.ADMIN_PROVIDER_EXCHANGE_NAME, "admin_provider_message.#");
            this.channel.basicConsume(this.ADMIN_PROVIDER_QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}