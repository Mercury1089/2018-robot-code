package org.usfirst.frc.team1089.robot;

import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	private final double DEADZONE = 0.1;
	
	private Joystick leftStick, rightStick, gamepad;

	public OI() {
		leftStick = new Joystick(DS_USB.LEFT_STICK);
		rightStick = new Joystick(DS_USB.RIGHT_STICK);
		gamepad = new Joystick(DS_USB.GAMEPAD);
		
		// Gamepad binds
	}
	
	/**
	 * Gets the move value for driving forwards/backwards.
	 * A deadzone is applied to this value.
	 * 
	 * @return Y-axis of left joystick.
	 */
	public double getMoveValue() {
		return applyDeadzone(leftStick.getY());
	}
	
	/**
	 * Gets the move value for rotating right/left.
	 * A deadzone is applied to this value.
	 * 
	 * @return X-axis of right joystick.
	 */
	public double getRotateValue() {
		return applyDeadzone(leftStick.getX());
	}
	
	/**
	 * Applies the deadzone to a given value.
	 * 
	 * @param val value to apply a deadzone to.
	 * @return {@code val} if |{@code val}| >= 0.1, 0 otherwise.
	 */
	private double applyDeadzone(double val) {
		return Math.abs(val) >= 0.1 ? val : 0;
	}
}
