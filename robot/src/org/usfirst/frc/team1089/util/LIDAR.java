package org.usfirst.frc.team1089.util;
import com.ctre.phoenix.CANifier;


/**
 * Wrapper class for the entire LIDAR system that we are using to check for distance.
 */
public class LIDAR {
    private CANifier canifier;
    private CANifier.PWMChannel pwmChannel;
    private final double[] PWM_INPUT = new double[2];
    private LidarNum lidarNum;

    private final double COEFFICIENT, CONSTANT;

    public enum LidarNum {ONE, TWO};

    /**
     * Creates a new LIDAR by defining both the CANifier PWM channel that the
     * LIDAR is connected to, as well as the ID of the CANifier that the
     * LIDAR is connected to.
     *
     * @param deviceID ID of the CANifier
     * @param channel  PWMChannel of the LIDAR
     */
    public LIDAR(int deviceID, CANifier.PWMChannel channel, LidarNum lidarNum) {
        canifier = new CANifier(deviceID);
        pwmChannel = channel;
        this.lidarNum = lidarNum;

        switch (lidarNum) {
            case ONE:
                CONSTANT = -5.55;
                COEFFICIENT = 1.0;
                break;
            case TWO:
                CONSTANT = -4.67;
                COEFFICIENT = 1.02;
                break;
            default:
                CONSTANT = 0.0;
                COEFFICIENT = 0.0;
                break;
        }

        // canifier(channel.value, true);
    }

    /**
     * Gets the distance from the LIDAR sensor
     * @return the distance sensed from the LIDAR, in centimeters.
     */
    public double[] getDistance() {
        // returns [PWM Value, Period]
        canifier.getPWMInput(pwmChannel, PWM_INPUT);

        // 10 us (microseconds) to 1 cm to 1 inch
        return new double[] {
                PWM_INPUT[0] / 10.0 / 2.54,
                PWM_INPUT[1]
        }; // TODO: use a conversion method in MercMath
    }

    public double getFixedDistance() {
        return fixDistance(getDistance()[0]);
    }

    public double getPaddedDistance() {
        return fixDistance(getDistance()[0]) - 2;
    }

    public double fixDistance(double rawVal) {
        double newVal;

        //Equations are based of lines of best fit calculated from Luke's amazing data
        newVal = COEFFICIENT * rawVal + CONSTANT;

        return newVal;
    }
}
