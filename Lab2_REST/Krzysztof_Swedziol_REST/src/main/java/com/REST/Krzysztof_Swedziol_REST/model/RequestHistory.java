package com.REST.Krzysztof_Swedziol_REST.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestHistory {
    private Map<Integer, FormRequestResponse> requestHistory;

    public RequestHistory() {
        this.requestHistory = new HashMap<>();
    }

    public void add(FormRequestResponse request) {
        requestHistory.put(getCurrentId(), request);
    }

    public int getCurrentId(){
        return requestHistory.size();
    }

    public FormRequestResponse getFormById(int id){
        return requestHistory.get(id);
    }

    public Map<Integer, FormRequestResponse> getRequestHistory() {
        return requestHistory;
    }

    public void replaceFormById(int id, FormRequestResponse request){
        requestHistory.put(id, request);
    }

    public void removeFormById(int id){
        requestHistory.remove(id);
    }

}
