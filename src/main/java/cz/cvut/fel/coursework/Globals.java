package cz.cvut.fel.coursework;

import java.awt.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public final class Globals {

    // GUI
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;
    private static final String ABOUT = "Awesome program by CTU students<br>Lukáš Forst and Anastasia Surikova";
    private static final Color GREEN = new Color(5, 141, 0);
    public static ServerSocket SERVER;
    private static int PORT = 3843;
    private static String IP;
    private static String IMGPATH;
    private static String appDirectory;
    private static Object[][] data = {{}};

    public static Object[][] getData() {
        return data;
    }

    public static void setData(Object[][] newData) {
        Globals.data = newData;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static String getABOUT() {
        return ABOUT;
    }

    public static Color getGREEN() {
        return GREEN;
    }

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

    public static String getAppDirectory() {
        return appDirectory;
    }

    public static void setAppDirectory(String appDirectory) {
        Globals.appDirectory = appDirectory;
    }
}
