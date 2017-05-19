package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;

import java.io.IOException;

/**
 * Restarts server when port number is changed.
 * Implements Runnable.
 * @author Anastasia Surikova
 */
public class RestartServer extends Thread {

    /**
     * Closes server connection and then starts listening again
     */
    public void run() {

        try {

            System.out.println("Restarting serer...");
            Globals.SERVER.close();
            System.out.println("Server stopped.");
            Thread startListening = new Thread(new StartServer());
            startListening.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
