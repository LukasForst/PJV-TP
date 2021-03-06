
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @[ile name:  Server
 * @date :       16.3.17
 * @author :     Lukas Forst
 * Package:    PACKAGE_NAME
 * Project:    ServerTest
 */

public class Server extends Thread{

    //anonymous ports 32768 - 65535
    //By default all ports from 1 to 1024 are root privileged

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while(true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                InputStreamReader input = new InputStreamReader(server.getInputStream());
                BufferedReader br = new BufferedReader(input);
                OutputStreamWriter out = new OutputStreamWriter(server.getOutputStream());
                BufferedWriter bw = new BufferedWriter(out);
                System.out.println("OK");

                String read = br.readLine();
                if(read == null){
                    System.out.println("Client connected!");
                } else {
                    System.out.println("read is: " + read);

                    try {
                        JSONObject received = new JSONObject(read);
                        if(received.has("json_active")){
                            JSONObject active_notifications = new JSONObject(received.get("json_active").toString());

                            for(int i = 0; active_notifications.has("active_" + i); i++){
                                JSONObject par_notification = new JSONObject(active_notifications.get("active_" + i).toString());
                                System.out.println("active_" +i + " - " + par_notification.getString("tickerText"));
                            }

                        } else if(received.has("tickerText")){
                            System.out.println("Message: " + received.get("tickerText"));

                            String[] processName = {"notify-send", received.get("package").toString(), received.get("tickerText").toString()};
                            Process myProcess = Runtime.getRuntime().exec(processName);
                        } else {
                            System.out.println("nop");
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }

                server.close();

            }catch(SocketTimeoutException s) {
                System.out.println("Socket timed out! Trying again.");
                run();
            }catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String [] args) {
        int port = 3843;
        try {
            Thread t = new Server(port);
            t.start();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}
