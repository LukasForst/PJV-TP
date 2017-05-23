package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Restarts server when port number is changed.
 * Implements Runnable.
 * @author Anastasia Surikova
 */
public class RestartServer extends Thread {

    private static final Logger LOG = Logger.getLogger(RestartServer.class.getName());

    /**
     * Closes server connection and then starts listening again
     */
    public void run() {

        LOG.setUseParentHandlers(false);
        try {
            LOG.addHandler(new FileHandler("logs/RestartServer/log.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            LOG.log(Level.INFO, "Restarting server...");
            System.out.println("Restarting server...");

            Globals.SERVER.close();

            LOG.log(Level.INFO, "Server stopped.");
            System.out.println("Server stopped.");

            Thread startListening = new Thread(new StartServer());
            startListening.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
