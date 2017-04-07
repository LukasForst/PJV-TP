package com.forst.lukas.pibe.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.forst.lukas.pibe.fragment.SettingsFragment;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
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
    private static boolean isWiFiConnected = false;
    private static String serverAddress = null;
    private static int port = -1;
    private final String TAG = this.getClass().getSimpleName();
    private SettingsFragment fragment; // TODO: 6.4.17 try to make it more for general usage

    public static void setWiFiConnected(boolean isWiFiConnected) {
        ServerCommunication.isWiFiConnected = isWiFiConnected;
    }

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

    public static void resetData() {
        serverAddress = null;
        port = -1;
        isReady = false;
    }

    /**
     * @param json JSONObject which will be send to the computer
     */
    public void sendJSON(JSONObject json) {
        if (!isSendingEnabled) {
            Log.i(TAG, "Sending is disabled.");
        } else if (!isReady || serverAddress == null || port == -1) {
            Log.w(TAG, "Communication is not ready yet!");
        } else if (!isWiFiConnected) {
            Log.e(TAG, "WiFi is not enabled!");
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
    public void verifyGivenIPAndPort(Fragment fragment, String ipAddress, String port) {
        this.fragment = (SettingsFragment) fragment;

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
                Log.i(TAG, "JSON successfully sent");
            } catch (IOException e) {
                Log.i(TAG, "IOException - " + e.getMessage());
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
            if (!testIP(tmpIPAddress) || !testPort(tmpPort)) return false;
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(tmpIPAddress, Integer.parseInt(tmpPort)), TIME_OUT);
                socket.close();
                return true;
            } catch (Exception e) {
                Log.e(TAG, "TestConnection - " + e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isReady = aBoolean;
            Log.i(TAG, "isReady - " + isReady);
            if (aBoolean) {
                serverAddress = tmpIPAddress;
                port = Integer.parseInt(tmpPort);
            }

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (isReady()) {
                        fragment.connectionOK();
                    } else {
                        fragment.connectionError();
                    }
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

