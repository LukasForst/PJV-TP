package cz.cvut.fel.coursework;

import java.awt.*;
import java.net.ServerSocket;

// TODO: všechny private? - Another shitcode (sorry for that)
// TODO: 5/5/17 google for singleton pattern and use it here

public class Globals {

    // GUI
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;
    public static final String ABOUT = "Awesome program by CTU students<br>Lukáš Forst and Anastasia Surikova";
    public static final Color GREEN = new Color(5, 141, 0);
    public static ServerSocket SERVER;
    public static int PORT = 3843;
    private static String IP;
    private static String IMGPATH;

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        Globals.PORT = PORT;
        System.out.println("Port changed to " + PORT);
    }

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        Globals.IP = IP;
    }

    public static String getIMGPATH() {
        return IMGPATH;
    }

    public static void setIMGPATH(String IMGPATH) {
        Globals.IMGPATH = IMGPATH;
    }
}
