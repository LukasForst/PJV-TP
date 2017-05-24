package com.forst.lukas.pibe.tasks;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 5/24/17.
 *
 * @author Lukas Forst
 */
public class TestConnectionTest {
    @Test
    public void testPort() throws Exception {
        TestConnection tc = new TestConnection(null, "127.0.0.1", "3843");

        assertEquals(tc.testPort("3843"), true);
        assertEquals(tc.testPort("0"), false);
        assertEquals(tc.testPort("sdkfjh"), false);
        assertEquals(tc.testPort("1040"), true);
        assertEquals(tc.testPort("1000000"), false);
    }

    @Test
    public void testIP() throws Exception {
        TestConnection tc = new TestConnection(null, "127.0.0.1", "3843");

        assertEquals(tc.testIP("127.0.0.1"), true);
        assertEquals(tc.testIP("127.122.123.127"), true);
        assertEquals(tc.testIP("127.0.0.290"), false);
        assertEquals(tc.testIP("asdfasdfasdf"), false);
    }

}