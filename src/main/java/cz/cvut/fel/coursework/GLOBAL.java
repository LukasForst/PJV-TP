package cz.cvut.fel.coursework;

import java.awt.*;

public class GLOBAL {

    public static int PORT = 1111;

    public static void setPORT(int PORT) {
        GLOBAL.PORT = PORT;
        System.out.println("Port changed to " + PORT);
    }


    // GUI

    public static final int WIDTH = 500;
    public static final int HEIGHT = 300;
    public static final String ABOUT = "Awesome program by CTU students<br>Lukáš Forst and Anastasia Surikova";

    public static final Color GREEN = new Color(5, 141, 0);



}
