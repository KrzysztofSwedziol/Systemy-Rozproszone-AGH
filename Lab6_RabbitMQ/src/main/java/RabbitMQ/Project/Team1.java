package RabbitMQ.Project;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Team1 implements Runnable{
    public static void main(String[] args) {
        String teamName = "CaveDivers";

        ArrayList<String> firstTeamsItems = new ArrayList<>();
        firstTeamsItems.add("Oxygen");
        firstTeamsItems.add("Water");
        firstTeamsItems.add("Backpack");
        firstTeamsItems.add("Flashlight");

        Team1 team = new Team1(teamName, firstTeamsItems);
        Thread teamThread = new Thread(team);
        teamThread.start();
    }

    private String teamName;
    private ArrayList<String> availableRequestobjects;
    private int requestCounter;
    private Connection connection;
    private Channel channel;
    private static final String EXCHANGE_NAME = "ORDER_TOPIC_ROUTER";
    private static final String COMPLETION_EXCHANGE_NAME = "COMPLETION_DIRECT_ROUTER";
    private static final String ADMIN_TEAM_EXCHANGE_NAME = "ADMIN_TEAM_EXCHANGE_NAME";
    private static final String ADMIN_FANOUT_EXCHANGE_NAME = "ADMIN_FANOUT_EXCHANGE_NAME";
    private String ORDER_COMPLETION_QUEUE;
    private String ADMIN_TEAM_QUEUE;
    private String ADMIN_FANOUT_QUEUE;

    public Team1(String teamName, ArrayList<String> availableRequestobjects) {
        this.teamName = teamName;
        this.requestCounter = 0;
        this.availableRequestobjects = availableRequestobjects;
        this.ORDER_COMPLETION_QUEUE = "ORDER_COMPLETION_QUEUE" + "_" + this.teamName;
        this.ADMIN_TEAM_QUEUE = "ADMIN_TEAM_QUEUE" + "_" + this.teamName;
        this.ADMIN_FANOUT_QUEUE = "ADMIN_FANOUT_QUEUE" + "_" + this.teamName;
    }

    public void run(){
        this.setupCommunication();
        this.setupExchangers();
        this.setupOrderCompletionQueue();
        this.setupAdminTeamMessage();
        this.setupAdminFanoutMessage();
        this.postRandomRequests();
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
            this.channel.exchangeDeclare(this.ADMIN_TEAM_EXCHANGE_NAME, "topic");
            this.channel.exchangeDeclare(this.ADMIN_FANOUT_EXCHANGE_NAME, "fanout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupOrderCompletionQueue(){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received Order Completion Message: " + message);
        };
        try {
            this.channel.queueDeclare(this.ORDER_COMPLETION_QUEUE, false, false, false, null);
            this.channel.queueBind(this.ORDER_COMPLETION_QUEUE, this.COMPLETION_EXCHANGE_NAME, teamName + ".order_completion");
            this.channel.basicConsume(this.ORDER_COMPLETION_QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupAdminTeamMessage(){
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received Admin Team Message: " + message);
        };
        try {
            this.channel.queueDeclare(this.ADMIN_TEAM_QUEUE, false, false, false, null);
            this.channel.queueBind(this.ADMIN_TEAM_QUEUE, this.ADMIN_TEAM_EXCHANGE_NAME, "admin_team_message.#");
            this.channel.basicConsume(this.ADMIN_TEAM_QUEUE, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void postRandomRequests(){
        System.out.println("Starting to post random requests");
        Random rand = new Random();
        while(true){
            int random_index = rand.nextInt(availableRequestobjects.size());
            int randomQuantity = rand.nextInt(10) + 1;
            int randomSleepAmount = rand.nextInt(8) + 8;
            String randomAvailableItem = this.availableRequestobjects.get(random_index);

            String message = "Order from : " + this.teamName + " with teams order id of : " + this.requestCounter + " for product : " + randomAvailableItem
                    + ", quantity : " + randomQuantity;
            try {
                channel.basicPublish(EXCHANGE_NAME, randomAvailableItem + ".order", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Sent request from : " + this.teamName + " with id : " + this.requestCounter + " for product : " + randomAvailableItem + ", quantity : " + randomQuantity);
                this.requestCounter++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(randomSleepAmount * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
