package com.forst.lukas.pibe.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.forst.lukas.pibe.data.PibeData;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *  ServerCommunication class provides methods for communication between device, MySQL and laptop.
 *  @author Lukas Forst
 */

public class ServerCommunication {
    private final String TAG = this.getClass().getSimpleName();

    private PibeData pb;
    /**
     * @param json JSONObject which will be send to the computer
     */
    public void sendJSON(JSONObject json) {
        pb = PibeData.getInstance();
        //Testing purpose
        Log.i(TAG, "JSON: " + json.toString());

        if (!pb.isSendingEnabled()) {
            Log.w(TAG, "Sending is disabled.");
        } else if (!pb.isConnectionReady() ||
                pb.getIpAddress().equals("") ||
                pb.getPort() == -1) {
            Log.w(TAG, "Communication is not ready yet!");
        } else if (!pb.isWifiConnected()) {
            Log.w(TAG, "WiFi is not enabled!");
        } else {
            new Send().execute(json);
            pb.COUNTER++;
        }
    }

    // Async task which will send JSON
    private class Send extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            try {
                Socket client = new Socket(pb.getIpAddress(), pb.getPort());
                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter bw = new BufferedWriter(out);

                bw.write(params[0].toString());
                bw.flush();

                client.close();
                Log.i(TAG, "JSON successfully sent");
            } catch (IOException e) {
                Log.i(TAG, "IOException - " + e.getMessage());
                pb.setConnectionReady(false);
            }
            return null;
        }
    }
}

