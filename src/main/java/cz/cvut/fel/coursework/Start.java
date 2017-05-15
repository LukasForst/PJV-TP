package cz.cvut.fel.coursework;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts the program. Contains one runnable public static method main().
 * Imports Controller to provide program's main functions.
 * Provides logging to console and logfile 'log/start.log'.
 * @author Anastasia Surikova
 */

public class Start {

    public static Controller c = new Controller();
    private static final Logger LOG = Logger.getLogger(Start.class.getName());

    public static void main(String[] args) throws UnknownHostException {

        // TODO: advice with logs

        LOG.setUseParentHandlers(false);
        try {
            LOG.addHandler(new FileHandler("log/start.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // For console debugging
        System.out.println("  ____       _                 \n" +
                            " |  _ \\  ___| |__  _   _  __ _ \n" +
                            " | | | |/ _ \\ '_ \\| | | |/ _` |\n" +
                            " | |_| |  __/ |_) | |_| | (_| |\n" +
                            " |____/ \\___|_.__/ \\__,_|\\__, |\n" +
                            "                         |___/ ");

        // Generate app directory in user home
        LOG.log(Level.INFO, "||========= Generating an app directory... =========||");
        System.out.println("||========= Generating an app directory... =========||");
        c.setAppDirectory();

        // Generate path for QR code saving
        LOG.log(Level.INFO, "||========= Generating a path to image... ==========||");
        System.out.println("||========= Generating a path to image... ==========||");
        c.setPathToImage();

        // Get hoster's IP address
        LOG.log(Level.INFO, "||========= Getting my IP address... ===============||");
        System.out.println("||========= Getting my IP address... ===============||");
        c.setIP();

        // Save QR code with IP address and Port number
        LOG.log(Level.INFO, "||========= Generating QR code image... ============||");
        System.out.println("||========= Generating QR code image... ============||");
        c.saveQR();

        // Initialize GUI
        LOG.log(Level.INFO, "||========= Initializing GUI... ====================||");
        System.out.println("||========= Initializing GUI... ====================||");
        c.initializeGUI();

    }
}
