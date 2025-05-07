package com.servantsmanagement;

import com.zeroc.Ice.Current;
import com.zeroc.Ice.ServantLocator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionServantLocator implements ServantLocator {

    private final Map<String, SessionI> sessions = new ConcurrentHashMap<>();

    @Override
    public LocateResult locate(Current current) {
        String userId = current.id.name;
        SessionI servant = sessions.computeIfAbsent(userId, SessionI::new);
        return new LocateResult(servant, null);
    }

    @Override
    public void finished(Current current,
                         com.zeroc.Ice.Object servant,
                         Object cookie) { }

    @Override
    public void deactivate(String category) { }
}
