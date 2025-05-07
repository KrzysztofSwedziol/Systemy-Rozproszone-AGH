package com.servantsmanagement;

import ServantsManagement.Logger;
import com.zeroc.Ice.Current;

public class LoggerI implements Logger {

    @Override
    public void logMessage(String message, Current current) {
        System.out.println("[Logger] Message received: " + message);
    }
}
