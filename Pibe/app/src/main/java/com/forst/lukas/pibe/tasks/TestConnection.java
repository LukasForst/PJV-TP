package com.forst.lukas.pibe.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.fragment.SettingsFragment;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 * Created by lukas on 14/04/17.
 */

// Async task which will try to connect to the server
public class TestConnection extends AsyncTask<Void, Void, Boolean> {
    private final int TIME_OUT = 3000;
    private final String TAG = this.getClass().getSimpleName();

    private String ipAddress;
    private String port;
    private SettingsFragment settingsFragment;

    public TestConnection(SettingsFragment settingsFragment, String ipAddress, String port) {
        this.settingsFragment = settingsFragment;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (!testIP(ipAddress) || !testPort(port)) return false;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ipAddress, Integer.parseInt(port)), TIME_OUT);
            socket.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "TestConnection - " + e.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        PibeData.setConnectionReady(aBoolean);
        Log.i(TAG, "isReady - " + PibeData.isConnectionReady());

        if (aBoolean) {
            PibeData.setIPAndPort(ipAddress, Integer.parseInt(port));
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (aBoolean) {
                    settingsFragment.connectionOK();
                } else {
                    settingsFragment.connectionError();
                }
            }
        });
    }

    private boolean testPort(String port) {
        try {
            int pt = Integer.parseInt(port);
            if (pt > 65534 || pt < 0)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean testIP(String ipAddress) {
        final String[] ipArray = ipAddress.split(Pattern.quote("."));

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
