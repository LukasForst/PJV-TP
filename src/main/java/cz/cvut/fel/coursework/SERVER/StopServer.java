package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;

import java.io.IOException;

/**
 * Stops server listening.
 * Implements Runnable.
 * @author Anastasia Surikova
 */
public class StopServer extends Thread {

    /**
     * CLoses server connection
     */
    public void run() {

        try {
            Globals.SERVER.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
