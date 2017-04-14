package com.forst.lukas.pibe.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.forst.lukas.pibe.data.PibeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 14/04/17.
 */

public class GetInstalledApplications implements Runnable {
    private PackageManager pm;

    public GetInstalledApplications(PackageManager packageManager) {
        pm = packageManager;
    }

    @Override
    public void run() {
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<String> appNames = new ArrayList<>();

        for (ApplicationInfo app : apps) {
            appNames.add(pm.getApplicationLabel(app).toString());
            PibeData.setInstalledAppsNames(appNames);
        }
    }
}
