package com.forst.lukas.pibe.fragment;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.tasks.ServerCommunication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * {@link Fragment} with settings.
 *
 * @author Lukas Forst
 */
public class SettingsFragment extends Fragment {
    private EditText ipAddressText;
    private EditText portText;
    private Button connect, testNotification;
    private ImageView okView;
    private ProgressDialog progressDialog;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_settings, container, false);

        ipAddressText = (EditText) inflatedView.findViewById(R.id.fragment_settings_IP_address_set);
        portText = (EditText) inflatedView.findViewById(R.id.fragment_settings_port_set);
        connect = (Button) inflatedView.findViewById(R.id.fragment_settings_connect_button);
        testNotification = (Button) inflatedView.findViewById(R.id.fragment_settings_test_notification_button);
        okView = (ImageView) inflatedView.findViewById(R.id.fragment_settings_ok_image);

        progressDialog = new ProgressDialog(inflatedView.getContext());

        if (ServerCommunication.isReady()) {
            setGUIConnectionIsOnline();
        }

        setDialog();
        setListeners();

        return inflatedView;
    }

    private void setDialog() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Connecting to the database...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setListeners() {
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                verifyConnection(v);
            }
        });

        testNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(v.getContext());
                mBuilder.setSmallIcon(R.mipmap.main_icon);
                mBuilder.setContentTitle("Pibe"); // TODO: 5.4.17 app name from strings
                mBuilder.setTicker("Test notification - Do you see me?");

                NotificationManager manager = (NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());
            }
        });
    }

    private void setGUIConnectionIsOnline() {
        ipAddressText.setText(ServerCommunication.getServerAddress());
        ipAddressText.setEnabled(false);
        portText.setText(String.valueOf(ServerCommunication.getPort()));
        portText.setEnabled(false);

        okView.setVisibility(View.VISIBLE);
        testNotification.setVisibility(View.VISIBLE);
    }

    private void verifyConnection(final View v) {
        // Test connection -> verify IP and port
        new ServerCommunication().testConnection(
                ipAddressText.getText().toString(),
                portText.getText().toString());

        // Show progress bar
        okView.setVisibility(View.GONE);
        progressDialog.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String message;
                if (ServerCommunication.isReady()) message = "Connection was successful";
                else message = "Wrong IP address or port!";

                Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Invoke GUI from main thread
                okView.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (ServerCommunication.isReady()) {
                            setGUIConnectionIsOnline();
                        }
                        progressDialog.hide();
                    }
                });
            }
        }, ServerCommunication.TIME_OUT + 20);

    }
}
