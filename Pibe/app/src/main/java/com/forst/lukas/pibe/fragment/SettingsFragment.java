package com.forst.lukas.pibe.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.tasks.ServerCommunication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private EditText ipAddressText;
    private EditText portText;
    private Button connect;
    private ProgressBar progressBar;
    private ImageView okView;

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
        progressBar = (ProgressBar) inflatedView.findViewById(R.id.fragment_settings_progress_bar);
        okView = (ImageView) inflatedView.findViewById(R.id.fragment_settings_ok_image);

        setListeners();
        return inflatedView;
    }

    private void setListeners() {
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ServerCommunication().testConnection(
                        ipAddressText.getText().toString(),
                        portText.getText().toString());
                okView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String message;
                        if (ServerCommunication.isReady()) message = "Connection was successful";
                        else message = "Wrong IP address or port!";
                        Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progressBar.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                if (ServerCommunication.isReady()) {
                                    okView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }, ServerCommunication.TIME_OUT + 20);
            }
        });
    }
}
