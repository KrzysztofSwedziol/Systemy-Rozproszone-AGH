package com.smarthome;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import smart.co.COSensorGrpc;
import smart.co.CoSensor.*;

public class COSensorService extends COSensorGrpc.COSensorImplBase {

    private final COSensor sensor;

    public COSensorService(COSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void getState(Empty request, StreamObserver<SensorState> responseObserver) {
        responseObserver.onNext(sensor.getState());
        responseObserver.onCompleted();
    }

    @Override
    public void toggleSensor(Empty request, StreamObserver<Ack> responseObserver) {
        String msg = sensor.toggle();
        Ack ack = Ack.newBuilder().setMessage(msg).build();
        responseObserver.onNext(ack);
        responseObserver.onCompleted();
    }

    @Override
    public void readCOLevel(Empty request, StreamObserver<SensorState> responseObserver) {
        responseObserver.onNext(sensor.readCOLevel());
        responseObserver.onCompleted();
    }
}
