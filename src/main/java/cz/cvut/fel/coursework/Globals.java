package cz.cvut.fel.coursework;

import java.awt.*;
import java.net.ServerSocket;

// TODO: všechny private?

public class Globals {

    public static ServerSocket SERVER;

    public static int PORT = 1111;

    public static void setPORT(int PORT) {
        Globals.PORT = PORT;
        System.out.println("Port changed to " + PORT);
    }

    public static int getPORT() {
        return PORT;
    }

    private static String IP;

    public static void setIP(String IP) {
        Globals.IP = IP;
    }

    public static String getIP() {
        return IP;
    }

    private static String IMGPATH;

    public static void setIMGPATH(String IMGPATH) {
        Globals.IMGPATH = IMGPATH;
    }

    public static String getIMGPATH() {
        return IMGPATH;
    }

    // GUI
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;
    public static final String ABOUT = "Awesome program by CTU students<br>Lukáš Forst and Anastasia Surikova";
    public static final Color GREEN = new Color(5, 141, 0);
}
