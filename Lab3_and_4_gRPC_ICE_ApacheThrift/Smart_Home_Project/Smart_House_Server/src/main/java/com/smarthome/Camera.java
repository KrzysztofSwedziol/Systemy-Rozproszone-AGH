package com.smarthome;

import smart.camera.Camera.CameraState;

public class Camera {
    public enum Type { PTZ, STANDARD }

    private final String id;
    private final String ip;
    private final Type type;
    private boolean isOn = false;
    private int angle = 0;
    private int tilt = 0;

    public Camera(String id, String ip, Type type) {
        this.id = id;
        this.ip = ip;
        this.type = type;
    }

    public String getId() { return id; }
    public String getIp() { return ip; }
    public Type getType() { return type; }
    public boolean isPTZ() { return type == Type.PTZ; }

    public CameraState getState() {
        return CameraState.newBuilder()
                .setCameraId(id)
                .setIsOn(isOn)
                .setType(type.toString())
                .setAngle(angle)
                .setTilt(tilt)
                .setIp(ip)
                .build();
    }

    public String togglePower(boolean turnOn) {
        this.isOn = turnOn;
        return "Camera " + id + " turned " + (isOn ? "ON" : "OFF");
    }

    public String move(int angle, int tilt) {
        this.angle = angle;
        this.tilt = tilt;
        return "Camera " + id + " moved to angle " + angle + "°, tilt " + tilt + "°";
    }
}
