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

            String content = br.readLine();

            if (!content.equals("")) {
                System.out.println(content);
                Notification n = new Notification();
                n.notificate();
            }

            socket.close();
        }

        //server.close();
    }
}