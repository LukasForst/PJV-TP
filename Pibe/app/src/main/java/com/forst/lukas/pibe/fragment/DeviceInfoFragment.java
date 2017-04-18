package com.forst.lukas.pibe.fragment;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.tasks.DeviceInfo;

/**
 * {@link Fragment} with main device info.
 *
 * @author Lukas Forst
 */
public class DeviceInfoFragment extends Fragment {

    public DeviceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_device_info, container, false);

        TextView tx = (TextView) inflatedView.findViewById(R.id.fragment_device_info_textView);
        tx.setText(getDeviceIPAddress(inflatedView));

        DeviceInfo d = new DeviceInfo();
        d.getBatteryPercentage(inflatedView.getContext());
        d.getWifiSignalStrength(inflatedView.getContext());

        return inflatedView;
    }

    /**
     * @return Device IPV4 IP address
     */
    private String getDeviceIPAddress(View v) {
        Context context = v.getContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        PibeData.setDeviceIPAddress(ip);
        return ip;
    }


}
