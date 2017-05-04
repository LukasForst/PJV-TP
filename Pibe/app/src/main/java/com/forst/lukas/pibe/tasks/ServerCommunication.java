package com.forst.lukas.pibe.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.forst.lukas.pibe.data.AppConfig;
import com.forst.lukas.pibe.data.PibeConfiguration;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *  ServerCommunication class provides methods for communication between device and laptop.
 *  @author Lukas Forst
 */

public class ServerCommunication {
    private final String TAG = this.getClass().getSimpleName();

    private PibeConfiguration pc;
    /**
     * @param json JSONObject which will be send to the computer
     */
    public void sendJSON(JSONObject json) {
        pc = AppConfig.getInstance();
        //Testing purpose
        Log.i(TAG, "JSON: " + json.toString());

        if (!pc.isSendingEnabled()) {
            Log.w(TAG, "Sending is disabled.");
        } else if (!pc.isConnectionReady() ||
                pc.getIpAddress().equals("") ||
                pc.getPort() == -1) {
            Log.w(TAG, "Communication is not ready yet!");
        } else if (!pc.isWifiConnected()) {
            Log.w(TAG, "WiFi is not enabled!");
        } else {
            new Send().execute(json);
            pc.COUNTER++;
        }
    }

    /**
     * Async task which will send JSON
     */
    private class Send extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            try {
                Socket client = new Socket(pc.getIpAddress(), pc.getPort());
                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter bw = new BufferedWriter(out);

                bw.write(params[0].toString());
                bw.flush();

                client.close();
                Log.i(TAG, "JSON successfully sent");
            } catch (IOException e) {
                Log.i(TAG, "IOException - " + e.getMessage());
                pc.setConnectionReady(false);
            }
            return null;
        }
    }
}

