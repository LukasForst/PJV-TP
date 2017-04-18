package com.forst.lukas.pibe.tasks;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by lukas on 18/04/17.
 */

public class DeviceInfo {
    private final String TAG = this.getClass().getSimpleName();

    /**
     * Excellent >-50 dBm
     * <p>
     * Good -50 to -60 dBm
     * <p>
     * Fair -60 to -70 dBm
     * <p>
     * Weak < -70 dBm
     */
    public int getWifiSignalStrength(Context context) {
        WifiManager wifiManager = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 100;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        Log.i(TAG, "Wifi " + String.valueOf(level) + "%");
        return level;
    }

    public int getBatteryPercentage(Context context) {
        BatteryManager bm = (BatteryManager)
                context.getApplicationContext().getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.i(TAG, "Battery " + String.valueOf(batLevel) + "%");
        return batLevel;
    }

    public JSONObject packData(Context context, JSONObject jsonObject) {
        try {
            jsonObject.put("wifi", String.valueOf(getWifiSignalStrength(context)));
            jsonObject.put("battery", String.valueOf(getBatteryPercentage(context)));
            return jsonObject;
        } catch (JSONException e) {
            return jsonObject;
        }
    }
}
