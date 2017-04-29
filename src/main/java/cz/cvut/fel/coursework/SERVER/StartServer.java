package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVICES.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class StartServer implements Runnable {

    public void run() {
        try {
            Globals.SERVER = new ServerSocket(Globals.PORT);
            Socket socket;

            while(true){
                System.out.println("Waiting for JSON notification");
                socket = Globals.SERVER.accept();
                System.out.println("JSON accepted");

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message = br.readLine();

                if (!message.equals("")) {
                    Notification n = new Notification();
                    n.notificate(message);
                }
            }

        } catch (IOException e) {}
    }
}
