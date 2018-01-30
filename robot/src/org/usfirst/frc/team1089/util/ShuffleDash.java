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
    public double maxRevsPerMinRight = 0.0, maxRevsPerMinLeft = 0.0;
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
        SmartDashboard.putNumber("Left Wheel RPM", MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getLeft().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP))); //ticks per tenth of a second
        SmartDashboard.putNumber("Right Wheel RPM", MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getRight().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP)));
        SmartDashboard.putNumber("LIDAR Distance (in.)", MercMath.roundFloat(Robot.manipulator.getLidar().getDistance()[0], 10));
        SmartDashboard.putNumber("LIDAR Fixed Distance (in.)", MercMath.roundFloat(Robot.manipulator.getLidar().getPaddedDistance(), 10));

        SmartDashboard.putNumber("LIDAR Period", MercMath.roundFloat(Robot.manipulator.getLidar().getDistance()[1], 10));

        double recentRevsPerMinLeft = MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getLeft().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP));
        if(Math.abs(recentRevsPerMinLeft) > Math.abs(maxRevsPerMinLeft))
            maxRevsPerMinLeft = Math.abs(recentRevsPerMinLeft);
        double recentRevsPerMinRight = MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getRight().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP));
        if(Math.abs(recentRevsPerMinRight) > Math.abs(maxRevsPerMinRight))
            maxRevsPerMinRight = Math.abs(recentRevsPerMinRight);
        SmartDashboard.putNumber("Left Max RPM", maxRevsPerMinLeft);
        SmartDashboard.putNumber("Right Max RPM", maxRevsPerMinRight);

    }
}
