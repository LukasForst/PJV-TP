package com.forst.lukas.pibe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.data.AppConfig;
import com.forst.lukas.pibe.data.AppPreferences;
import com.forst.lukas.pibe.data.PibeConfiguration;
import com.forst.lukas.pibe.fragment.AppFilterFragment;
import com.forst.lukas.pibe.fragment.DeviceInfoFragment;
import com.forst.lukas.pibe.fragment.HomeFragment;
import com.forst.lukas.pibe.fragment.LogFragment;
import com.forst.lukas.pibe.fragment.PermissionFragment;
import com.forst.lukas.pibe.fragment.SettingsFragment;
import com.forst.lukas.pibe.tasks.InstalledApplications;
import com.forst.lukas.pibe.tasks.Permissions;

/**
 * Main activity of Pibe application.
 * @author Lukas Forst
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    public static final int PERMISSION_REQUEST_READ_CONTACTS = 2;
    public static final int PERMISSION_REQUEST_CAMERA = 3;

    private final String TAG = this.getClass().getSimpleName();

    private HomeFragment homeFragment;
    private AppFilterFragment appFilterFragment;
    private SettingsFragment settingsFragment;
    private DeviceInfoFragment deviceInfoFragment;
    private LogFragment logFragment;
    private PermissionFragment permissionFragment;
    private Fragment currentFragment;

    private LogFragment.NotificationReceiver notificationReceiver;

    private PibeConfiguration pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = AppConfig.getInstance();

        new AppPreferences(this).loadPreferences();

        // Add the fragment to the 'fragment_container' FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) return;
            initializeFragments();

            Fragment firstDisplayed = pb.hasNotificationPermission()
                    ? homeFragment : permissionFragment;
            FragmentTransaction ft =
                    getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.fragment_container, firstDisplayed);
            ft.commit();
            currentFragment = firstDisplayed;
        }

        prepareGUI();

        //Last thing to do is turn whole circus on :-)
        pb.setNotificationCatcherEnabled(true);

        //check permissions
        Permissions p = new Permissions(this);

        //p.checkNotificationPermission();
        p.checkAllPermissions();
        //get list of all installed applications
        new Thread(new InstalledApplications(getPackageManager())).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment =
                pb.hasNotificationPermission() ? homeFragment : permissionFragment;
        if (currentFragment != fragment && currentFragment != settingsFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            currentFragment = fragment;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        new AppPreferences(this).savePreferences();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(notificationReceiver);
        } catch (IllegalArgumentException ignored) {
        }
        new AppPreferences(this).savePreferences();
        pb.setConnectionReady(false);
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment =
                    pb.hasNotificationPermission() ? homeFragment : permissionFragment;
            if (currentFragment != fragment) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
                currentFragment = fragment;
            } else {
                super.onBackPressed();
            }
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
        bundle = getIntent().getExtras() != null
                ? new Bundle(getIntent().getExtras()) : new Bundle();


        if (fragment != null && fragment != currentFragment) {
            fragment.setArguments(bundle);

            //add animation because of lots of data in some fragments
            FragmentTransaction ft =
                    getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            currentFragment = fragment;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_PHONE_STATE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pb.setReadPhoneStatePermission(true);
                    pb.setPhoneStateCatchingEnabled(true);
                    homeFragment.setReadPhoneStateChecked(true);
                } else {
                    Log.i(TAG, "ReadPhoneState permission denied!");
                    pb.setReadPhoneStatePermission(false);
                    homeFragment.setReadPhoneStateChecked(false);
                }
                break;
            case PERMISSION_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    pb.setReadContactsPermission(true);
                    pb.setPhoneStateCatchingEnabled(true);
                    homeFragment.setReadContactsChecked(true);
                } else {
                    pb.setReadContactsPermission(false);
                    homeFragment.setReadContactsChecked(false);
                }
                break;
            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    settingsFragment.launchQR();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Prepare interface for using application.
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
        registerReceiver(notificationReceiver);

        setupSwitch();
    }

    /**
     * Prepares {@link Switch} in the main gui.<br>Layout for the switch is located
     * in the <i>app_bar_main.xml</i>
     */
    private void setupSwitch() {
        final Switch notifySwitch = (Switch) findViewById(R.id.notification_sending_switch);
        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message;
                //check wifi connection
                ConnectivityManager connManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getActiveNetworkInfo();

                if (!pb.hasNotificationPermission()) {
                    notifySwitch.setChecked(false);
                    message = "Permission is not granted yet!";

                } else if (!mWifi.isConnected()) {
                    notifySwitch.setChecked(false);
                    message = "Turn your WiFi on!";
                } else if (!pb.isConnectionReady()) {
                    notifySwitch.setChecked(false);
                    message = "You must set server IP!";

                } else {
                    pb.setSendingEnabled(isChecked);
                    message = "Sending notifications to the computer is now turned ";
                    message += isChecked ? "on" : "off";
                }
                Snackbar.make(buttonView, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        pb.aSwitch = notifySwitch;
    }

    /**
     * Register receiver for given {@link BroadcastReceiver}
     * and PibeConfiguration.NOTIFICATION_EVENT code.
     *
     * @see PibeConfiguration
     * @see BroadcastReceiver
     */
    private void registerReceiver(BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PibeConfiguration.NOTIFICATION_EVENT);
        registerReceiver(receiver, filter);
    }

    /**
     * Prepares fragments for usage. Initialization.
     * */
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
        switch (id) {
            case R.id.nav_home:  // main page
                fragment = pb.hasNotificationPermission() ? homeFragment : permissionFragment;
                break;
            case R.id.nav_app_filter:  //app filter
                fragment = appFilterFragment;
                break;
            case R.id.nav_settings:  // settings
                fragment = settingsFragment;
                break;
            case R.id.nav_info:  //device info
                fragment = deviceInfoFragment;
                break;
            case R.id.nav_log:  //notification log
                fragment = logFragment;
                break;
            case R.id.nav_share:  //share button
                shareLink();
                break;
            case R.id.nav_review:  //review on the play store
                googlePlayReview();
                break;
            case R.id.nav_contact_developer:  // contact me
                contactMe();
                break;
            case R.id.nav_payment:  // buy me a beer!
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://paypal.me/LukasForst/35")));
                break;
        }
        return fragment;
    }

    /**
     * New activity, that can send email to the author.
     */
    private void contactMe() {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("message/rfc822");
        it.putExtra(Intent.EXTRA_EMAIL, new String[]{"lukasforst@gmail.com"});
        it.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
        try {
            startActivity(Intent.createChooser(it, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email clients installed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * New activity, that shows Google Play on the page with this application.
     */
    private void googlePlayReview() {
        final String appPackageName = getPackageName();
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
     */
    private void shareLink() {
        String message = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Share link to my app!"));
    }
}