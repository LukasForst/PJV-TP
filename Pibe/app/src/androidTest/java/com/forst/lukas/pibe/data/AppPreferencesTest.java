package com.forst.lukas.pibe.data;

import android.support.test.rule.ActivityTestRule;

import com.forst.lukas.pibe.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 5/12/17.
 *
 * @author Lukas Forst
 */
public class AppPreferencesTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loadPreferences() throws Exception {
        PibeConfiguration createdConfig = new PibeConfiguration();

        createdConfig.setNotificationPermission(true);

        createdConfig.setReadContactsPermission(true);
        createdConfig.setReadingContactsEnabled(true);

        createdConfig.setReadPhoneStatePermission(true);
        createdConfig.setPhoneStateCatchingEnabled(true);

        createdConfig.COUNTER = 10;
        createdConfig.setIPAndPort("127.1.1.1", 1945);


        AppConfig.setTestInstance(createdConfig);

        AppPreferences appPreferences = new AppPreferences(mActivityRule.getActivity());
        appPreferences.savePreferences();

        PibeConfiguration loadedConfig = new PibeConfiguration();

        AppConfig.setTestInstance(loadedConfig);
        appPreferences.loadPreferences();

        loadedConfig.setReadPhoneStatePermission(true); //this is checked on the start of the app
        loadedConfig.setReadContactsPermission(true);

        assertEquals(true, loadedConfig.hasNotificationPermission());
        assertEquals(true, loadedConfig.isReadingContactsEnabled());
        assertEquals(true, loadedConfig.isPhoneStateCatchingEnabled());
        assertEquals(10, loadedConfig.COUNTER);
        assertEquals("127.1.1.1", loadedConfig.getIpAddress());
        assertEquals(1945, loadedConfig.getPort());
    }

}