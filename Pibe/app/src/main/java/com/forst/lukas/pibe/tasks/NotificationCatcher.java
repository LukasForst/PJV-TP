package com.forst.lukas.pibe.tasks;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class provide service that listens to the notifications and then send them
 * via broadcast to the other classes.<br>
 * Sending to the computer via LAN can be turned off / on by modifying boolean isSendingEnabled.
 * @author Lukas Forst
 * */
public class NotificationCatcher extends NotificationListenerService {
    //Situation when it is not loaded onCreate in Main and notification arrives
    private static boolean isNotificationListenerEnabled = false;

    private final String NOTIFICATION_RECEIVED
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_RECEIVED";

    public NotificationCatcher() {
        //public constructor is compulsory

    }

    /**
     * Enables listening to the notifications.
     * */
    public static void setNotificationCatcherEnabled(boolean isNotificationListenerEnabled) {
        NotificationCatcher.isNotificationListenerEnabled = isNotificationListenerEnabled;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if (!isNotificationListenerEnabled) return;
        Intent it = new Intent(NOTIFICATION_RECEIVED);

        //Filtering some empty notifications coming from the system
        if(sbn.getNotification().category != null
                && sbn.getNotification().category.equals("sys")) {
            return;
        } else if (sbn.getNotification().tickerText == null
                || sbn.getNotification().tickerText.equals("")) {
            return;
        }

        //Parse received notification to the JSON
        try {
            JSONObject notification = new JSONObject();
            notification
                    .put("package", getApplicationName(sbn.getPackageName()))
                    .put("tickerText", sbn.getNotification().tickerText)
                    .put("id", sbn.getId())
                    .put("category", sbn.getNotification().category)
                    .put("onPostTime", sbn.getPostTime());

            // TODO: 25.3.17 Icons

            it.putExtra("json_received", notification.toString());

            //Send JSON to the server
            sendToTheServer(notification);

            //Testing purpose
            Log.i("JSON", notification.toString());
        } catch (Exception e) {
            Log.i("JSONException", e.getMessage());
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

        for (int i = 0; i < active.length; i++) {
            JSONObject currentNotification = new JSONObject();
            try {
                StatusBarNotification currentNotif = active[i];
                currentNotification
                        .put("package", getApplicationName(currentNotif.getPackageName()))
                        .put("tickerText", currentNotif.getNotification().tickerText)
                        .put("id", currentNotif.getId())
                        .put("category", currentNotif.getNotification().category)
                        .put("onPostTime", currentNotif.getPostTime());

                activeNotification.put("active_" + numberOfStoredNotifications++, currentNotification);
            } catch (JSONException e) {
                Log.i("JSON", "getPresentNotification " + e.getMessage());
            }
        }

        return activeNotification;
    }

    /**
     * @param notification JSONObject which is supposed to be send to the connected computer
     * Exceptions are handled there.
     * */
    private void sendToTheServer(JSONObject notification){
        new ServerCommunication().sendJSON(notification);
    }
}
