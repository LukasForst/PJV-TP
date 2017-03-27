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
 * NotificationCatcher extends NotificationListenerService.
 *
 * This class provide service that listens to the notifications and then send them
 * via broadcast to the other classes.
 *
 * Sending to the computer via LAN can be turned off / on by modifying boolean isSendingEnabled.
 * @author Lukas Forst
 * */
public class NotificationCatcher extends NotificationListenerService {
    private final String NOTIFICATION_RECEIVED
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_RECEIVED";


    // TODO: 25.3.17 waiting for Database connect, to this is temporary
    private final String hostAddress = "192.168.1.97";
    private final int port = 3843;

    private static boolean isSendingEnabled = false;

    public NotificationCatcher() {
        //public constructor is compulsory
    }

    /**
     * Enables sending notifications to the computer.
     * */
    public static void setSendingEnabled(boolean isServiceEnabled) {
        NotificationCatcher.isSendingEnabled = isServiceEnabled;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        Intent it = new Intent(NOTIFICATION_RECEIVED);

        //Filtering some empty notifications coming from the system
        if(sbn.getNotification().category != null
                && sbn.getNotification().category.equals("sys")) return;

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
            if(isSendingEnabled) {
                sendToTheServer(notification);
            }
        } catch (Exception e){
            // TODO: 27.3.17 handle this exception
            e.printStackTrace();
            return;
        }
        //Get all present notifications (JSON) and put them to the intent
        it.putExtra("json_active", getAllActiveNotifications().toString());
        sendBroadcast(it);
    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

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
        for (int i = 0; i < getActiveNotifications().length; i++) {
            JSONObject currentNotification = new JSONObject();
            try{
                StatusBarNotification bar = getActiveNotifications()[i];
                currentNotification
                        .put("package", getApplicationName( bar.getPackageName()))
                        .put("tickerText", bar.getNotification().tickerText)
                        .put("id", bar.getId())
                        .put("category", bar.getNotification().category)
                        .put("onPostTime", bar.getPostTime());

                activeNotification.put("active_" + numberOfStoredNotifications++, currentNotification);
            } catch (JSONException e){
                Log.i("JSON", "getPresentNotification");
            }
        }

        return activeNotification;
    }

    /**
     * @param notification JSONObject which is supposed to be send to the connected computer
     * Exceptions are handled there.
     * */
    private void sendToTheServer(JSONObject notification){
        try{
            new ServerCommunication(hostAddress, port).execute(notification);
        } catch (Exception e){
            Log.i("ServerCommunication", e.getMessage());
        }
    }
}
