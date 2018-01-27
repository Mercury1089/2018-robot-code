package org.usfirst.frc.team1089.util;
import com.ctre.phoenix.CANifier;

/**
 * Wrapper class for the entire LIDAR system that we are using to check for distance.
 */
public class LIDAR {
    private CANifier canifier;
    private CANifier.PWMChannel pwmChannel;
    private final double[] PWM_INPUT = new double[2];

    /**
     * Creates a new LIDAR by defining both the CANifier PWM channel that the
     * LIDAR is connected to, as well as the ID of the CANifier that the
     * LIDAR is connected to.
     *
     * @param deviceID ID of the CANifier
     * @param channel  PWMChannel of the LIDAR
     */
    public LIDAR(int deviceID, CANifier.PWMChannel channel) {
        canifier = new CANifier(deviceID);
        pwmChannel = channel;

        // canifier(channel.value, true);
    }

    /**
     * Gets the distance from the LIDAR sensor
     * @return the distance sensed from the LIDAR, in centimeters.
     */
    public double getDistance() {
        // returns [PWM Value, Period]
        canifier.getPWMInput(pwmChannel, PWM_INPUT);

        // 10 us (microseconds) to 1 cm to 1 inch
        return PWM_INPUT[0] / 10.0 / 2.54; // TODO: use a conversion method in MercMath
    }

}
