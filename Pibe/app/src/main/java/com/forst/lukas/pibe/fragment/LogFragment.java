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

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {
    private TextView logText;

    private NotificationReceiver notificationReceiver;

    public NotificationReceiver getNotificationReceiver() {
        return notificationReceiver;
    }

    public LogFragment() {
        // Required empty public constructor
        notificationReceiver = new NotificationReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_log, container, false);
        logText = (TextView) inflatedView.findViewById(R.id.fragment_log_text);

        return inflatedView;
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            JSONObject jsonObject = null;
            try{
                jsonObject = new JSONObject(intent.getStringExtra("json"));
                Log.i("json", jsonObject.toString());
            } catch (Exception e){
                Log.i("json error", e.getMessage());
            }
            if(jsonObject != null){
                try{
                    logText.setText(logText.getText() + "\n" + jsonObject.getString("package")
                            + " - " + jsonObject.getString("tickerText"));
                } catch (Exception e){
                    Log.i("JSONException", e.getMessage());
                }
            }
        }
    }

}
