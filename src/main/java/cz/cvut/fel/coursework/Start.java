package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;

public class Start {

    public static void main(String[] args) {

        // Get hoster IP address and saves it to database

//        IPIdentifier ipIdentifier = new IPIdentifier();
//        ipIdentifier.getIP();
//        ipIdentifier.insertIntoDatabase();

        // Waits for JSON data and then displays notification

//        JSONReceiver receiver = new JSONReceiver();
//        try {
//            receiver.startListening();
//        } catch (Exception ignore) {}

        // Some GUI out here

        MainWindow main = new MainWindow();
        main.createWindow();

    }
}
