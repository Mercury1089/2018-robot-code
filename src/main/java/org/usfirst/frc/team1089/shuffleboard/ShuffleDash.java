package org.usfirst.frc.team1089.shuffleboard;

import org.usfirst.frc.team1089.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Helper class to interface with the Shuffleboard Dashboard.
 *
 */
public class ShuffleDash {
	public ShuffleDash() {
		new Notifier(() -> updateDash()).startPeriodic(0.050);
	}
	public void updateDash() {
		SmartDashboard.putString("FMS Data", DriverStation.getInstance().getGameSpecificMessage());
		SmartDashboard.putString("Alliance Color", DriverStation.getInstance().getAlliance().toString());
		if(Robot.navX != null)
			SmartDashboard.putNumber("NavX Angle", Robot.navX.getAngle());
	}
}