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
 * <b>Not fully implemented yet!</b>
 * <br>
 *  Class provides methods for getting phone stats like Wifi signal strength, battery percentage etc.
 * @author Lukas Forst
 */

public class DeviceInfo {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;

    public DeviceInfo(Context context) {
        this.context = context;
    }

    /**
     * @return percentage of Wifi Signal.
     * */
    public int getWifiSignalStrength() {
        WifiManager wifiManager = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 100;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        Log.i(TAG, "Wifi " + String.valueOf(level) + "%");
        return level;
    }

    /**
     * @return percentage of remaining battery
     */
    public int getBatteryPercentage() {
        BatteryManager bm = (BatteryManager)
                context.getApplicationContext().getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.i(TAG, "Battery " + String.valueOf(batLevel) + "%");
        return batLevel;
    }

    /**
     * @return JSON with packed data in <i>wifi</i> and <i>battery</i>
     */
    public JSONObject packData(JSONObject jsonObject) {
        try {
            jsonObject.put("wifi", String.valueOf(getWifiSignalStrength()));
            jsonObject.put("battery", String.valueOf(getBatteryPercentage()));
            return jsonObject;
        } catch (JSONException e) {
            return jsonObject;
        }
    }
}
