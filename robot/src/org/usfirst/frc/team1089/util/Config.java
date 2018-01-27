package org.usfirst.frc.team1089.util;

import java.io.FileReader;
import java.util.Properties;

/**
 * Class that handles loading properties file into robot code
 * and handling of properties object
 */
public class Config {
    private static Properties instance;

    public static void initialize() {
        if (instance == null) {
            instance = new Properties();
            try {
                FileReader loader = new FileReader("robot.properties");
                instance.load(loader);
            } catch (Exception e) { } // Unhandled, because we live dangerously on the edge
        }
    }

    public static Properties getInstance() {
        return instance;
    }
}
