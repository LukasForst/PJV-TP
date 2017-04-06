package com.forst.lukas.pibe.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 *  ServerCommunication class provides methods for communication between device, MySQL and laptop.
 *  @author Lukas Forst
 */

public class ServerCommunication {
    public static int TIME_OUT = 3000;

    private static boolean isSendingEnabled = false;
    private static boolean isReady = false;
    private static String serverAddress = null;
    private static int port = -1;

    private ProgressDialog progressDialog;
    private Handler mHandler;

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getPort() {
        return port;
    }

    public static boolean isReady() {
        return isReady;
    }

    /**
     * Enables sending notifications to the computer.
     */
    public static void setSendingEnabled(boolean isSendingEnabled) {
        ServerCommunication.isSendingEnabled = isSendingEnabled;
        Log.i("SettingsFragment", "Sending is now " + isSendingEnabled);
    }

    /**
     * @param json JSONObject which will be send to the computer
     */
    public void sendJSON(JSONObject json) {
        if (!isSendingEnabled) {
            Log.i("ServerCommunication", "Sending is disabled.");
        } else if (!isReady || serverAddress == null || port == -1) {
            Log.e("ServerCommunication", "Communication is not ready yet!");
        } else {
            new Send().execute(json);
        }
    }

    /**
     * Verify given IP Address and Port
     *
     * @param ipAddress IP Address of the server
     * @param port      Port
     */
    public void testConnection(ProgressDialog progressDialog, String ipAddress, String port) {
        this.progressDialog = progressDialog;
        mHandler = new Handler();
        new TestConnection(ipAddress, port).execute();
    }

    // Async task which will send JSON
    private class Send extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            try {
                Socket client = new Socket(serverAddress, port);

                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter bw = new BufferedWriter(out);

                bw.write(params[0].toString());
                bw.flush();

                client.close();
                Log.i("sendJSON", "successfully sent");
            } catch (Exception e) {
                Log.i("sendJSON", e.getMessage());
            }
            return null;
        }
    }

    // Async task which will try to connect to the server
    private class TestConnection extends AsyncTask<View, Void, Boolean> {
        private String tmpIPAddress;
        private String tmpPort;

        private TestConnection(String ipAddress, String port) {
            this.tmpIPAddress = ipAddress;
            this.tmpPort = port;
        }

        @Override
        protected Boolean doInBackground(View... params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });

            if (!testIP(tmpIPAddress) || !testPort(tmpPort)) return false;
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(tmpIPAddress, Integer.parseInt(tmpPort)), TIME_OUT);
                socket.close();
                return true;
            } catch (Exception e) {
                Log.e("TestConnection", e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isReady = aBoolean;
            Log.i("isReady", "" + isReady);
            if (aBoolean) {
                serverAddress = tmpIPAddress;
                port = Integer.parseInt(tmpPort);
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.hide();
                }
            });
        }

        private boolean testPort(String port) {
            //Not numbers handle exception
            //Wrong numbers handle somethingIsDefinitelyWrong
            try {
                int pt = Integer.parseInt(port);
                if (pt > 65534 || pt < 0) return false;
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        private boolean testIP(String ipAddress) {
            String[] ipArray = ipAddress.split(Pattern.quote("."));

            //Empty strings or wrong length of array
            if (ipAddress.equals("") || ipArray.length != 4) {
                return false;
            }
            try {
                for (String anIpArray : ipArray) {
                    int t = Integer.parseInt(anIpArray);
                    if (t > 255 || t < 0) return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

    }
}

