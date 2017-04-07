package com.forst.lukas.pibe.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.forst.lukas.pibe.R;

/**
 * {@link Fragment} with permission access.
 *  @author Lukas Forst
 */
public class PermissionFragment extends Fragment {


    public PermissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_permission, container, false);
        Button button = (Button) inflatedView.findViewById(R.id.fragment_permission_button);

        // Determine whether can be settings offend directly
        if (Build.VERSION.SDK_INT > 21) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
            });
        } else {
            // Show manual instead of button
            button.setVisibility(View.GONE);
            TextView tx = (TextView) inflatedView.findViewById(R.id.fragment_permission_low_sdk_text);
            tx.setVisibility(View.VISIBLE);
        }
        return inflatedView;
    }

}
