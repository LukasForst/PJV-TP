package cz.cvut.fel.coursework;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class JSONReceiver {

    private static ServerSocket server;
    private static int port = 1111;

    public void startListening() throws IOException, ClassNotFoundException {

        server = new ServerSocket(port);

        while(true){

            System.out.println("Waiting for JSON notification");

            Socket socket = server.accept();

            System.out.println("JSON accepted");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = br.readLine();

            if (!message.equals("")) {
                System.out.println(message);
                Notification n = new Notification();
                n.notificate(message);
            }

            socket.close();
        }

        // TODO: Add some condition to end connection
        //server.close();
    }
}