package org.usfirst.frc.team1089.util;

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
		if(Robot.SmartDashboard.putNumber("NavX Angle", Robot.ahrs.getAngle());
		SmartDashboard.putNumber("Left Enc in ticks", Robot.driveTrain.getLeft().getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("Right Enc in ticks", Robot.driveTrain.getRight().getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("Left Enc in feet", Robot.driveTrain.getLeftEncPositionInFeet());
		SmartDashboard.putNumber("Right Enc in feet", Robot.driveTrain.getRightEncPositionInFeet());
	}
}
