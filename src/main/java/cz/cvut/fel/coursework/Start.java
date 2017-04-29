package cz.cvut.fel.coursework;

public class Start {

    public static Controller c = new Controller();

    public static void main(String[] args) {

        // Get hoster IP address
        c.getIP();

        // Save QR code with IP
        c.saveQR();

        // Some GUI out here
        c.initializeGUI();

    }
}
