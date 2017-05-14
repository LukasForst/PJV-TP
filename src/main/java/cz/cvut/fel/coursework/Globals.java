package cz.cvut.fel.coursework;

import java.awt.*;
import java.net.ServerSocket;

/**
 * Provides program's configuration such as GUI configuration, PORT and IP, APP_DIRECTORY and IMG_PATH.
 * Stores data, that are accessible from other classes (i.e. ACTIVE_NOTIFICATIONS).
 * @author Anastasia Surikova
 */
public final class Globals {

    // GUI
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String ABOUT = "Awesome program by CTU students<br>Lukáš Forst and Anastasia Surikova";
    private static final Color GREEN = new Color(5, 141, 0);

    // Server
    public static ServerSocket SERVER;
    private static int PORT = 3843;
    private static String IP;

    // Paths
    private static String IMG_PATH;
    private static String APP_DIRECTORY;

    // Active notifications
    private static Object[][] ACTIVE_NOTIFICATIONS = {{}};

    // Getters
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
    public static String getIP() {
        return IP;
    }
    public static String getIMG_PATH() {
        return IMG_PATH;
    }
    public static String getAPP_DIRECTORY() {
        return APP_DIRECTORY;
    }
    public static Object[][] getACTIVE_NOTIFICATIONS() {
        return ACTIVE_NOTIFICATIONS;
    }

    // Setters
    public static void setPORT(int PORT) {
        Globals.PORT = PORT;
    }
    public static void setIP(String IP) {
        Globals.IP = IP;
    }
    public static void setImgPath(String imgPath) {
        Globals.IMG_PATH = imgPath;
    }
    public static void setAppDirectory(String appDirectory) {
        Globals.APP_DIRECTORY = appDirectory;
    }
    public static void setActiveNotifications(Object[][] newData) {
        Globals.ACTIVE_NOTIFICATIONS = newData;
    }
}
