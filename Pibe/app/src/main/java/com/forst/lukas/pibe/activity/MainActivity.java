package com.forst.lukas.pibe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.fragment.AppFilterFragment;
import com.forst.lukas.pibe.fragment.DeviceInfoFragment;
import com.forst.lukas.pibe.fragment.HomeFragment;
import com.forst.lukas.pibe.fragment.LogFragment;
import com.forst.lukas.pibe.fragment.PermissionFragment;
import com.forst.lukas.pibe.fragment.SettingsFragment;
import com.forst.lukas.pibe.tasks.NotificationCatcher;
import com.forst.lukas.pibe.tasks.ServerCommunication;

/**
 * @author Lukas Forst
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context applicationContext;

    private final String NOTIFICATION_RECEIVED
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_RECEIVED";

    private HomeFragment homeFragment;
    private AppFilterFragment appFilterFragment;
    private SettingsFragment settingsFragment;
    private DeviceInfoFragment deviceInfoFragment;
    private LogFragment logFragment;
    private PermissionFragment permissionFragment;
    private Fragment currentFragment;

    private LogFragment.NotificationReceiver notificationReceiver;

    // TODO: 28.3.17 temporary, delete
    private boolean permissionGranted = false;

    /**
     * Static way to get application context.
     */
    public static Context getContext() {
        return applicationContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applicationContext = getApplicationContext();

        //Check the permission fo notification reading
        // TODO: 28.3.17 deal with permissions

        // Add the fragment to the 'fragment_container' FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) return;
            initializeFragments();

            Fragment firstDisplayed = permissionGranted ? homeFragment : permissionFragment;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstDisplayed).commit();
            currentFragment = firstDisplayed;
        }
        prepareGUI();
        //Last thing to do is turn whole circus on :-)
        NotificationCatcher.setNotificationCatcherEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId(); //clicked item id

        // In case that something wrong happened
        if (findViewById(R.id.fragment_container) == null) {
            return false;
        }

        // Get selected fragment from id
        Fragment fragment = selectedFragment(id);

        // Bundle of data given to the fragment
        Bundle bundle;
        if (getIntent().getExtras() != null) {
            bundle = new Bundle(getIntent().getExtras());
        } else {
            bundle = new Bundle();
        }

        // Put some necessary bundle info for different fragments
        if (id == R.id.nav_info) {
            bundle.putString("ip_address", getDeviceIPAddress());
        }

        if (fragment != null && fragment != currentFragment) {
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
            currentFragment = fragment;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Prepare all necessary services and listeners.
     */
    private void prepareGUI() {
        //Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Register notification listener service
        notificationReceiver = logFragment.getNotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_RECEIVED);
        registerReceiver(notificationReceiver, filter);

        //Add switch listener
        final Switch notifySwitch = (Switch) findViewById(R.id.notification_sending_switch);
        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message;
                if (ServerCommunication.isReady()) {
                    ServerCommunication.setSendingEnabled(isChecked);
                    message = "Sending notifications to the computer is now turned ";
                    message += isChecked ? "on" : "off";
                } else {
                    notifySwitch.setChecked(false);
                    message = "You must set server IP!";
                }
                Snackbar.make(buttonView, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Initialize all fragments.
     */
    private void initializeFragments() {
        permissionFragment = new PermissionFragment();
        homeFragment = new HomeFragment();
        appFilterFragment = new AppFilterFragment();
        settingsFragment = new SettingsFragment();
        deviceInfoFragment = new DeviceInfoFragment();
        logFragment = new LogFragment();
    }

    /**
     * Identify selected fragment.
     */
    private Fragment selectedFragment(int id) {
        Fragment fragment = null;
        if (id == R.id.nav_home) { // main page
            fragment = permissionGranted ? homeFragment : permissionFragment;

        } else if (id == R.id.nav_app_filter) { //app filter
            fragment = appFilterFragment;

        } else if (id == R.id.nav_settings) { // settings
            fragment = settingsFragment;

        } else if (id == R.id.nav_info) { //device info
            fragment = deviceInfoFragment;

        } else if (id == R.id.nav_log) { //notification log
            fragment = logFragment;
        } else if (id == R.id.nav_share) { //share button
            shareLink();

        } else if (id == R.id.nav_review) { //review on the play store
            googlePlayReview();

        } else if (id == R.id.nav_contact_developer) { // contact me
            contactMe();

        } else if (id == R.id.nav_payment) { // buy me a beer!
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://paypal.me/LukasForst/35")));
        }
        return fragment;
    }

    /**
     * New activity, that can send email to the author.
     * */
    private void contactMe(){
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("message/rfc822");
        it.putExtra(Intent.EXTRA_EMAIL  , new String[]{"lukasforst@gmail.com"});
        it.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
        try {
            startActivity(Intent.createChooser(it, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * New activity, that shows Google Play on the page with this application.
     * */
    private void googlePlayReview(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("play://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException acnf) { //google play not installed
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }
    }

    /**
     * New activity, that provide option to send link (for the application) using android im.
     * */
    private void shareLink(){
        String message = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Share link to my app!"));
    }

    /**
     * @return Device IPV4 IP address
     * */
    private String getDeviceIPAddress(){
        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}