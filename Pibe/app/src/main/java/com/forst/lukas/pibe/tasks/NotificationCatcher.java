package com.forst.lukas.pibe.tasks;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import org.json.JSONObject;

public class NotificationCatcher extends NotificationListenerService {
    private final String NOTIFICATION_RECEIVED
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_RECEIVED";

    public NotificationCatcher() {

    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Intent it = new Intent(NOTIFICATION_RECEIVED);
        try {
            JSONObject notification = new JSONObject();
            notification.put("package", sbn.getPackageName());
            notification.put("tickerText", sbn.getNotification().tickerText);
            notification.put("id", sbn.getId());
            notification.put("category", sbn.getNotification().category);
            notification.put("onPostTime", sbn.getPostTime());

           /* notification.put("large_icon", sbn.getNotification().getLargeIcon());
            notification.put("icon_small", sbn.getNotification().getSmallIcon());*/
            // TODO: 25.3.17 Icons

            it.putExtra("json", notification.toString());
            sendBroadcast(it);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
