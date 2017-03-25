package com.forst.lukas.pibe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.forst.lukas.pibe.R;
import com.forst.lukas.pibe.fragment.AppFilterFragment;
import com.forst.lukas.pibe.fragment.DeviceInfoFragment;
import com.forst.lukas.pibe.fragment.HomeFragment;
import com.forst.lukas.pibe.fragment.LogFragment;
import com.forst.lukas.pibe.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String NOTIFICATION_RECEIVED
            = "com.forst.lukas.pibe.tasks.NOTIFICATION_RECEIVED";


    private static MainActivity mainActivity;

    private HomeFragment homeFragment;
    private AppFilterFragment appFilterFragment;
    private SettingsFragment settingsFragment;
    private DeviceInfoFragment deviceInfoFragment;
    private LogFragment logFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            //Initialize fragments
            initializeFragments();
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment).commit();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LogFragment.NotificationReceiver notificationReceiver = logFragment.getNotificationReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_RECEIVED);
        registerReceiver(notificationReceiver, filter);
    }

    private void initializeFragments(){
        homeFragment = new HomeFragment();
        appFilterFragment = new AppFilterFragment();
        settingsFragment = new SettingsFragment();
        deviceInfoFragment = new DeviceInfoFragment();
        logFragment = new LogFragment();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId(); //clicked item id
        if (findViewById(R.id.fragment_container) == null) { //in case that something wrong happened
            return false;
        }

        Fragment fragment = null;
        Bundle bundle; //bundle of data given to the fragment
        if(getIntent().getExtras() != null){
            bundle = new Bundle(getIntent().getExtras());
        } else {
            bundle = new Bundle();
        }

        if(id == R.id.nav_home) { // main page
            fragment = homeFragment;

        } else if(id == R.id.nav_app_filter){ //app filter
            fragment = appFilterFragment;

        } else if (id == R.id.nav_settings) { // settings
            fragment = settingsFragment;

        } else if(id == R.id.nav_info){ //device info
            fragment = deviceInfoFragment;
            bundle.putString("ip_address", getDeviceIPAddress());

        } else if(id == R.id.nav_log){ //notification log
            fragment = logFragment;

        } else if (id == R.id.nav_share) { //share button
            shareLink();

        } else if(id == R.id.nav_review){ //review on the play store
            googlePlayReview();

        } else if (id == R.id.nav_contact_developer) { // contact me
            contactMe();

        } else if(id == R.id.nav_payment){ // buy me a beer!
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://paypal.me/LukasForst/35")));
        }

        if(fragment != null){
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

    private void googlePlayReview(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException acnf) { //google play not installed
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }
    }

    private void shareLink(){
        String message = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Share link to my app!"));
    }

    private String getDeviceIPAddress(){
        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    public static Context getContext() {
        return mainActivity.getApplicationContext();
    }

}
