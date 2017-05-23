package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stops server listening.
 * Implements Runnable.
 * @author Anastasia Surikova
 */
public class StopServer extends Thread {

    private static final Logger LOG = Logger.getLogger(StopServer.class.getName());

    /**
     * Closes server connection
     */
    public void run() {

        LOG.setUseParentHandlers(false);
        try {
            LOG.addHandler(new FileHandler("logs/StopServer/log.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Globals.SERVER.close();
            LOG.log(Level.INFO, "Server stopped.");
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
