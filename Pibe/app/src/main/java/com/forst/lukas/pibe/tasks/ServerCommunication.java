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
    /**
     * @param json JSONObject which will be send to the computer
     */
    public void sendJSON(JSONObject json) {
        if (!PibeData.isSendingEnabled()) {
            Log.w(TAG, "Sending is disabled.");
        } else if (!PibeData.isConnectionReady() ||
                PibeData.getIpAddress().equals("") ||
                PibeData.getPort() == -1) {
            Log.w(TAG, "Communication is not ready yet!");
        } else if (!PibeData.isWifiConnected()) {
            Log.w(TAG, "WiFi is not enabled!");
        } else {
            new Send().execute(json);
            PibeData.COUNTER++;
        }
    }

    // Async task which will send JSON
    private class Send extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            try {
                Socket client = new Socket(PibeData.getIpAddress(), PibeData.getPort());
                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter bw = new BufferedWriter(out);

                bw.write(params[0].toString());
                bw.flush();

                //Testing purpose
                Log.i(TAG, "JSON: " + params[0].toString());

                client.close();
                Log.i(TAG, "JSON successfully sent");
            } catch (IOException e) {
                Log.i(TAG, "IOException - " + e.getMessage());
                PibeData.setConnectionReady(false);
            }
            return null;
        }
    }
}

