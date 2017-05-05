package com.forst.lukas.pibe.data;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * 5/4/17.
 *
 * @author Lukas Forst
 */
public class PibeConfigurationTest {
    @Test
    public void setIPAndPort() throws Exception {
        String ip = "127.0.0.1";
        int port = 1032;
        PibeConfiguration pb = new PibeConfiguration();
        pb.setIPAndPort(ip, port);

        HashMap hashMap = pb.getLastUsedIPsAndPorts();
        assertEquals(hashMap.containsKey(ip), true);
        assertEquals(hashMap.get(ip), port);

        pb.setIPAndPort("", 0);
        assertEquals(hashMap.containsKey(""), false);
    }

    @Test
    public void setReadContactsPermission() throws Exception {
        PibeConfiguration pb = new PibeConfiguration();
        pb.setReadContactsPermission(false);
        assertEquals(pb.isReadingContactsEnabled(), false);
    }

}