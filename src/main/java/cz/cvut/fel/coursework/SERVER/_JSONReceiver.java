package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.GLOBAL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class _JSONReceiver implements Runnable {

    private static ServerSocket server;
    private static Socket socket;

    public void run() throws IOException {

        server = new ServerSocket(GLOBAL.PORT);

        while(true){

            System.out.println("Waiting for JSON notification");

            socket = server.accept();

            System.out.println("JSON accepted");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = br.readLine();

            if (!message.equals("")) {
                // System.out.println(message);
                Notification n = new Notification();
                n.notificate(message);
            }


        }

    }

    public void stop() throws IOException, ClassNotFoundException {
        socket.close();
    }

}