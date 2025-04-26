package com.smarthome;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class SmartHomeServer {
    public static void main(String[] args) throws Exception {
        Fridge fridge = new Fridge();
        COSensor coSensor = new COSensor();

        Server server = ServerBuilder.forPort(50051)
                .addService(new FridgeService(fridge))
                .addService(new COSensorService(coSensor))
                .build();

        System.out.println("Smart Home Server running on port 50051");
        server.start();
        server.awaitTermination();
    }
}