package com.smarthome;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import smart.camera.CameraServiceGrpc;
import smart.camera.Camera.*;

import java.util.List;

public class CameraService extends CameraServiceGrpc.CameraServiceImplBase {

    private final List<Camera> cameras;

    public CameraService(List<Camera> cameras) {
        this.cameras = cameras;
    }

    private Camera findCameraById(String id) {
        return cameras.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void listCameras(Empty request, StreamObserver<CameraList> responseObserver) {
        CameraList.Builder listBuilder = CameraList.newBuilder();
        for (Camera cam : cameras) {
            listBuilder.addCameras(
                    CameraInfo.newBuilder()
                            .setCameraId(cam.getId())
                            .setType(cam.getType().toString())
                            .setIp(cam.getIp())
                            .build()
            );
        }
        responseObserver.onNext(listBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getState(CameraRequest request, StreamObserver<CameraState> responseObserver) {
        Camera cam = findCameraById(request.getCameraId());
        if (cam == null) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Camera not found").asRuntimeException());
            return;
        }
        responseObserver.onNext(cam.getState());
        responseObserver.onCompleted();
    }

    @Override
    public void togglePower(TogglePowerRequest request, StreamObserver<Ack> responseObserver) {
        Camera cam = findCameraById(request.getCameraId());
        if (cam == null) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Camera not found").asRuntimeException());
            return;
        }
        String msg = cam.togglePower(request.getTurnOn());
        responseObserver.onNext(Ack.newBuilder().setMessage(msg).build());
        responseObserver.onCompleted();
    }

    @Override
    public void moveCamera(MoveCameraRequest request, StreamObserver<Ack> responseObserver) {
        Camera cam = findCameraById(request.getCameraId());
        if (cam == null) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Camera not found").asRuntimeException());
            return;
        }
        if (!cam.isPTZ()) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("This camera does not support PTZ").asRuntimeException());
            return;
        }
        String msg = cam.move(request.getAngle(), request.getTilt());
        responseObserver.onNext(Ack.newBuilder().setMessage(msg).build());
        responseObserver.onCompleted();
    }
}
