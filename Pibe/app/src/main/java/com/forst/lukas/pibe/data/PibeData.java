package com.forst.lukas.pibe.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Data holding class with static methods.
 * @author Lukas Forst
 */

public class PibeData {
    public final static String NOTIFICATION_EVENT
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_EVENT";
    public final static String NOTIFICATION_REQUEST
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_REQUEST";
    private static final String TAG = "PibeData";
    public static int COUNTER = 0;
    private static List<String> filteredApps = new ArrayList<>();
    private static List<String> installedAppsNames = new ArrayList<>();
    private static HashMap<String, Integer> lastUsedIPsAndPorts = new HashMap<>();
    private static String ipAddress = "";
    private static String deviceIPAddress = "";

    private static int port = -1;

    private static boolean hasPermission = false;
    private static boolean isSendingEnabled = false;
    private static boolean isNotificationCatcherEnabled = false;
    private static boolean isConnectionReady = false;
    private static boolean isWifiConnected = false;
    private static boolean testNotificationArrived = false; //testing purpose only

    public static List<String> getFilteredApps() {
        return filteredApps;
    }

    public static void setFilteredApps(List<String> filteredApps) {
        PibeData.filteredApps = filteredApps;
    }

    public static List<String> getInstalledAppsNames() {
        return installedAppsNames;
    }

    public static void setInstalledAppsNames(List<String> installedAppsNames) {
        PibeData.installedAppsNames = installedAppsNames;
    }

    public static HashMap<String, Integer> getLastUsedIPsAndPorts() {
        return lastUsedIPsAndPorts;
    }

    public static void setLastUsedIPsAndPorts(HashMap<String, Integer> lastUsedIPsAndPorts) {
        PibeData.lastUsedIPsAndPorts = lastUsedIPsAndPorts;
    }

    public static String getIpAddress() {
        return ipAddress;
    }

    public static void setIPAndPort(String ipAddress, int port) {
        PibeData.ipAddress = ipAddress;
        PibeData.port = port;
        if (!ipAddress.equals("") && port != -1) {
            lastUsedIPsAndPorts.put(ipAddress, port);
        }
    }

    public static int getPort() {
        return port;
    }

    public static String getDeviceIPAddress() {
        return deviceIPAddress;
    }

    public static void setDeviceIPAddress(String deviceIPAddress) {

        PibeData.deviceIPAddress = deviceIPAddress;
    }

    public static boolean hasPermission() {
        return hasPermission;
    }

    public static void setPermission(boolean hasPermission) {
        PibeData.hasPermission = hasPermission;
    }

    public static boolean isSendingEnabled() {
        return isSendingEnabled;
    }

    public static void setSendingEnabled(boolean isSendingEnabled) {
        PibeData.isSendingEnabled = isSendingEnabled;
    }

    public static boolean isNotificationCatcherEnabled() {
        return isNotificationCatcherEnabled;
    }

    public static void setNotificationCatcherEnabled(boolean isNotificationCatcherEnabled) {
        PibeData.isNotificationCatcherEnabled = isNotificationCatcherEnabled;
    }

    public static boolean isConnectionReady() {
        return isConnectionReady;
    }

    public static void setConnectionReady(boolean isConnectionReady) {
        PibeData.isConnectionReady = isConnectionReady;
        if (!isConnectionReady)
            PibeData.isSendingEnabled = false;
    }

    public static boolean isWifiConnected() {
        return isWifiConnected;
    }

    public static void setWifiConnected(boolean isWifiConnected) {
        PibeData.isWifiConnected = isWifiConnected;
    }

    public static boolean hasTestNotificationArrived() {
        return testNotificationArrived;
    }

    public static void setTestNotificationArrived(boolean testNotificationArrived) {
        PibeData.testNotificationArrived = testNotificationArrived;
    }

    public static void resetData() {
        PibeData.isConnectionReady = false;
        PibeData.isSendingEnabled = false;
    }
}
