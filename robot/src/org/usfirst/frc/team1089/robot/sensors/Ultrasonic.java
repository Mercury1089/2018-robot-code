package org.usfirst.frc.team1089.robot.sensors;

import org.usfirst.frc.team1089.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This {@link Subsystem} encapsulates the analog ultrasonic, as well as some
 * constants that need to be used with the ultrasonic.
 *
 * @see AnalogInput
 */
public class Ultrasonic {
    private AnalogInput ultrasonic;

    private final double
            VOLTAGE_MIN,    // The range of the distances returned by this class in ft
            VOLTAGE_RANGE,  // The range of the voltages returned by the sensor
            DISTANCE_MIN,   // Minimum distance the ultrasonic sensor can return in ft
            DISTANCE_RANGE;	// Minimum voltage the ultrasonic sensor can return

    /** Scaling factor - inches / volts */
    private final double SCALING_FACTOR = /*1/*/9.8;

    public Ultrasonic(int port) {
        ultrasonic = new AnalogInput(port);
        VOLTAGE_MIN = .5;
        VOLTAGE_RANGE = 5.0 - VOLTAGE_MIN;
        DISTANCE_MIN = 0.25;
        DISTANCE_RANGE = 5 - DISTANCE_MIN;
    }

    /**
     * Gets the range between the rangefinder board and the object across from it
     * as perceived by the rangefinder by getting the voltage and multiplying it by the scaling factor
     *
     * @return the range between the board and the object across from it in inches
     */
    public double getRange() {
        return ultrasonic.getVoltage() * SCALING_FACTOR;

        // TODO: Figure out conversion method from volts to inches
    	/*double range = ultrasonic.getVoltage();
    	//first, normalize the voltage
    	range = (range - min_voltage) / voltage_range;
    	//next, denormalize to the unit range
    	range = (range * distance_range) + min_distance;
    	return range;*/
    }

    /**
     * Gets the raw, unprocessed voltage from the ultrasonic
     *
     * @return the raw voltage being sent from the ultrasonic
     */
    public double getRawVoltage() {
        return ultrasonic.getVoltage();
    }
}