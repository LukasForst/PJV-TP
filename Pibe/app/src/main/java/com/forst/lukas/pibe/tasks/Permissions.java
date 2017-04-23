package com.forst.lukas.pibe.tasks;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.activity.MainActivity;
import com.forst.lukas.pibe.data.PibeData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class which provide ability of permission checking.
 *
 * @author Lukas Forst
 */

public class Permissions {
    private static final int DEFAULT_DELAY = 1000;
    private final String TAG = this.getClass().getSimpleName();

    public void checkNotificationPermission(final Context context) {
        checkNotificationPermission(context, DEFAULT_DELAY);
    }

    public void checkNotificationPermission(final Context context, int delay) {
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
                } else {
                    PibeData.setNotificationPermission(false);
                    NotificationManager nm = (NotificationManager)
                            context.getApplicationContext().
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel(testNotificationID);
                }
                Log.i(TAG, "NotificationPermission - " + PibeData.hasTestNotificationArrived());
            }
        }, delay + 200);
    }

    public boolean checkReadPhoneStatePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MainActivity.PERMISSION_REQUEST_READ_PHONE_STATE);
            return false;
        } else {
            PibeData.setReadPhoneStatePermission(true);
            return true;
        }
    }

    public boolean checkContactReadPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MainActivity.PERMISSION_REQUEST_READ_CONTACTS);
            return false;
        } else {
            PibeData.setReadContactsPermission(true);
            return true;
        }
    }
}
