package com.forst.lukas.pibe.tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.data.PibeData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class which provide ability of permission checking.
 *
 * @author Luaks Forst
 */

public class NotificationPermission {
    private static final int DEFAULT_DELAY = 1000;
    private final String TAG = this.getClass().getSimpleName();

    public void checkPermission(final Context context) {
        checkPermission(context, DEFAULT_DELAY);
    }

    public void checkPermission(final Context context, int delay) {
        final int testNotificationID = (int) System.currentTimeMillis();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context.getApplicationContext());
                mBuilder.setSmallIcon(R.mipmap.main_icon);
                mBuilder.setContentTitle(context.getString(R.string.app_name));
                mBuilder.setTicker("permission_test");

                NotificationManager manager = (NotificationManager)
                        context.getApplicationContext()
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(testNotificationID, mBuilder.build());
            }
        }, delay);

        Timer next = new Timer();
        next.schedule(new TimerTask() {
            @Override
            public void run() {
                if (PibeData.hasTestNotificationArrived()) {
                    PibeData.setNotificationPermission(true);
                    Log.i(TAG, "Granted");
                } else {
                    PibeData.setNotificationPermission(false);
                    NotificationManager nm = (NotificationManager)
                            context.getApplicationContext().
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel(testNotificationID);
                    Log.i(TAG, "ERROR");
                }
            }
        }, delay + 200);
    }
}
