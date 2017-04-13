package com.forst.lukas.pibe.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 13/04/17.
 */

public class DataHolder {
    public final static String NOTIFICATION_EVENT = "com.forst.lukas.pibe.tasks.NOTIFICATION_EVENT";
    public static boolean PERMISSION_GRANTED = false;

    private static List<String> filteredApps = new ArrayList<>();

    private static String ipAddress = "";
    private static String port = "";
    private static String deviceIPAddress = "";

    private static boolean hasPermission = false;
    private static boolean isSendingEnabled = false;
    private static boolean isCatchingEnabled = false;

}
