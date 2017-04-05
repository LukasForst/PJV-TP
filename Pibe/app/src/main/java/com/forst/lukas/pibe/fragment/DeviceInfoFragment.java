package com.forst.lukas.pibe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forst.lukas.pibe.R;
/**
 * {@link Fragment} with main device info.
 *
 * @author Lukas Forst
 */
public class DeviceInfoFragment extends Fragment {
    private View mView;
    private Bundle args;

    public DeviceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.args = args;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_device_info, container, false);

        if(args.containsKey("ip_address")) {
            String c = args.getString("ip_address");

            TextView tx = (TextView) mView.findViewById(R.id.fragment_device_info_textView);
            tx.setText(c);
        }

        return mView;
    }

}
