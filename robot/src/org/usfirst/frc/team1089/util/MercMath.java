package org.usfirst.frc.team1089.util;

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
}
