package com.forst.lukas.pibe.tasks;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.data.PibeData;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class provide service that listens to the notifications and then send them
 * via broadcast to the other classes.<br>
 * Sending to the computer via LAN can be turned off / on by modifying boolean <i><b>canSendNotification</b></i>.<br>
 *
 *<br>
 * <i>There's bug present in the Android since version 4.4. (reported 2013) - tested on version 7.1.2 -
 * when method <i><b>onNotificationPosted</b></i> is NOT called when was the application updated!<br>
 * <br>Total time wasted while solving this <b>f*cking</b> thing here: <b>eternity!</b></i>
 * @see  <a href="https://code.google.com/p/android/issues/detail?can=2&start=0&num=100&q=&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars&groupby=&sort=&id=62811">Bug discussion</a>
 * @author Lukas Forst
 * */
public class Catcher extends NotificationListenerService {
    //rename class every time when updating
    //final name is Catcher

    private final String TAG = this.getClass().getSimpleName();

    private CommandReceiver commandReceiver;

    public Catcher() {
        //public constructor is compulsory
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //register commandReceiver - used for sending commands to the Catcher
        commandReceiver = new CommandReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PibeData.NOTIFICATION_REQUEST);
        registerReceiver(commandReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(commandReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (!canSendNotification(sbn)) return;

        // Parse received notification to the JSON
        Intent it = new Intent(PibeData.NOTIFICATION_EVENT);
        try {
            //testing permission
            if (getApplicationName(sbn.getPackageName()).equals(getString(R.string.app_name))) {
                PibeData.setPermission(true);
                // Permission notification
                if (sbn.getNotification().tickerText.equals("permission_test")) {
                    NotificationManager nm = (NotificationManager)
                            getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel(sbn.getId());
                    PibeData.setTestNotificationArrived(true);
                    return;
                }
            }

            JSONObject notification = parseNotification(sbn);
            it.putExtra("json_received", notification.toString());

            //Send JSON to the server
            sendToTheServer(notification);

            //Testing purpose
            Log.i(TAG, "JSON: " + notification.toString());
        } catch (JSONException e) {
            Log.i(TAG, "JSONException - " + e.getMessage());
            return;
        }
        //Get all present notifications (JSON) and put them to the intent
        JSONObject activeNotifications = getAllActiveNotifications();
        if (activeNotifications != null) {
            it.putExtra("json_active", getAllActiveNotifications().toString());
        }
        sendBroadcast(it);
    }

    private boolean canSendNotification(StatusBarNotification sbn) {
        if (!PibeData.isNotificationCatcherEnabled()) {
            Log.w(TAG, "Catcher is disabled!");
            return false;
        }
        // Filtering some empty notifications coming from the system
        if (sbn.getNotification().category != null
                && sbn.getNotification().category.equals("sys")) {
            return false;
        } else if (sbn.getNotification().tickerText == null
                || sbn.getNotification().tickerText.equals("")) {
            return false;
        }

        String appName = getApplicationName(sbn.getPackageName());
        if (PibeData.getFilteredApps().contains(appName)) {
            Log.i(TAG, appName + " is filtered");
            return false;
        }

        return true;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        if (!PibeData.isNotificationCatcherEnabled()) return;

        Intent it = new Intent(PibeData.NOTIFICATION_EVENT);
        it.putExtra("json_active", getAllActiveNotifications().toString());

        sendBroadcast(it);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        PibeData.setPermission(true);
    }

    /**
     * Parses notification to the JSON.
     */
    private JSONObject parseNotification(StatusBarNotification sbn) throws JSONException {
        JSONObject notification = new JSONObject();
        notification
                .put("package", getApplicationName(sbn.getPackageName()))
                .put("tickerText", sbn.getNotification().tickerText)
                .put("id", sbn.getId())
                .put("category", sbn.getNotification().category)
                .put("onPostTime", sbn.getPostTime());
        // TODO: 25.3.17 Icons
        return notification;
    }

    /**
     * @param packageName package name of the application
     * @return String which is provided by PackageManager - Application Name
     *
     * example - from com.forst.lukas.pibe returns Pibe
     * */
    private String getApplicationName(String packageName){
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;

        try {
            ai = pm.getApplicationInfo( packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return  (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    /**
     * Parse current active notifications to the JSON format.
     * @return JSONObject with all active notifications
     * */
    private JSONObject getAllActiveNotifications() {
        JSONObject activeNotification = new JSONObject();

        //we can't handle it with i, because of possible errors while parsing
        int numberOfStoredNotifications = 0;
        StatusBarNotification[] active = getActiveNotifications().clone();

        for (StatusBarNotification anActive : active) {
            try {
                JSONObject currentNotification = parseNotification(anActive);

                activeNotification.put("active_" + numberOfStoredNotifications++,
                        currentNotification);
            } catch (JSONException e) {
                Log.i(TAG, "JSON - getPresentNotification - " + e.getMessage());
            }
        }

        return activeNotification;
    }

    /**
     * @param notification JSONObject which is supposed to be send to the connected computer
     * Exceptions are handled there.
     * */
    private void sendToTheServer(JSONObject notification){
        //first check if there's network connection
        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        if (mWifi.isConnected()) {
            PibeData.setWifiConnected(true);
            new ServerCommunication().sendJSON(notification);
        } else {
            PibeData.setWifiConnected(false);
            Log.w(TAG, "No WiFi connection");
        }

    }

    /**
     * Receiver used for receiving commands from all over the application -
     * typically request for active notifications.
     */
    class CommandReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!PibeData.hasPermission()) {
                Log.e(TAG, "Permission is - " + false);
                return;
            }

            if (intent.hasExtra("command") && intent.getStringExtra("command").equals("list")) {
                Intent it = new Intent(PibeData.NOTIFICATION_EVENT);
                it.putExtra("json_active", getAllActiveNotifications().toString());
                sendBroadcast(it);
            }
        }
    }

}
