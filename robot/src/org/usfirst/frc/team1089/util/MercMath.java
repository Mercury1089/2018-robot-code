package org.usfirst.frc.team1089.util;

import org.usfirst.frc.team1089.robot.Robot;

import java.nio.ByteBuffer;

/**
 * Class that contains various math functions.
 */
public class MercMath {
	private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

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

	// TODO: Make this work
	/**
	 * Rounds a floating-point value to a certain number of places past the decimals
	 * @param val    the number to round
	 * @param places the number of places to round to
	 * @return the value, truncated to the setClawState amount of places
	 */
	public static double roundFloat(double val, int places) {
		double factor = Math.pow(10.0, places);

		return Math.round(val * factor) / factor;
	}

	/**
	 * Interpolates a value between a minimum and maximum value via a percentage
	 *
	 * @param percent percent to use to interpolate between a and b
	 * @param a some value
	 * @param b some other value
	 * @return a value between a and b given a percent, with 0 being min, and 1 being max
	 */
	public static double lerp(double percent, double a, double b) {
		percent = clamp(percent, 0, 1);

		return percent * b - percent * a + a;
	}

	public static double centimetersToInches(double val) {
		return val / 2.54;
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

	public static double getEncPosition(double ticks) {
		return ((Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES) /
				(Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION * Robot.driveTrain.GEAR_RATIO) * ticks) / 12;
	}

	/**
	 * Returns a value in ticks based on a certain value in feet using
	 * the Magnetic Encoder.
	 *
	 * @param feet The value in feet
	 * @return The value in ticks
	 */
	public static double feetToEncoderTicks(double feet) {
		return inchesToEncoderTicks(feet * 12);
	}

	public static double inchesToEncoderTicks(double inches) {
		return inches / (Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES) * Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION;
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
		final double NATIVE_UNITS_PER_100 = rpm / 600 * Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION;
		return MAX_MOTOR_OUTPUT/NATIVE_UNITS_PER_100;
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	// This doesn't work yet. At least that's what I recall.
	public static String intsToHex(int[] ints) {
		StringBuilder hexString = new StringBuilder();
		for (int i : ints)
			hexString.append(Integer.toHexString(i));

		return hexString.toString();
	}

	// Some byte math conversion thing
	public static String bbToString(ByteBuffer bb) {
		final byte[] b = new byte[bb.remaining()];
		bb.duplicate().get(b);
		bb.rewind();
		return MercMath.bytesToHex(b);
	}
}
