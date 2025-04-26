package com.smarthome;

import smart.co.CoSensor.SensorState;

import java.util.Random;

public class COSensor {

    private boolean isOn = false;
    private float currentCOLevel = 0.0f;
    private final Random random = new Random();

    public SensorState getState() {
        return SensorState.newBuilder()
                .setIsOn(isOn)
                .setCoLevel(currentCOLevel)
                .build();
    }

    public String toggle() {
        isOn = !isOn;
        if (!isOn) {
            currentCOLevel = 0.0f;
        }
        return "CO Sensor turned " + (isOn ? "ON" : "OFF");
    }

    public SensorState readCOLevel() {
        if (isOn) {
            currentCOLevel = 1 + random.nextFloat() * 49;
        } else {
            currentCOLevel = 0.0f;
        }

        return getState();
    }
}
