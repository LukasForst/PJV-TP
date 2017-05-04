package com.forst.lukas.pibe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.data.AppConfig;
import com.forst.lukas.pibe.data.PibeConfiguration;
import com.forst.lukas.pibe.tasks.Permissions;

/**
 * {@link Fragment} with main screen displayed after start of the application.
 * @author Lukas Forst
 */
public class HomeFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private CheckBox readPhoneState;
    private CheckBox readContacts;
    private CheckBox readNotifications;

    private PibeConfiguration pb;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pb = AppConfig.getInstance();

        View inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        Log.i(TAG, "onCreate");
        //check permissions
        Permissions p = new Permissions(getActivity());
        p.checkAllPermissions();

        readPhoneState = (CheckBox)
                inflatedView.findViewById(R.id.fragment_home_readPhoneStateCheckBox);
        readPhoneState.setChecked(pb.isPhoneStateCatchingEnabled());

        readContacts = (CheckBox)
                inflatedView.findViewById(R.id.fragment_home_readContactsCheckBox);
        readContacts.setChecked(pb.isReadingContactsEnabled());

        readNotifications = (CheckBox)
                inflatedView.findViewById(R.id.fragment_home_readNotificationsCheckBox);
        readNotifications.setChecked(pb.hasNotificationPermission());

        setListeners();

        return inflatedView;
    }

    /**
     * Attach listeners to the buttons.
     */
    private void setListeners() {
        final Permissions p = new Permissions(getActivity());
        readPhoneState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p.checkReadPhoneStatePermissions()) {
                    p.askForReadPhoneStatePermission();
                    readPhoneState.setChecked(false);
                } else {
                    pb.setPhoneStateCatchingEnabled(readPhoneState.isChecked());
                }
            }
        });

        readContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p.checkContactReadPermission()) {
                    p.askForContactReadPermission();
                    readContacts.setChecked(false);
                } else {
                    pb.setReadingContactsEnabled(readContacts.isChecked());
                }
            }
        });

        readNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setNotificationCatcherEnabled(readNotifications.isChecked());
                if (!readNotifications.isChecked()) {
                    Toast.makeText(v.getContext().getApplicationContext(),
                            "Application is now not working!", Toast.LENGTH_LONG).show();
                    pb.setNotificationCatcherEnabled(false);
                    readContacts.setEnabled(false);
                    readPhoneState.setEnabled(false);
                } else {
                    Toast.makeText(v.getContext().getApplicationContext(),
                            "Enjoy", Toast.LENGTH_LONG).show();
                    readContacts.setEnabled(true);
                    readPhoneState.setEnabled(true);
                    pb.setNotificationCatcherEnabled(false);
                }
            }
        });

    }

    /**
     * Set checkbox checked from main activity.
     */
    public void setReadPhoneStateChecked(boolean checked) {
        if (readContacts == null) return;
        readPhoneState.setChecked(checked);
    }

    /**
     * Set checkbox checked from main activity.
     */
    public void setReadContactsChecked(boolean checked) {
        if (readContacts == null) return;
        readContacts.setChecked(checked);
    }
}
