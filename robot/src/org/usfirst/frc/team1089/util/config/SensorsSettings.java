package org.usfirst.frc.team1089.util.config;

import org.usfirst.frc.team1089.util.LIDAR;

import java.util.Properties;

/**
 * Settings specifically applied to sensors.
 */
public class SensorsSettings extends Config {
    private static Properties instance;

    public static void initialize() {
        instance = getInstance();
        loadProperties("sensors.properties");
    }

    /**
     * Gets the PWN offset equation used by the LIDAR.
     *
     * @return {@code PWMOffset} value
     */
    public static LIDAR.PWMOffset getEquation() {
        String eq = instance.getProperty("lidar.offsetEquation", "default");

        switch (eq.toUpperCase()) {
            case "EQUATIONA":
            case "EQUATION_A":
                return LIDAR.PWMOffset.EQUATION_A;
            case "EQUATIONB":
            case "EQUATION_B":
                return LIDAR.PWMOffset.EQUATION_B;
            default:
                return LIDAR.PWMOffset.DEFAULT;
        }
    }
}
