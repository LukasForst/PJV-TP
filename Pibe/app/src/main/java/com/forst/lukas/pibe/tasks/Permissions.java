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
import com.forst.lukas.pibe.data.AppConfig;
import com.forst.lukas.pibe.data.PibeConfiguration;

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

    private Activity activity;
    private PibeConfiguration pb;

    public Permissions(Activity activity) {
        pb = AppConfig.getInstance();
        this.activity = activity;
    }

    public void checkNotificationPermission() {
        checkNotificationPermission(DEFAULT_DELAY);
    }

    public void checkNotificationPermission(int delay) {
        final Context context = activity.getApplicationContext();
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
                if (pb.hasTestNotificationArrived()) {
                    pb.setNotificationPermission(true);
                } else {
                    pb.setNotificationPermission(false);
                    NotificationManager nm = (NotificationManager)
                            context.getApplicationContext().
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel(testNotificationID);
                }
                Log.i(TAG, "NotificationPermission - " + pb.hasTestNotificationArrived());
            }
        }, delay + 200);
    }

    public boolean checkReadPhoneStatePermissions() {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (pb.hasReadPhoneStatePermission())
                pb.setReadPhoneStatePermission(false);
            return false;
        } else {
            if (!pb.hasReadPhoneStatePermission())
                pb.setReadPhoneStatePermission(true);
            return true;
        }
    }

    public void askForReadPhoneStatePermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                MainActivity.PERMISSION_REQUEST_READ_PHONE_STATE);
    }

    public boolean checkContactReadPermission() {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (pb.hasReadContactsPermission())
                pb.setReadContactsPermission(false);
            return false;
        } else {
            if (!pb.hasReadContactsPermission())
                pb.setReadContactsPermission(true);
            return true;
        }
    }

    public void askForContactReadPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_CONTACTS},
                MainActivity.PERMISSION_REQUEST_READ_CONTACTS);
    }

    public boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void askForCameraPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                MainActivity.PERMISSION_REQUEST_CAMERA);
    }

    public boolean checkAllPermissions() {
        return checkContactReadPermission()
                && checkReadPhoneStatePermissions()
                && checkCameraPermission();
    }
}
