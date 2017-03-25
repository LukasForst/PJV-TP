package com.forst.lukas.pibe.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author Lukas Forst
 * @[ile_name ServerComunication
 * @date 25.3.17
 * @package com.forst.lukas.pibe.tasks
 * @project Pibe
 */

public class ServerComunication extends AsyncTask<JSONObject, Void, Void> {
    private String serverAddress;
    private int port;

    public ServerComunication(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public ServerComunication(String userName, String password){
        port = getPortFromDatabase(userName, password);
        serverAddress = getServerAddressFromDatabase(userName, password);
    }

    @Override
    protected Void doInBackground(JSONObject... params) {
        sendJSON(params[0]);
        return null;
    }

    private boolean sendJSON(JSONObject jsonObject) {
        try{
            Socket client = new Socket(serverAddress, port);

            OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
            BufferedWriter bw = new BufferedWriter(out);

            Log.i("sendJSON", "successfully connected ");

            bw.write(jsonObject.toString());
            bw.flush();

            client.close();
            return true;
        } catch (Exception e){
            Log.i("sendJSON", e.getMessage());
            return false;
        }
    }

    private int getPortFromDatabase(String userName, String password){
        int port = 0; // TODO: 25.3.17 get data from database

        return port;
    }

    private String getServerAddressFromDatabase(String userName, String password){
        String serverAddress = " "; // TODO: 25.3.17 get String from database

        return serverAddress;
    }
}
