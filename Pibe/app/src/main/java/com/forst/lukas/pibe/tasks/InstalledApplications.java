package com.forst.lukas.pibe.tasks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.forst.lukas.pibe.data.AppConfig;
import com.forst.lukas.pibe.data.PibeConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which will search for installed applications and save them in the {@link PibeConfiguration#installedAppsNames}
 * @author Lukas Forst
 */

public class InstalledApplications implements Runnable {
    private PackageManager pm;

    private PibeConfiguration pb;

    public InstalledApplications(PackageManager packageManager) {
        pm = packageManager;
    }

    @Override
    public void run() {
        pb = AppConfig.getInstance();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<String> appNames = new ArrayList<>();

        for (ApplicationInfo app : apps) {
            appNames.add(pm.getApplicationLabel(app).toString());
        }
        pb.setInstalledAppsNames(appNames);
    }
}
