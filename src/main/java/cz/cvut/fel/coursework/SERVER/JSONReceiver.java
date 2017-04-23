package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.GLOBAL;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class JSONReceiver {

    private static ServerSocket server;

    // TODO: nové vlákno !!!!
    // TODO: getting json with other structure

    public void startListening() throws IOException, ClassNotFoundException {

        server = new ServerSocket(GLOBAL.PORT);

        while(true){

            System.out.println("Waiting for JSON notification");

            Socket socket = server.accept();

            System.out.println("JSON accepted");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = br.readLine();

            if (!message.equals("")) {
//                System.out.println(message);
                Notification n = new Notification();
                n.notificate(message);
            }

            socket.close();
        }

        // TODO: Add some condition to end connection from GUI
        //server.close();
    }
}