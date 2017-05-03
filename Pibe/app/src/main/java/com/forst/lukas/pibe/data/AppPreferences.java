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

    private PibeData pb;
    private SharedPreferences shp;

    public AppPreferences(Activity activity) {
        shp = activity.getPreferences(Context.MODE_PRIVATE);
        pb = PibeData.getInstance();
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
            pb.setFilteredApps(tmpFilteredApps);
        }

        //last used
        tmpShpData = shp.getString("lastUsedIPsPort", "");
        if (!tmpShpData.equals("")) {
            HashMap<String, Integer> tmpLastUsed;
            tmpLastUsed = gson.fromJson(tmpShpData,
                    new TypeToken<HashMap<String, Integer>>() {
                    }.getType());
            pb.setLastUsedIPsAndPorts(tmpLastUsed);
        }

        //IP and port
        pb.setIPAndPort(shp.getString("ipAddress", ""), shp.getInt("port", -1));

        //Permission
        pb.setNotificationPermission(shp.getBoolean("notificationPermission", false));

        //Checkboxes enabled
        pb.setReadingContactsEnabled(shp.getBoolean("contactsReadingEnabled", false));
        pb.setPhoneStateCatchingEnabled(shp.getBoolean("phoneStateReadingEnabled", false));

        //counter
        pb.COUNTER = shp.getInt("counter", 0);

        Log.i(TAG, "loaded");
    }

    public void savePreferences() {
        SharedPreferences.Editor editor = shp.edit();
        Gson gson = new Gson();

        editor.putBoolean(
                "notificationPermission", pb.hasNotificationPermission());
        editor.putBoolean("phoneStateReadingEnabled", pb.isPhoneStateCatchingEnabled());
        editor.putBoolean("contactsReadingEnabled", pb.isReadingContactsEnabled());

        editor.putString("ipAddress", pb.getIpAddress());
        editor.putInt("port", pb.getPort());
        editor.putInt("counter", pb.COUNTER);

        editor.putString("filteredApps", gson.toJson(pb.getFilteredApps()));
        editor.putString("lastUsedIPsPort", gson.toJson(pb.getLastUsedIPsAndPorts()));

        editor.apply();
        Log.i(TAG, "saved");
    }

}
