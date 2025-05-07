package com.smarthome;

import io.grpc.stub.StreamObserver;
import smart.fridge.FridgeGrpc;
import smart.fridge.FridgeOuterClass.Ack;
import smart.fridge.FridgeOuterClass.Item;
import smart.fridge.FridgeOuterClass.FridgeState;
import smart.fridge.FridgeOuterClass.TemperatureRequest;
import smart.fridge.FridgeOuterClass.TemperatureResponse;
import smart.fridge.FridgeOuterClass.LightResponse;
import smart.fridge.FridgeOuterClass.ContentsResponse;
import com.google.protobuf.Empty;

public class FridgeService extends FridgeGrpc.FridgeImplBase {

    private final Fridge fridge;

    public FridgeService(Fridge fridge) {
        this.fridge = fridge;
    }

    @Override
    public void getState(Empty request, StreamObserver<FridgeState> responseObserver) {
        responseObserver.onNext(fridge.getState());
        responseObserver.onCompleted();
    }

    @Override
    public void getFridgeTemperature(Empty request, StreamObserver<TemperatureResponse> responseObserver) {
        responseObserver.onNext(fridge.getFridgeTemperature());
        responseObserver.onCompleted();
    }

    @Override
    public void getFreezerTemperature(Empty request, StreamObserver<TemperatureResponse> responseObserver) {
        responseObserver.onNext(fridge.getFreezerTemperature());
        responseObserver.onCompleted();
    }

    @Override
    public void setFridgeTemperature(TemperatureRequest request, StreamObserver<Ack> responseObserver) {
        fridge.setFridgeTemperature(request.getTargetTemperature());
        sendAck(responseObserver, "Fridge temperature set to " + request.getTargetTemperature() + " °C");
    }

    @Override
    public void setFreezerTemperature(TemperatureRequest request, StreamObserver<Ack> responseObserver) {
        fridge.setFreezerTemperature(request.getTargetTemperature());
        sendAck(responseObserver, "Freezer temperature set to " + request.getTargetTemperature() + " °C");
    }

    @Override
    public void getLightStatus(Empty request, StreamObserver<LightResponse> responseObserver) {
        responseObserver.onNext(fridge.getLightStatus());
        responseObserver.onCompleted();
    }

    @Override
    public void toggleLight(Empty request, StreamObserver<Ack> responseObserver) {
        boolean status = fridge.toggleLight();
        sendAck(responseObserver, "Light has been turned " + (status ? "ON" : "OFF"));
    }

    @Override
    public void getContents(Empty request, StreamObserver<ContentsResponse> responseObserver) {
        responseObserver.onNext(fridge.getContents());
        responseObserver.onCompleted();
    }

    @Override
    public void addProductToFridge(Item request, StreamObserver<Ack> responseObserver) {
        String msg = fridge.addProduct(fridge.getFridgeContents(), request, "fridge");
        sendAck(responseObserver, msg);
    }

    @Override
    public void addProductToFreezer(Item request, StreamObserver<Ack> responseObserver) {
        String msg = fridge.addProduct(fridge.getFreezerContents(), request, "freezer");
        sendAck(responseObserver, msg);
    }

    @Override
    public void removeProductFromFridge(Item request, StreamObserver<Ack> responseObserver) {
        String msg = fridge.removeProduct(fridge.getFridgeContents(), request, "fridge");
        sendAck(responseObserver, msg);
    }

    @Override
    public void removeProductFromFreezer(Item request, StreamObserver<Ack> responseObserver) {
        String msg = fridge.removeProduct(fridge.getFreezerContents(), request, "freezer");
        sendAck(responseObserver, msg);
    }

    private void sendAck(StreamObserver<Ack> responseObserver, String message) {
        Ack ack = Ack.newBuilder().setMessage(message).build();
        responseObserver.onNext(ack);
        responseObserver.onCompleted();
    }
}
