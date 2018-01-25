package org.usfirst.frc.team1089.util;

import org.usfirst.frc.team1089.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;

/**
 * Helper class to interface with the Shuffleboard Dashboard.
 */
public class ShuffleDash {
    public ShuffleDash() {
        new Notifier(this::updateDash).startPeriodic(0.050);
    }

    private void updateDash() {
        SmartDashboard.putString("FMS Data", DriverStation.getInstance().getGameSpecificMessage());
        SmartDashboard.putString("Alliance Color", DriverStation.getInstance().getAlliance().toString());
        if (Robot.driveTrain.getGyro() != null)
            SmartDashboard.putNumber("NavX Angle", Robot.driveTrain.getGyro().getAngle());

        SmartDashboard.putNumber("Left Enc in ticks", Robot.driveTrain.getLeft().getSelectedSensorPosition(DriveTrain.PRIMARY_PID_LOOP));
        SmartDashboard.putNumber("Right Enc in ticks", Robot.driveTrain.getRight().getSelectedSensorPosition(DriveTrain.PRIMARY_PID_LOOP));
        SmartDashboard.putNumber("Left Enc in feet", Robot.driveTrain.getLeftEncPositionInFeet());
        SmartDashboard.putNumber("Right Enc in feet", Robot.driveTrain.getRightEncPositionInFeet());
        SmartDashboard.putString("DriveTrain", Robot.driveTrain.getCurrentCommandName());
    }
}
