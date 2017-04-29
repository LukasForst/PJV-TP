package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;

import java.io.IOException;

public class StopServer extends Thread {

    public void run() {

        try {
            Globals.SERVER.close();
            System.out.println("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
