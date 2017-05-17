package com.forst.lukas.pibe.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.data.PibeConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link Fragment} class which provide Log screen with two main elements - whole notification log
 * and current present notification.
 * @author Lukas Forst
 */
public class LogFragment extends Fragment {

    private NotificationReceiver notificationReceiver;
    private TextView logText;
    private TextView activeNotificationText;
    private String activeNotificationString = null;
    private String savedData = null;

    public LogFragment() {
        // Required empty public constructor
        notificationReceiver = new NotificationReceiver();
    }

    /**
     * @return {@link NotificationReceiver} which updates log in the fragment.
     */
    public NotificationReceiver getNotificationReceiver() {
        return notificationReceiver;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_log, container, false);

        // Init TextViews
        logText = (TextView) inflatedView.findViewById(R.id.fragment_log_text);
        activeNotificationText = (TextView) inflatedView.findViewById(R.id.fragment_log_active);

        if(savedData != null){ // restore saved data - when is fragment reloaded
            logText.setText(savedData);
        }
        if(activeNotificationString != null){
            activeNotificationText.setText(activeNotificationString);
        } else {
            Intent it = new Intent(PibeConfiguration.NOTIFICATION_REQUEST);
            it.putExtra("command", "list");
            getActivity().sendBroadcast(it);
        }
        return inflatedView;
    }

    /**
     * Updates log according to the received {@link Intent}
     */
    private void updateLog(Intent intent) {
        // New notification appeared
        // Case when this fragment haven't been created yet
        if (intent.hasExtra("json_received") && logText != null) {
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("json_received"));
                Log.i("JSON", jsonObject.toString());
                savedData = logText.getText() + "\n" + jsonObject.getString("package")
                        + " - " + jsonObject.getString("tickerText");
                logText.setText(savedData);
            } catch (JSONException e) {
                Log.i("JSONException", e.getMessage());
            }
        }
    }

    /**
     * Updates log with Active Notifications.
     */
    private void showActiveNotifications(Intent intent) {
        if (intent.hasExtra("json_active") && activeNotificationText != null) {
            try {
                JSONObject activeNotification = new JSONObject(
                        intent.getStringExtra("json_active"));

                StringBuilder sb = new StringBuilder();
                for (int i = 0; activeNotification.has("active_" + i); i++) {
                    JSONObject current = activeNotification.getJSONObject("active_" + i);

                    if (!current.has("tickerText")) continue; //notifications without text

                    sb.append("\n")
                            .append(current.getString("package"))
                            .append(" - ")
                            .append(current.getString("tickerText"));
                }
                activeNotificationString = sb.toString();
                activeNotificationText.setText(activeNotificationString);

            } catch (JSONException e) {
                Log.i("JSONEx", e.getMessage());
            }
        }
    }

    /**
     * Broadcast receiver that handle received notifications.
     */
    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateLog(intent);
            showActiveNotifications(intent);
        }
    }

}
