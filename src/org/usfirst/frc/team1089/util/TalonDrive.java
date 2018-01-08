package org.usfirst.frc.team1089.util;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Helper class for various manual drive controls
 * using Talon SRX's. This is to replace {@link DifferentialDrive},
 * which can interfere with Talon SRX's in closed-loop mode.
 * 
 */
public class TalonDrive {
	private final TalonSRX T_FRONT_LEFT, T_FRONT_RIGHT, T_BACK_LEFT, T_BACK_RIGHT;
	private double maxOutput = 1.0;

	/**
	 * Creates a drive train, assuming it has four Talons, 
	 * two for the front, two for the back. 
	 * 
	 * The back talons are set to follow their respective front talons,
	 * i.e.: backLeft follows frontLeft, backRight follows frontRight.
	 * 
	 * @param frontLeft  Front-left Talon ID
	 * @param frontRight Front-right Talon ID
	 * @param backLeft   Back-left Talon ID
	 * @param backRight  Back-right Talon ID
	 */
	public TalonDrive(int frontLeft, int frontRight, int backLeft, int backRight) {
		T_FRONT_LEFT = new TalonSRX(frontLeft);
		T_FRONT_RIGHT = new TalonSRX(frontRight);
		T_BACK_LEFT = new TalonSRX(backLeft);
		T_BACK_RIGHT = new TalonSRX(backRight);
		
		// Set follower control on back talons.
		T_BACK_LEFT.set(ControlMode.Follower, frontLeft);
		T_BACK_LEFT.set(ControlMode.Follower, frontRight);
	}
	
	/**
	 * Sets the max output that can be taken in by the Talons.
	 * The value is in change/100ms
	 * 
	 * @param max Max output value
	 */
	public void setMaxOutput(double max) {
		maxOutput = max;
	}
	
	/**
	 * Single stick driving. This is done by using one axis for forwards/backwards,
	 * and another for turning right/left. This method allows direct input from any joystick
	 * value.
	 * 
	 * @param moveVal      Value for forwards/backwards
	 * @param rotateVal    Value for rotation right/left
	 * @param squareInputs If set, decreases sensitivity at lower speeds
	 */
	public void arcadeDrive(double moveVal, double rotateVal, boolean squareInputs) {
		double leftPercent, rightPercent;
		
		// Clamp moveVal and rotateVal.
		// Assume a deadzone is already being applied to these values.
		moveVal = MercMath.clamp(moveVal, -1.0, 1.0);
		rotateVal = MercMath.clamp(rotateVal, -1.0, 1.0);
		
		// Square inputs, but maintain their signs.
		// This allows for more precise control at lower speeds,
		// but permits full power.
		if (squareInputs) {
			moveVal = Math.copySign(moveVal * moveVal, moveVal);
			rotateVal = Math.copySign(rotateVal * rotateVal, rotateVal);
		}
		
		// Set left and right motor speeds.
		if (moveVal > 0) {
			if (rotateVal > 0) {
				leftPercent = moveVal - rotateVal;
				rightPercent = Math.max(moveVal, rotateVal);
			} else {
				leftPercent = Math.max(moveVal, -rotateVal);
				rightPercent = moveVal + rotateVal;
			}
		} else {
			if (rotateVal > 0) {
				leftPercent = -Math.max(-moveVal, rotateVal);
				rightPercent = moveVal + rotateVal;
			} else {
				leftPercent = moveVal - rotateVal;
				rightPercent = -Math.max(-moveVal, -rotateVal);
			}
		}
		
		// Clamp motor percents
		leftPercent = MercMath.clamp(leftPercent, -1.0, 1.0);
		rightPercent = MercMath.clamp(rightPercent, -1.0, 1.0);
		
		// Apply speeds to motors
		T_FRONT_LEFT.set(ControlMode.Velocity, leftPercent * maxOutput);
		T_FRONT_RIGHT.set(ControlMode.Velocity, rightPercent * maxOutput);
	}
}
