package cz.cvut.fel.coursework.SERVER;

import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVICES.Notification;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class StartServer implements Runnable {

    public void run() {
        while (true) {
            try {
                Globals.SERVER = new ServerSocket(Globals.PORT);
                Socket socket;


                System.out.println("Waiting for JSON notification...");
                socket = Globals.SERVER.accept();
                System.out.println("JSON accepted.");

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message = br.readLine();

                //client has connected
                if (message == null) {
                    System.out.println("Client connected!");
                } else {
                    System.out.println("read is: " + message);

                    try {
                        JSONObject received = new JSONObject(message);
                        if (received.has("json_active")) {
                            JSONObject active_notifications = new JSONObject(received.get("json_active").toString());
                            // TODO: 5/5/17 Here you have to handle active notifications
                            for (int i = 0; active_notifications.has("active_" + i); i++) {
                                //par_notification is every single notification in the bundle of notifications received
                                JSONObject par_notification = new JSONObject(active_notifications.get("active_" + i).toString());
                                System.out.println("active_" + i + " - " + par_notification.getString("tickerText"));
                            }

                        } else if (received.has("tickerText")) {
                            Notification n = new Notification();
                            n.notificate(message);
                        } else {
                            System.out.println("nop");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                socket.close();
                Globals.SERVER.close();
            } catch (IOException e) {
                // TODO: 5/5/17 handle exception, otherwise everything will fall down
                break;
            }

        }
    }
}
