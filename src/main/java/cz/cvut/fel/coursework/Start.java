package cz.cvut.fel.coursework;

import java.net.UnknownHostException;

/**
 * Starts the program. Contains one runnable public static method main().
 * Imports Controller to provide program's main functions.
 * Provides logging to console and logfile 'log/start.log'.
 * @author Anastasia Surikova
 */

public class Start {

    public static Controller c = new Controller();

    public static void main(String[] args) throws UnknownHostException {

        // For console debugging
        System.out.println("  ____       _                 \n" +
                            " |  _ \\  ___| |__  _   _  __ _ \n" +
                            " | | | |/ _ \\ '_ \\| | | |/ _` |\n" +
                            " | |_| |  __/ |_) | |_| | (_| |\n" +
                            " |____/ \\___|_.__/ \\__,_|\\__, |\n" +
                            "                         |___/ ");

        // Generate app directory in user home
        System.out.println("||========= Generating an app directory... =========||");
        c.setAppDirectory();

        // Generate path for QR code saving
        System.out.println("||========= Generating a path to image... ==========||");
        c.setPathToImage();

        // Get hoster's IP address
        System.out.println("||========= Getting my IP address... ===============||");
        c.setIP();

        // Save QR code with IP address and Port number
        System.out.println("||========= Generating QR code image... ============||");
        c.saveQR();

        // Initialize GUI
        System.out.println("||========= Initializing GUI... ====================||");
        c.initializeGUI();

    }
}
