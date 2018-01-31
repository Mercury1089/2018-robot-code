package org.usfirst.frc.team1089.util;

import org.usfirst.frc.team1089.robot.Robot;

/**
 * Class that contains various math functions.
 */
public class MercMath {

	/**
	 * Clamps a value between a minimum and maximum, inclusive.
	 * 
	 * @param val the value to clamp
	 * @param min the minimum value
	 * @param max the maximum value
	 * @return {@code val}, if {@code val} is between [{@code min}, {@code max}]
	 * 		   {@code min}, if {@code val} is <= {@code min}
	 * 		   {@code min}, if {@code val} is >= {@code min}
	 */
	public static double clamp(double val, double min, double max) {
		if (val <= min)
			val = min;
		else if (val >= max)
			val = max;
		
		return val;
	}

	/**
	 * Rounds a floating-point value to a certain number of places past the decimals
	 * @param val    the number to round
	 * @param places the number of places to round to
	 * @return the value, truncated to the set amount of places
	 */
	public static double roundFloat(double val, int places) {
//		double factor = Math.pow(10, places);
//		val *= factor;
//
//		val = (int)(val + 0.5);
//
//		val /= factor;

		return val;
	}

	public static double centimetersToInches(double val) {
		return val / 0.393700787;
	}

	public static double inchesToCentimeters(double val) {
		return val * 0.393700787;
	}

	public static double secondsToMinutes(double val) {
		return val / 60;
	}

	public static double minutesToSeconds(double val) {
		return val * 60;
	}

	public static double minutesToHours(double val) {
		return val / 60;
	}

	public static double hoursToMinutes(double val) {
		return val * 60;
	}

	public static double feetToMeters(double val) {
		return 100 * inchesToCentimeters(val / 12);
	}

	public static double metersToFeet(double val) {
		return 1 / (100 * inchesToCentimeters(val / 12));
	}

	public static double ticksToMeters(double ticks) {
		return inchesToCentimeters(encoderTicksToInches(ticks)) * 100;
	}

	/*public double getLeftEncPositionInFeet() {
		double ticks = Robot.driveTrain.getLeft().getSelectedSensorPosition(Robot.driveTrain.PRIMARY_PID_LOOP);
		//Convert encoder ticks to feet
		return ((Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES) /
				(Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * Robot.driveTrain.GEAR_RATIO) * ticks) / 12;
	}

	public double getRightEncPositionInFeet() {
		double ticks = Robot.driveTrain.getRight().getSelectedSensorPosition(Robot.driveTrain.PRIMARY_PID_LOOP);
		//Convert encoder ticks to feet
		return ((Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES) /
				(Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * Robot.driveTrain.GEAR_RATIO) * ticks) / 12;
	}*/

	public static double getEncPosition(double ticks) {
		return ((Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES) /
				(Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * Robot.driveTrain.GEAR_RATIO) * ticks) / 12;
	}

	/**
	 * <pre>s
	 * public double feetToEncoderTicks(double feet)
	 * </pre>
	 * Returns a value in ticks based on a certain value in feet using
	 * the Magnetic Encoder.
	 * @param feet The value in feet
	 * @return The value in ticks
	 */
	public static double feetToEncoderTicks(double feet) {
		return (Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * Robot.driveTrain.GEAR_RATIO) /
				(Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES) * feet * 12.0;
	}

	public static double inchesToEncoderTicks(double inches) {
		return (inches / (Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES)) * Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION;
	}

	public static double encoderTicksToInches(double ticks) {
		return ticks / Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * (Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES);
	}

	/**
	 * <pre>
	 *     public double ticksPerTenthToRevsPerMinute(double ticksPerTenthSecond)
	 * </pre>
	 * Returns value in revolutions per minute given ticks per tenth of a second.
	 * @param ticksPerTenthSecond
	 * @return Revs per minute
	 */
	public static double ticksPerTenthToRevsPerMinute(double ticksPerTenthSecond) {
		return ticksPerTenthSecond / Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * 600;
	}

    public static double revsPerMinuteToTicksPerTenth(double revsPerMinute) {
        return revsPerMinute * Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION / 600;
    }

	public static double revsPerMinuteToMetersPerSecond(double revsPerMinute) {
		return revsPerMinute * feetToMeters(Robot.driveTrain.WHEEL_DIAMETER_INCHES * Math.PI / 12) / 60;
	}

	public static double ticksPerTenthToMetersPerSecond(double ticksPerTenth) {
		return revsPerMinuteToMetersPerSecond(ticksPerTenthToRevsPerMinute(ticksPerTenth));
	}

		public static double calculateFeedForward(double rpm) {
		final double MAX_MOTOR_OUTPUT = 1023;
		final double NATIVE_UNITS_PER_100 = rpm * 1/600 * Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION;
		return MAX_MOTOR_OUTPUT/NATIVE_UNITS_PER_100;
	}
}
