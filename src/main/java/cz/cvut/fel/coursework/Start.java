package cz.cvut.fel.coursework;

public class Start {

    public static Controller c = new Controller();

    public static void main(String[] args) {

        System.out.println("  ____       _                 \n" +
                            " |  _ \\  ___| |__  _   _  __ _ \n" +
                            " | | | |/ _ \\ '_ \\| | | |/ _` |\n" +
                            " | |_| |  __/ |_) | |_| | (_| |\n" +
                            " |____/ \\___|_.__/ \\__,_|\\__, |\n" +
                            "                         |___/ ");

        System.out.println("Generating an app directory...");
        c.setAppDirectory();

        System.out.println("Generating a path to image...");
        c.setPathToImage();

        // Get hoster IP address
        System.out.println("Getting my IP address...");
        c.setIP();

        // Save QR code with IP
        System.out.println("Generating QR code image...");
        c.saveQR();

        // Some GUI out here
        System.out.println("Initializing GUI...");
        c.initializeGUI();

    }
}
