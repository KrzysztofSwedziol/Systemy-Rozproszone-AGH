package com.smarthome;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import com.smarthome.Camera.Type;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeCameraServer {
    public static void main(String[] args) throws Exception {
        List<Camera> cameras = new ArrayList<>();
        cameras.add(new Camera("cam1", "192.168.1.10", Type.PTZ));
        cameras.add(new Camera("cam2", "192.168.1.11", Type.STANDARD));

        Server server = ServerBuilder.forPort(50052)
                .addService(new CameraService(cameras))
                .build();

        System.out.println("Camera Server running on port 50052 with 2 cameras.");
        server.start();
        server.awaitTermination();
    }
}