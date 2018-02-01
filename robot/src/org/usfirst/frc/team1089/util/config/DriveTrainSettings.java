package org.usfirst.frc.team1089.util.config;

import java.util.Properties;

public class DriveTrainSettings extends Config {
    /**
     * Enumeration of motor controller layouts for the drive train.
     * <p>
     * {@code DEFAULT}: 2 Victor SPX followers, 2 Talon SRX leaders
     * <p>
     * {@code LEGACY}: 2 Talon SRX followers, 2 Talon SRX leaders
     */
    public enum DriveTrainLayout {
        DEFAULT,
        LEGACY
    }

    private static Properties instance;

    public static void initialize() {
        instance = getInstance();
        loadProperties("drivetrain.properties");
    }

    /**
     * Gets the controller layout used for the drive train.
     *
     * @return the drive train layout
     */
    public static DriveTrainLayout getControllerLayout() {
        String layout = instance.getProperty("driveTrain.layout", "default").trim();

        return DriveTrainLayout.valueOf(layout.toUpperCase());
    }

    /**
     * Gets the max output for the drive train motor controllers
     *
     * @return max output, represented as percent value [0.0, 1.0]
     */
    public static double getMaxOutput() {
        String val = instance.getProperty("driveTrain.maxOutput", "1.0").trim();

        return Double.parseDouble(val);
    }
}
