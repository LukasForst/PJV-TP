
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * @[ile name:  Client
 * @date :       16.3.17
 * @author :     Lukas Forst
 * Package:    PACKAGE_NAME
 * Project:    ServerTest
 */

public class Client {

    public static void main(String [] args) {
        String serverName = "192.168.1.97";
        int port = 3843;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer); //what doest the sever says

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            try {
                JSONObject jsonObject = new JSONObject();
            } catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("Server says " + in.readUTF());
            client.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
