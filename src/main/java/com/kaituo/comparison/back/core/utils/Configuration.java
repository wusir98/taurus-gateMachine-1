package com.kaituo.comparison.back.core.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Configuration {
    private static Object lock = new Object();
    private static Configuration config = null;
    private static ResourceBundle rb = null;
    private static final String CONFIG_FILE = "validateConfig";

    private Configuration() {
        rb = ResourceBundle.getBundle(CONFIG_FILE);
    }

    public static Configuration getInstance() {
        synchronized (lock) {
            if (null == config) {
                config = new Configuration();
            }
        }
        return (config);
    }

    public String getValue(String key) {
        try {
            return (rb.getString(key));
        } catch (MissingResourceException e) {
            return "";
        }
    }
}
