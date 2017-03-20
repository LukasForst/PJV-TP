package com.forst.lukas.mytestreciverapp;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import org.json.JSONObject;

/**
 * Created by lukas on 15.3.17.
 */

public class NotificationCatcher extends NotificationListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Intent it = new Intent("com.forst.lukas.mytestreciverapp.NOTIFICATION_EVENT");
        try {
            JSONObject notification = new JSONObject();
            notification.put("package", sbn.getPackageName());
            notification.put("tickerText", sbn.getNotification().tickerText);
            notification.put("id", sbn.getId());
            notification.put("category", sbn.getNotification().category);
            notification.put("large_icon", sbn.getNotification().getLargeIcon());
            notification.put("icon_small", sbn.getNotification().getSmallIcon());
            notification.put("onPostTime", sbn.getPostTime());
            it.putExtra("json", notification.toString());
        } catch (Exception e){
            e.printStackTrace();
            it.putExtra("notification_posted", "ERROR");
        }
        sendBroadcast(it);
    }
}
