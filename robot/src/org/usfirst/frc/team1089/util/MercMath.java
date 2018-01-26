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
}
