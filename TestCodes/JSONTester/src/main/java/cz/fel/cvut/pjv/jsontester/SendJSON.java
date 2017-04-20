package cz.fel.cvut.pjv.jsontester;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 @author Lukas Forst
 */
public class SendJSON {
    private String ipAddress;
    private int port;
    private final static Logger LOGGER = Logger.getLogger(SendJSON.class.getName());

    public SendJSON(int port) {
        this.ipAddress = "localhost";
        this.port = port;
    }

    public SendJSON(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }


    public void send(JSONObject jsonObject){
        Thread t = new Thread(new Send(jsonObject));
        t.start();
    }

    private class Send implements Runnable{
        JSONObject jsonObject;

        public Send(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public void run() {
            try(Socket clientSocket = new Socket(ipAddress, port);){
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(jsonObject.toString());
                clientSocket.close();
            } catch (Exception ignored){}
        }
    }
}
