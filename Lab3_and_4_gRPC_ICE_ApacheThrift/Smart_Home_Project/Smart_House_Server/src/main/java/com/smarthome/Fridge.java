package com.smarthome;

import smart.fridge.*;
import java.util.ArrayList;
import java.util.List;
import smart.fridge.FridgeOuterClass;
import smart.fridge.FridgeOuterClass.Item;
import smart.fridge.FridgeOuterClass.FridgeState;
import smart.fridge.FridgeOuterClass.TemperatureResponse;
import smart.fridge.FridgeOuterClass.ContentsResponse;
import smart.fridge.FridgeOuterClass.LightResponse;

public class Fridge {

    private float fridgeTemperature = 4.0f;
    private float freezerTemperature = -18.0f;
    private boolean lightOn = false;

    private final List<Item> fridgeContents = new ArrayList<>();
    private final List<Item> freezerContents = new ArrayList<>();

    public Fridge() {
        fridgeContents.add(Item.newBuilder().setName("Milk").setQuantity(2).build());
        freezerContents.add(Item.newBuilder().setName("Ice Cream").setQuantity(1).build());
    }

    public FridgeState getState() {
        return FridgeState.newBuilder()
                .setFridgeTemperature(fridgeTemperature)
                .setFreezerTemperature(freezerTemperature)
                .setLightOn(lightOn)
                .addAllFridgeContents(fridgeContents)
                .addAllFreezerContents(freezerContents)
                .build();
    }

    public TemperatureResponse getFridgeTemperature() {
        return TemperatureResponse.newBuilder().setTemperature(fridgeTemperature).build();
    }

    public TemperatureResponse getFreezerTemperature() {
        return TemperatureResponse.newBuilder().setTemperature(freezerTemperature).build();
    }

    public void setFridgeTemperature(float temp) {
        this.fridgeTemperature = temp;
    }

    public void setFreezerTemperature(float temp) {
        this.freezerTemperature = temp;
    }

    public LightResponse getLightStatus() {
        return LightResponse.newBuilder().setLightOn(lightOn).build();
    }

    public boolean toggleLight() {
        lightOn = !lightOn;
        return lightOn;
    }

    public ContentsResponse getContents() {
        return ContentsResponse.newBuilder()
                .addAllFridgeContents(fridgeContents)
                .addAllFreezerContents(freezerContents)
                .build();
    }

    public String addProduct(List<Item> contents, Item request, String zone) {
        for (int i = 0; i < contents.size(); i++) {
            Item item = contents.get(i);
            if (item.getName().equalsIgnoreCase(request.getName())) {
                contents.set(i, item.toBuilder()
                        .setQuantity(item.getQuantity() + request.getQuantity())
                        .build());
                return "Updated " + request.getName() + " in " + zone;
            }
        }
        contents.add(request);
        return "Added " + request.getName() + " to " + zone;
    }

    public String removeProduct(List<Item> contents, Item request, String zone) {
        for (int i = 0; i < contents.size(); i++) {
            Item item = contents.get(i);
            if (item.getName().equalsIgnoreCase(request.getName())) {
                int newQty = item.getQuantity() - request.getQuantity();
                if (newQty > 0) {
                    contents.set(i, item.toBuilder().setQuantity(newQty).build());
                    return "Reduced quantity of " + request.getName() + " in " + zone + " to " + newQty;
                } else {
                    contents.remove(i);
                    return "Removed " + request.getName() + " from " + zone;
                }
            }
        }
        return "Product not found in " + zone + ": " + request.getName();
    }

    public List<Item> getFridgeContents() {
        return fridgeContents;
    }

    public List<Item> getFreezerContents() {
        return freezerContents;
    }
}
