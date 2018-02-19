package org.usfirst.frc.team1089.robot.subsystems;

import org.usfirst.frc.team1089.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This {@link Subsystem} encapsulates the analog ultrasonic, as well as some
 * constants that need to be used with the ultrasonic.
 *
 * @see AnalogInput
 */
public class Ultrasonic extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private AnalogInput ultrasonic;
    private final double
            VOLTAGE_MIN,    // The range of the distances returned by this class in ft
            VOLTAGE_RANGE,  // The range of the voltages returned by the sensor
            DISTANCE_MIN,   // Minimum distance the ultrasonic sensor can return in ft
            DISTANCE_RANGE;	// Minimum voltage the ultrasonic sensor can return

    /** Scaling factor - inches / volts */
    private final double SCALING_FACTOR = /*1/*/9.8;

    public Ultrasonic() {
        ultrasonic = new AnalogInput(0);
        VOLTAGE_MIN = .5;
        VOLTAGE_RANGE = 5.0 - VOLTAGE_MIN;
        DISTANCE_MIN = 0.25;
        DISTANCE_RANGE = 5 - DISTANCE_MIN;
    }

    public AnalogInput getUltrasonic() {
        return ultrasonic;
    }

    /**
     * <pre>
     * public double getRange()
     * </pre>
     * Gets the range between the rangefinder board and the object across from it
     * as perceived by the rangefinder by getting the voltage and multiplying it by the scaling factor
     *
     * @return the range between the board and the object across from it in inches
     */
    public double getRange() {

        return ultrasonic.getVoltage() * SCALING_FACTOR;
    	/*double range = ultrasonic.getVoltage();
    	//first, normalize the voltage
    	range = (range - min_voltage) / voltage_range;
    	//next, denormalize to the unit range
    	range = (range * distance_range) + min_distance;
    	return range;*/
    }

    public void initDefaultCommand() {
        //setDefaultCommand(new DriveToWall());
    }
}