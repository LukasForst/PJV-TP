package com.forst.lukas.pibe.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class which selectively saves application preferences to the SharedPreferences
 *
 * @author Lukas Forst
 */

public class AppPreferences {
    private final String TAG = this.getClass().getSimpleName();

    private SharedPreferences shp;

    public AppPreferences(Activity activity) {
        shp = activity.getPreferences(Context.MODE_PRIVATE);
    }
    public void loadPreferences() {
        Gson gson = new Gson();

        //filtered apps settings
        String tmpShpData = shp.getString("filteredApps", "");
        if (!tmpShpData.equals("")) {
            List<String> tmpFilteredApps;
            tmpFilteredApps = gson.fromJson(tmpShpData,
                    new TypeToken<ArrayList<String>>() {
                    }.getType());
            PibeData.setFilteredApps(tmpFilteredApps);
        }

        //last used
        tmpShpData = shp.getString("lastUsedIPsPort", "");
        if (!tmpShpData.equals("")) {
            HashMap<String, Integer> tmpLastUsed;
            tmpLastUsed = gson.fromJson(tmpShpData,
                    new TypeToken<HashMap<String, Integer>>() {
                    }.getType());
            PibeData.setLastUsedIPsAndPorts(tmpLastUsed);
        }

        //IP and port
        PibeData.setIPAndPort(shp.getString("ipAddress", ""), shp.getInt("port", -1));

        //Permission
        PibeData.setNotificationPermission(shp.getBoolean("notificationPermission", false));

        //Checkboxes enabled
        PibeData.setIsReadingContactsEnabled(shp.getBoolean("contactsReadingEnabled", false));
        PibeData.setIsPhoneStateCatchingEnabled(shp.getBoolean("phoneStateReadingEnabled", false));

        //counter
        PibeData.COUNTER = shp.getInt("counter", 0);

        Log.i(TAG, "loaded");
    }

    public void savePreferences() {
        SharedPreferences.Editor editor = shp.edit();
        Gson gson = new Gson();

        editor.putBoolean(
                "notificationPermission", PibeData.hasNotificationPermission());
        editor.putBoolean("phoneStateReadingEnabled", PibeData.isPhoneStateCatchingEnabled());
        editor.putBoolean("contactsReadingEnabled", PibeData.isReadingContactsEnabled());

        editor.putString("ipAddress", PibeData.getIpAddress());
        editor.putInt("port", PibeData.getPort());
        editor.putInt("counter", PibeData.COUNTER);

        editor.putString("filteredApps", gson.toJson(PibeData.getFilteredApps()));
        editor.putString("lastUsedIPsPort", gson.toJson(PibeData.getLastUsedIPsAndPorts()));

        editor.apply();
        Log.i(TAG, "saved");
    }

}
