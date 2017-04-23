package com.forst.lukas.pibe.fragment;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.activity.MainActivity;
import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.tasks.Permissions;

/**
 * {@link Fragment} with main screen displayed after onCreate()
 * @author Lukas Forst
 */
public class HomeFragment extends Fragment {
    public static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    public static final int PERMISSION_REQUEST_READ_CONTACTS = 2;
    private final String TAG = this.getClass().getSimpleName();
    private CheckBox readPhoneState;
    private CheckBox readContacts;
    private CheckBox readNotifications;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        readPhoneState = (CheckBox)
                inflatedView.findViewById(R.id.fragment_home_readPhoneStateCheckBox);
        readPhoneState.setChecked(PibeData.isPhoneStateCatchingEnabled());

        readContacts = (CheckBox)
                inflatedView.findViewById(R.id.fragment_home_readContactsCheckBox);
        readContacts.setChecked(PibeData.isReadingContactsEnabled());

        readNotifications = (CheckBox)
                inflatedView.findViewById(R.id.fragment_home_readNotificationsCheckBox);
        readNotifications.setChecked(PibeData.hasNotificationPermission());

        setListeners();

        return inflatedView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "PERMISSION");
        switch (requestCode) {
            case MainActivity.PERMISSION_REQUEST_READ_PHONE_STATE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PibeData.setReadPhoneStatePermission(true);
                    readPhoneState.setChecked(true);
                } else {
                    Log.i(TAG, "ReadPhoneState permission denied!");
                    PibeData.setReadPhoneStatePermission(false);
                    readPhoneState.setChecked(false);
                }
                break;
            case MainActivity.PERMISSION_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    PibeData.setReadContactsPermission(true);
                    readContacts.setChecked(true);
                } else {
                    PibeData.setReadContactsPermission(false);
                    readContacts.setChecked(false);
                }
                break;
            default:
                break;
        }

    }

    private void setListeners() {
        readPhoneState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PibeData.hasReadPhoneStatePermission()) {
                    boolean tmpState = new Permissions()
                            .checkReadPhoneStatePermissions(getActivity());
                    readPhoneState.setChecked(tmpState);
                } else {
                    PibeData.setIsPhoneStateCatchingEnabled(readPhoneState.isChecked());
                }
            }
        });

        readContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PibeData.hasReadContactsPermission()) {
                    boolean tmpState = new Permissions().checkContactReadPermission(getActivity());
                    readContacts.setChecked(tmpState);
                } else {
                    PibeData.setIsReadingContactsEnabled(readContacts.isChecked());
                }
            }
        });

        readNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PibeData.setNotificationCatcherEnabled(readNotifications.isChecked());
                if (!readNotifications.isChecked()) {
                    Toast.makeText(v.getContext().getApplicationContext(),
                            "Application is now not working!", Toast.LENGTH_LONG).show();
                    readContacts.setEnabled(false);
                    readPhoneState.setEnabled(false);
                } else {
                    readContacts.setEnabled(true);
                    readPhoneState.setEnabled(true);
                }
            }
        });

    }

    public void setReadPhoneStateChecked(boolean checked) {
        if (readContacts == null) return;
        readPhoneState.setChecked(checked);
    }

    public void setReadContactsChecked(boolean checked) {
        if (readContacts == null) return;
        readContacts.setChecked(checked);
    }
}
