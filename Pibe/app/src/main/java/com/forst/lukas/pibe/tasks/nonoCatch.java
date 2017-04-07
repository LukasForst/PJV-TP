package com.forst.lukas.pibe.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class provide service that listens to the notifications and then send them
 * via broadcast to the other classes.<br>
 * Sending to the computer via LAN can be turned off / on by modifying boolean <i><b>isSendingEnabled</b></i>.<br>
 *
 *<br>
 * <i>There's bug present in the Android since version 4.4. (reported 2013) - tested on version 7.1.2 -
 * when method <i><b>onNotificationPosted</b></i> is NOT called when was the application updated!<br>
 * <br>Total time wasted while solving this <b>f*cking</b> thing here: <b>eternity!</b></i>
 * @see  <a href="https://code.google.com/p/android/issues/detail?can=2&start=0&num=100&q=&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars&groupby=&sort=&id=62811">Bug discussion</a>
 * @author Lukas Forst
 * */
public class nonoCatch extends NotificationListenerService {
    //rename class every time when updating
    //final name is NotificationCatcher

    private final static String NOTIFICATION_RECEIVED
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_RECEIVED";
    //Situation when it is not loaded onCreate in Main and notification arrives
    private static boolean isNotificationListenerEnabled = false;
    private final String TAG = this.getClass().getSimpleName();

    public nonoCatch() {
        //public constructor is compulsory
    }

    /**
     * Enables listening to the notifications.
     * */
    public static void setNotificationCatcherEnabled(boolean isNotificationListenerEnabled) {
        nonoCatch.isNotificationListenerEnabled = isNotificationListenerEnabled;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if (!isNotificationListenerEnabled) {
            Log.w(TAG, "Catcher is disabled!");
            return;
        }

        // Filtering some empty notifications coming from the system
        if(sbn.getNotification().category != null
                && sbn.getNotification().category.equals("sys")) {
            return;
        } else if (sbn.getNotification().tickerText == null
                || sbn.getNotification().tickerText.equals("")) {
            return;
        }

        // Parse received notification to the JSON
        Intent it = new Intent(NOTIFICATION_RECEIVED);
        try {
            if (getApplicationName(sbn.getPackageName()).equals(getString(R.string.app_name))) {
                MainActivity.PERMISSION_GRANTED = true;
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
        it.putExtra("json_active", getAllActiveNotifications().toString());
        sendBroadcast(it);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        if (!isNotificationListenerEnabled) return;

        Intent it = new Intent(NOTIFICATION_RECEIVED);
        it.putExtra("json_active", getAllActiveNotifications().toString());

        sendBroadcast(it);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        MainActivity.PERMISSION_GRANTED = true;
    }

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

                activeNotification.put("active_" + numberOfStoredNotifications++, currentNotification);
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
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        if (mWifi.isConnected()) {
            ServerCommunication.setWiFiConnected(true);
            new ServerCommunication().sendJSON(notification);
        } else {
            ServerCommunication.setWiFiConnected(false);
            Log.w(TAG, "No WiFi connection");
        }

    }


}
