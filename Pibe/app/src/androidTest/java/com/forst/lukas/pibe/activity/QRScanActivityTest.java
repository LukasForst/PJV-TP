package com.forst.lukas.pibe.activity;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 5/24/17.
 *
 * @author Lukas Forst
 */
public class QRScanActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(QRScanActivity.class);

    @Test
    public void parseIP() throws Exception {
        QRScanActivity ac = (QRScanActivity) mActivityRule.getActivity();
        assertEquals(ac.parseIP("IP 123.0.0.1"), "123.0.0.1");
        assertEquals(ac.parseIP("IP 0.0.0.0"), "0.0.0.0");
        assertEquals(ac.parseIP("IPasdfa"), "");
    }

    @Test
    public void parsePort() throws Exception {
        QRScanActivity ac = (QRScanActivity) mActivityRule.getActivity();
        assertEquals(ac.parsePort("PORT 12333"), 12333);
        assertEquals(ac.parsePort("PORT 0000"), 0);
        assertEquals(ac.parsePort("PORT as;dfk"), -1);
    }

}