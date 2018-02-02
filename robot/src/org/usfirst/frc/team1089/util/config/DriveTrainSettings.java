package org.usfirst.frc.team1089.util.config;

import java.util.Properties;

/**
 * Change the values in the config file, NOT this one.
 *  -Luke, 2 hours after changing the values in this file and getting no change
 */
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

    /**
     * Gets the minimum percent VBus value for RotateRelative PID
     *
     * @return min output, represented as percent value [0.0, 1.0]
     */
    public static double getRotMinPVBus() {
        String val = instance.getProperty("rotateRelative.minPercentVBus", "0.3").trim();

        return Double.parseDouble(val);
    }

    /**
     * Gets the max output voltage range for RotateRelative PID
     *
     * @return double array containing minimum and maximum output percent value
     */
    public static double[] getRotOutputRange() {
        String[] arr = parseArrayValue("rotateRelative.outputRange", ",");
        double[] range = {-1, 1};

        if (arr.length == 2) {
            range[0] = Double.parseDouble(arr[0]);
            range[1] = Double.parseDouble(arr[1]);
        }

        return range;
    }

    /**
     * Gets the absolute tolerance for RotateRelative PID
     *
     * @return max degree delta from target heading
     */
    public static double getRotAbsTolerance() {
        String val = instance.getProperty("rotateRelative.absoluteTolerance", "1").trim();

        return Double.parseDouble(val);
    }

    /**
     * Gets the PID values for RotateRelative
     *
     * @return double array containing PID values
     */
    public static double[] getPIDValues() {
        String[] arr = parseArrayValue("rotateRelative.PID", ",");
        double[] pid = {0.005, 0, 0};

        if (arr.length == 3) {
            pid[0] = Double.parseDouble(arr[0]);
            pid[1] = Double.parseDouble(arr[1]);
            pid[2] = Double.parseDouble(arr[2]);
        }

        return pid;
    }
}