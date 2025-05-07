module ServantsManagement {

    interface Session {
        int performAction();
        string getSessionInfo();
    };

    interface Logger {
        void logMessage(string message);
    };
};