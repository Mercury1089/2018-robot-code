package org.usfirst.frc.team1089.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.auton.AutonPosition;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;

/**
 * Helper class to interface with the Shuffleboard Dashboard.
 */
public class ShuffleDash {
    public double maxRevsPerMinRight = 0.0, maxRevsPerMinLeft = 0.0;
    public SendableChooser<AutonPosition> startingPosition;

    //tOdO find out what to do here
    /*public SendableChooser<AutonTask> autonTaskLLL_1, autonTaskLLL_2, autonTaskLLL_3, autonTaskLLL_4,
            autonTaskLRL_1, autonTaskLRL_2, autonTaskLRL_3, autonTaskLRL_4,
            autonTaskRLR_1, autonTaskRLR_2, autonTaskRLR_3, autonTaskRLR_4,
            autonTaskRRR_1, autonTaskRRR_2, autonTaskRRR_3, autonTaskRRR_4;
    public SendableChooser<ScoringSide> scoringSideLLL_1, scoringSide_2, scoringSideLLL_3, scoringSideLLL_4,
            scoringSideLRL_1, scoringSideLRL_2, scoringSideLRL_3, scoringSideLRL_4,
            scoringSideRLR_1, scoringSideRLR_2, scoringSideRLR_3, scoringSideRLR_4,
            scoringSideRRR_1, scoringSideRRR_2, scoringSideRRR_3, scoringSideRRR_4;*/

    public ShuffleDash() {
        runAutonOnShuffleboard();
        new Notifier(this::updateDash).startPeriodic(0.050);
    }

    private void updateDash() {
        SmartDashboard.putString("FMS Data", GameData.getParsedString());
        SmartDashboard.putString("Alliance Color", DriverStation.getInstance().getAlliance().toString());
        if (Robot.driveTrain.getGyro() != null)
            SmartDashboard.putNumber("NavX Angle", Robot.driveTrain.getGyro().getAngle());

        SmartDashboard.putNumber("Left Enc in ticks", Robot.driveTrain.getLeft().getSelectedSensorPosition(DriveTrain.PRIMARY_PID_LOOP));
        SmartDashboard.putNumber("Right Enc in ticks", Robot.driveTrain.getRight().getSelectedSensorPosition(DriveTrain.PRIMARY_PID_LOOP));
        SmartDashboard.putNumber("Left Enc in feet", Robot.driveTrain.getLeftEncPositionInFeet());
        SmartDashboard.putNumber("Right Enc in feet", Robot.driveTrain.getRightEncPositionInFeet());
        SmartDashboard.putNumber("Elevator Enc in ticks", Robot.elevator.getElevatorTalon().getSelectedSensorPosition(DriveTrain.PRIMARY_PID_LOOP));
        SmartDashboard.putString("DriveTrain", Robot.driveTrain.getCurrentCommandName());
        SmartDashboard.putNumber("Left Wheel RPM", MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getLeft().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP))); //ticks per tenth of a second
        SmartDashboard.putNumber("Right Wheel RPM", MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getRight().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP)));
        SmartDashboard.putNumber("LIDAR Raw Distance (in.)", MercMath.roundFloat(Robot.claw.getLidar().getRawDistance(), 10));
        SmartDashboard.putNumber("LIDAR Distance (in.)", MercMath.roundFloat(Robot.claw.getLidar().getDistance(), 10));
        SmartDashboard.putNumber("LIDAR Period", MercMath.roundFloat(Robot.claw.getLidar().getDistance(), 10));
        SmartDashboard.putNumber("Gyro Angle", Robot.driveTrain.getGyro().getAngle());
        SmartDashboard.putNumber("Ultrasonic Distance", Robot.claw.getUltrasonic().getRange());
        SmartDashboard.putNumber("Pixy Displacement", Robot.claw.getPixyCam().pidGet());

        double recentRevsPerMinLeft = MercMath.ticksPerTenthToRevsPerMinute(Robot.driveTrain.getLeft().getSelectedSensorVelocity(DriveTrain.PRIMARY_PID_LOOP));
    }

    private void runAutonOnShuffleboard() {
        startingPosition = new SendableChooser<>();
        startingPosition.addObject("Left", AutonPosition.LEFT);
        startingPosition.addObject("Right", AutonPosition.RIGHT);
        SmartDashboard.putData("Auton Starting Position", startingPosition);
    }
}
