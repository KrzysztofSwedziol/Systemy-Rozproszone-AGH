package com.servantsmanagement;

import com.zeroc.Ice.*;

public class Server {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {

            ObjectAdapter adapter =
                    communicator.createObjectAdapterWithEndpoints(
                            "Adapter", "default -p 10000");

            adapter.addServantLocator(new SessionServantLocator(), "Session");

            adapter.addDefaultServant(new LoggerI(), "Logger");

            adapter.activate();
            System.out.println("Server is running on port 10000");

            communicator.waitForShutdown();
        }
    }
}
