package org.usfirst.frc.team1089.util;

import org.usfirst.frc.team1089.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Helper class to interface with the Shuffleboard Dashboard.
 *
 */
public class ShuffleDash {
	public void updateDash() {
		SmartDashboard.putString("FMS Data", DriverStation.getInstance().getGameSpecificMessage());
		SmartDashboard.putString("Alliance Color", DriverStation.getInstance().getAlliance().toString());
		SmartDashboard.putNumber("NavX Angle", Robot.ahrs.getAngle());
	}
}
