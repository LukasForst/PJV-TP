package com.forst.lukas.pibe.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.forst.lukas.pibe.data.PibeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which will search for installed applications.
 * @author Lukas Forst
 */

public class InstalledApplications implements Runnable {
    private PackageManager pm;

    public InstalledApplications(PackageManager packageManager) {
        pm = packageManager;
    }

    @Override
    public void run() {
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<String> appNames = new ArrayList<>();

        for (ApplicationInfo app : apps) {
            appNames.add(pm.getApplicationLabel(app).toString());
        }
        PibeData.setInstalledAppsNames(appNames);
    }
}
