package com.forst.lukas.pibe.fragment;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.tasks.TestConnection;

/**
 * {@link Fragment} with settings.
 *
 * @author Lukas Forst
 */
public class SettingsFragment extends Fragment {
    private EditText ipAddressText;
    private EditText portText;
    private Button connectButton, testNotification;
    private ImageView okView;
    private ProgressDialog progressDialog;
    private Snackbar connectionInfoSnack;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_settings, container, false);

        ipAddressText = (EditText)
                inflatedView.findViewById(R.id.fragment_settings_IP_address_set);
        portText = (EditText)
                inflatedView.findViewById(R.id.fragment_settings_port_set);
        connectButton = (Button)
                inflatedView.findViewById(R.id.fragment_settings_connect_button);
        testNotification = (Button)
                inflatedView.findViewById(R.id.fragment_settings_test_notification_button);
        okView = (ImageView) inflatedView.findViewById(R.id.fragment_settings_ok_image);

        progressDialog = new ProgressDialog(inflatedView.getContext());
        connectionInfoSnack = Snackbar.make(
                inflatedView, "Wrong IP address or port!", Snackbar.LENGTH_LONG)
                .setAction("Action", null);

        //load last used texts
        ipAddressText.setText(PibeData.getIpAddress());
        String text =
                PibeData.getPort() == -1 ? "" : String.valueOf(PibeData.getPort());
        portText.setText(text);


        if (PibeData.isConnectionReady()) {
            setGUIConnectionOK();
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
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //reset data or verify current settings
                if (PibeData.isConnectionReady()) {
                    okView.setVisibility(View.INVISIBLE);
                    testNotification.setVisibility(View.INVISIBLE);
                    ipAddressText.setEnabled(true);
                    portText.setEnabled(true);
                    connectButton.setText("Connect");
                    // TODO: 7.4.17 switch has to be turned off
                    PibeData.resetData();
                } else {
                    verifyConnection();
                }
            }
        });

        testNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(v.getContext());
                mBuilder.setSmallIcon(R.mipmap.main_icon);
                mBuilder.setContentTitle(getString(R.string.app_name));
                mBuilder.setTicker("Test notification - Do you see me?");

                NotificationManager manager = (NotificationManager)
                        v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify((int) System.currentTimeMillis(), mBuilder.build());
            }
        });
    }

    /**
     * connectionOK sets GUI to the OK state
     */
    public void connectionOK() {
        progressDialog.dismiss();
        connectionInfoSnack.setText("Connection was successful");
        connectionInfoSnack.show();

        setGUIConnectionOK();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ipAddress", String.valueOf(ipAddressText.getText()));
        editor.putInt("port", Integer.parseInt(String.valueOf(portText.getText())));
        editor.apply();
    }

    public void connectionError() {
        progressDialog.dismiss();
        connectionInfoSnack.setText("Wrong IP address or port!");
        connectionInfoSnack.show();
    }

    private void setGUIConnectionOK() {
        ipAddressText.setText(PibeData.getIpAddress());
        ipAddressText.setEnabled(false);
        portText.setText(String.valueOf(PibeData.getPort()));
        portText.setEnabled(false);

        okView.setVisibility(View.VISIBLE);
        testNotification.setVisibility(View.VISIBLE);

        connectButton.setText("Reset data");
    }

    private void verifyConnection() {
        // Test connection -> verify IP and port
        progressDialog.show();
        new TestConnection(
                this, ipAddressText.getText().toString(), portText.getText().toString())
                .execute();
    }
}
