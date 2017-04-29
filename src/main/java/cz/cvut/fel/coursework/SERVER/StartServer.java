package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVER.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class StartServer implements Runnable {

    public void run() {
        try {
            Globals.SERVER = new ServerSocket(Globals.PORT);

            while(true){
                System.out.println("Waiting for JSON notification");
                Globals.SOCKET = Globals.SERVER.accept();
                System.out.println("JSON accepted");

                BufferedReader br = new BufferedReader(new InputStreamReader(Globals.SOCKET.getInputStream()));

                String message = br.readLine();

                if (!message.equals("")) {
                    Notification n = new Notification();
                    n.notificate(message);
                }
            }

        } catch (IOException e) {}
    }
}
