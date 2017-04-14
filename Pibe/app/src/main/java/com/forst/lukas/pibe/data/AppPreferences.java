package com.forst.lukas.pibe.data;

import android.content.SharedPreferences;

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
    private SharedPreferences shp;

    public AppPreferences(SharedPreferences shp) {
        this.shp = shp;
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
        PibeData.setPermission(shp.getBoolean("isPermissionGranted", false));
    }

    public void savePreferences() {
        SharedPreferences.Editor editor = shp.edit();
        Gson gson = new Gson();

        editor.putBoolean(
                "isPermissionGranted", PibeData.hasPermission());
        editor.putString("ipAddress", PibeData.getIpAddress());
        editor.putInt("port", PibeData.getPort());

        editor.putString("filteredApps", gson.toJson(PibeData.getFilteredApps()));
        editor.putString("lastUsedIPsPort", gson.toJson(PibeData.getLastUsedIPsAndPorts()));

        editor.apply();
    }

}
