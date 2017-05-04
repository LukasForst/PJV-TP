package com.forst.lukas.pibe.data;

/**
 * Singleton class which provides container for {@link com.forst.lukas.pibe.data.PibeConfiguration}.
 *
 * @author Lukas Forst
 * @see com.forst.lukas.pibe.data.PibeConfiguration
 */

public class AppConfig {
    private static PibeConfiguration instance;

    private AppConfig() {
    }

    public static PibeConfiguration getInstance() {
        if (instance == null)
            instance = new PibeConfiguration();
        return instance;
    }
}
