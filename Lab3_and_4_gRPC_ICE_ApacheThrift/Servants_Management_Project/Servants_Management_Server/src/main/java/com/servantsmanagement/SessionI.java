package com.servantsmanagement;

import ServantsManagement.Session;
import com.zeroc.Ice.Current;

public class SessionI implements Session {
    private int actionCount = 0;
    private final String userId;

    public SessionI(String userId) {
        this.userId = userId;
        System.out.println("[LOG] Created Session for user: " + userId);
    }

    @Override
    public int performAction(Current current) {
        actionCount++;
        System.out.println("[Session] User: " + userId +
                " performed action. Total: " + actionCount);
        return actionCount;
    }

    @Override
    public String getSessionInfo(Current current) {
        return "Session for user: " + userId + ", actions: " + actionCount;
    }
}
