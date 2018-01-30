package org.usfirst.frc.team1089.config;

import java.io.FileReader;
import java.util.Properties;

/**
 * Abstract wrapper class for config files.
 * This needs to be initialized prior to usage.
 * This should not be instantiated, rather other classes need to implement it.
 */
public abstract class Config {
    private static Properties instance;

    protected static void loadProperties(String fileName) {
        if (instance != null) {
            try {
                FileReader loader = new FileReader(fileName);
                instance.load(loader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static Properties getInstance() {
        if (instance == null)
            instance = new Properties();
        return instance;
    }

    /**
     * Parses a property that is written as an array
     * @param key       the property key to get the value from
     * @param delimiter the string separating the elements of the array
     *
     * @return String array of the property's value,
     *         or a string array with only "" if there is nothing.
     */
    protected static String[] parseArrayValue(String key, String delimiter) {
        String[] arr = instance.getProperty(key, "").split(delimiter);

        if (arr.length == 0)
            return new String[] {""};

        for (int i = 0; i < arr.length; i++)
            arr[i] = arr[i].trim();

        return arr;
    }
}
