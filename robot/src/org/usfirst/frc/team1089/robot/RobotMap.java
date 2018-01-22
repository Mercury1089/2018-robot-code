package org.usfirst.frc.team1089.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * <p>
 * For example to map the left and right motors, you could define the
 * following variables to use with your drivetrain subsystem.
 * <pre>
 * public static int leftMotor = 1;
 * public static int rightMotor = 2;
 * </pre>
 * <p>
 * If you are using multiple modules, make sure to define both the port
 * number and the module. For example you with a rangefinder:
 * <pre>
 * public static int rangefinderPort = 1;
 * public static int rangefinderModule = 1;
 * </pre>
 */
public class RobotMap {
	/**
	 * Class containing constants for the ports of all CAN devices.
	 */
	public static class CAN {
		public static final int 
			TALON_DRIVETRAIN_FL = 4,
			TALON_DRIVETRAIN_FR = 2,
			TALON_DRIVETRAIN_BR = 1,
			TALON_DRIVETRAIN_BL = 3;
		
		private CAN() { } // Should never be constructed.
	}
	
	/**
	 * Class containing constants for ports of the devices on the USB interface of the Driver Station.
	 * Good for OI joystick ports and of the like.
	 */
	public static class DS_USB {
		public static final int 
			LEFT_STICK = 0,
			RIGHT_STICK = 1,
			GAMEPAD = 2;
		
		private DS_USB() { } // Should never be constructed.
	}
}
