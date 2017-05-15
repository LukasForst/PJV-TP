package com.forst.lukas.pibe.fragment;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.activity.QRScanActivity;
import com.forst.lukas.pibe.data.AppConfig;
import com.forst.lukas.pibe.data.AppPreferences;
import com.forst.lukas.pibe.data.PibeConfiguration;
import com.forst.lukas.pibe.tasks.Permissions;
import com.forst.lukas.pibe.tasks.TestConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Fragment} with application settings and connection to the server.
 *
 * @author Lukas Forst
 */
public class SettingsFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private EditText ipAddressText;
    private EditText portText;
    private Button connectButton, testNotification, qrScanButton;
    private ImageView okView;
    private ProgressDialog progressDialog;
    private ListView historyListView;
    private Snackbar connectionInfoSnack;

    private PibeConfiguration pb;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pb = AppConfig.getInstance();
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
        qrScanButton = (Button) inflatedView.findViewById(R.id.fragment_settings_qr_scann);
        okView = (ImageView) inflatedView.findViewById(R.id.fragment_settings_ok_image);
        historyListView = (ListView) inflatedView.findViewById(R.id.fragment_settings_listview);

        progressDialog = new ProgressDialog(inflatedView.getContext());
        connectionInfoSnack = Snackbar.make(
                inflatedView, "Wrong IP address or port!", Snackbar.LENGTH_LONG)
                .setAction("Action", null);

        //load last used texts
        ipAddressText.setText(pb.getIpAddress());
        String text =
                pb.getPort() == -1 ? "" : String.valueOf(pb.getPort());
        portText.setText(text);


        if (pb.isConnectionReady()) {
            setGUIConnectionOK();
        }

        prepareDialog();
        setListeners();
        initHistoryListView();

        return inflatedView;
    }

    /**
     * Show history of used IPs and ports.
     */
    private void initHistoryListView() {
        List<String> ips = new ArrayList<>();
        ips.addAll(pb.getLastUsedIPsAndPorts().keySet());
        ListAdapter lisViewAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, ips);
        historyListView.setAdapter(lisViewAdapter);
    }


    /**
     * Prepare dialog with progressbar when connecting to the server.
     */
    private void prepareDialog() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Connecting to the server...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Attach listeners to the buttons.
     */
    private void setListeners() {
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //reset data or verify current settings
                if (pb.isConnectionReady()) {
                    okView.setVisibility(View.INVISIBLE);
                    testNotification.setVisibility(View.INVISIBLE);
                    ipAddressText.setEnabled(true);
                    portText.setEnabled(true);
                    historyListView.setVisibility(View.VISIBLE);
                    connectButton.setText("Connect");

                    pb.resetData();
                    pb.aSwitch.setChecked(false);
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

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                ipAddressText.setText(item);
                portText.setText(String.valueOf(pb.getLastUsedIPsAndPorts().get(item)));
            }
        });

        qrScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions p = new Permissions(getActivity());
                if (p.checkCameraPermission()) {
                    launchQR();
                } else p.askForCameraPermission();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == 100) {
                try {
                    Log.i(TAG, "resultCode = " + resultCode);
                    Log.i(TAG, "IP " + data.getStringExtra("IP"));
                    Log.i(TAG, "PORT " + data.getIntExtra("port", -1));
                    ipAddressText.setText(data.getStringExtra("IP"));
                    if (data.getIntExtra("port", -1) != -1) {
                        portText.setText(String.valueOf(data.getIntExtra("port", -1)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == 101) {
                Toast.makeText(getContext(), "QR Code is not valid!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "QR Code is not valid!");
            } else {
                Log.i(TAG, "Resultcode: " + resultCode);
            }
        }

    }
    /**
     * Launch QR Activity from {@link com.forst.lukas.pibe.activity.MainActivity} or from this fragment.
     * */
    public void launchQR() {
        startActivityForResult(new Intent(getActivity(), QRScanActivity.class), 1);
    }

    /**
     * connectionOK sets GUI to the OK state and show {@link Snackbar}
     */
    public void connectionOK() {
        progressDialog.dismiss();
        connectionInfoSnack.setText("Connection was successful");
        connectionInfoSnack.show();

        setGUIConnectionOK();
        new AppPreferences(getActivity()).savePreferences();
    }
    /**
     * connectionError sets GUI to the Error state
     */
    public void connectionError() {
        progressDialog.dismiss();
        connectionInfoSnack.setText("Wrong IP address or port!");
        connectionInfoSnack.show();
    }

    /**
     * connectionOK sets GUI to the OK state
     */
    private void setGUIConnectionOK() {
        ipAddressText.setText(pb.getIpAddress());
        ipAddressText.setEnabled(false);
        portText.setText(String.valueOf(pb.getPort()));
        portText.setEnabled(false);
        historyListView.setVisibility(View.GONE);

        okView.setVisibility(View.VISIBLE);
        testNotification.setVisibility(View.VISIBLE);

        connectButton.setText("Reset data");
    }

    /**
     * Verify connection to the server.
     * */
    private void verifyConnection() {
        // Test connection -> verify IP and port
        progressDialog.show();
        new TestConnection(
                this, ipAddressText.getText().toString(), portText.getText().toString())
                .execute();
    }
}
