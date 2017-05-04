package com.forst.lukas.pibe.data;


import android.Manifest;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Whole configuration of application is stored here. Class is full of getters and setters.
 * <br>If is WiFi enabled, sending enabled etc. and whether has application access to the:
 * <ul>
 *     <li>{@link Manifest.permission#READ_PHONE_STATE}</li>
 *     <li>{@link Manifest.permission#READ_CONTACTS}</li>
 *     <li>{@link Manifest.permission#CAMERA}</li>
 *     <li>{@link Manifest.permission#BIND_NOTIFICATION_LISTENER_SERVICE}</li>
 * </ul><br>
 * Data is stored here as well:
 * <ul>
 *     <li>{@link #ipAddress} - String of IP Address of the server</li>
 *     <li>{@link #port} - Port of the server</li>
 *     <li>{@link #installedAppsNames} - list of the installed applications names</li>
 *     <li>{@link #filteredApps} - applications which are filtered</li>
 *     <li>{@link #lastUsedIPsAndPorts} - {@link HashMap} with history of used IPs and ports</li>
 * </ul>
 *
 * @author Lukas Forst
 */

public class PibeConfiguration {
    public static final String NOTIFICATION_EVENT
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_EVENT";
    public static final String NOTIFICATION_REQUEST
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_REQUEST";
    private static final String TAG = "PibeConfiguration";

    public int COUNTER = 0;
    private List<String> filteredApps = new ArrayList<>();
    private List<String> installedAppsNames = new ArrayList<>();
    private HashMap<String, Integer> lastUsedIPsAndPorts = new HashMap<>();
    private String ipAddress = "";
    private String deviceIPAddress = "";
    private int port = -1;
    private boolean hasNotificationPermission = false;
    private boolean readPhoneStatePermission = false;
    private boolean readContactsPermission = false;
    private boolean isSendingEnabled = false;
    private boolean isNotificationCatcherEnabled = false;
    private boolean isConnectionReady = false;
    private boolean isReadingContactsEnabled = false;
    private boolean isPhoneStateCatchingEnabled = false;
    private boolean isWifiConnected = false;
    private boolean testNotificationArrived = false; //testing purpose only

    public PibeConfiguration() {
    }

    public List<String> getFilteredApps() {
        return filteredApps;
    }

    public void setFilteredApps(List<String> filteredApps) {
        this.filteredApps = filteredApps;
    }

    public List<String> getInstalledAppsNames() {
        return installedAppsNames;
    }

    public void setInstalledAppsNames(List<String> installedAppsNames) {
        this.installedAppsNames = installedAppsNames;
    }

    public HashMap<String, Integer> getLastUsedIPsAndPorts() {
        return lastUsedIPsAndPorts;
    }

    public void setLastUsedIPsAndPorts(HashMap<String, Integer> lastUsedIPsAndPorts) {
        this.lastUsedIPsAndPorts = lastUsedIPsAndPorts;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIPAndPort(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        if (!ipAddress.equals("") && port != -1) {
            lastUsedIPsAndPorts.put(ipAddress, port);
        }
    }

    public int getPort() {
        return port;
    }

    public String getDeviceIPAddress() {
        return deviceIPAddress;
    }

    public void setDeviceIPAddress(String deviceIPAddress) {

        this.deviceIPAddress = deviceIPAddress;
    }

    public boolean hasNotificationPermission() {
        return hasNotificationPermission;
    }

    public void setNotificationPermission(boolean hasPermission) {
        this.hasNotificationPermission = hasPermission;
        Log.d(TAG, "Notification permission - " + hasPermission);
    }

    public boolean hasReadContactsPermission() {
        return readContactsPermission;
    }

    public void setReadContactsPermission(boolean readContactsPermission) {
        this.readContactsPermission = readContactsPermission;
        if (!readContactsPermission) this.setReadingContactsEnabled(false);
        Log.d(TAG, "ReadContacts permission - " + readContactsPermission);
    }

    public boolean hasReadPhoneStatePermission() {
        return readPhoneStatePermission;
    }

    public void setReadPhoneStatePermission(boolean readPhoneStatePermission) {
        this.readPhoneStatePermission = readPhoneStatePermission;
        if (!readPhoneStatePermission) this.setReadingContactsEnabled(false);
        Log.d(TAG, "ReadPhoneState permission - " + readPhoneStatePermission);

    }

    public boolean isSendingEnabled() {
        return isSendingEnabled;
    }

    public void setSendingEnabled(boolean isSendingEnabled) {
        this.isSendingEnabled = isSendingEnabled;
        Log.d(TAG, "Sending enabled - " + isSendingEnabled);
    }

    public boolean isNotificationCatcherEnabled() {
        return isNotificationCatcherEnabled;
    }

    public void setNotificationCatcherEnabled(boolean isNotificationCatcherEnabled) {
        this.isNotificationCatcherEnabled = isNotificationCatcherEnabled;
    }

    public boolean isReadingContactsEnabled() {
        return this.hasReadContactsPermission() && isReadingContactsEnabled;
    }

    public void setReadingContactsEnabled(boolean isReadingContactsEnabled) {
        this.isReadingContactsEnabled = isReadingContactsEnabled;
        Log.d(TAG, "Reading contacts - " + isReadingContactsEnabled);
    }

    public boolean isPhoneStateCatchingEnabled() {
        return this.hasReadPhoneStatePermission() && isPhoneStateCatchingEnabled;
    }

    public void setPhoneStateCatchingEnabled(boolean isPhoneStateCatchingEnabled) {
        this.isPhoneStateCatchingEnabled = isPhoneStateCatchingEnabled;
        Log.d(TAG, "Incoming call detection - " + isPhoneStateCatchingEnabled);
    }

    public boolean isConnectionReady() {
        return isConnectionReady;
    }

    public void setConnectionReady(boolean isConnectionReady) {
        this.isConnectionReady = isConnectionReady;
        if (!isConnectionReady)
            this.isSendingEnabled = false;
    }

    public boolean isWifiConnected() {
        return isWifiConnected;
    }

    public void setWifiConnected(boolean isWifiConnected) {
        this.isWifiConnected = isWifiConnected;
    }

    public boolean hasTestNotificationArrived() {
        return testNotificationArrived;
    }

    public void setTestNotificationArrived(boolean testNotificationArrived) {
        this.testNotificationArrived = testNotificationArrived;
    }

    /**
     * {@link #isConnectionReady} = false
     * <br>{@link #isSendingEnabled} = false
     */
    public void resetData() {
        this.isConnectionReady = false;
        this.isSendingEnabled = false;
    }
}
