package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.PIDCommand;
import org.usfirst.frc.team1089.robot.Robot;


/**
 * Drives to the LIDAR's target a specified distance away using the LIDAR's distance measurements.
 */
public class DriveWithLIDAR extends DriveDistance {

    public double minimumDistance;

    private static String currentMethod = "";

    /**
     * @param minimumDistance The distance for the robot to be away from the LIDAR's target when it reaches said target.
     */
    public DriveWithLIDAR(double minimumDistance, double percentVoltage) {
        super(Robot.manipulator.getLidar().getFixedDistance() - minimumDistance, percentVoltage);
        requires(Robot.driveTrain);
        this.minimumDistance = minimumDistance;
        System.out.println("DriveWithLIDAR constructed with minimum distance of "  + minimumDistance);
    }


    @Override
    protected void initialize() {
        distance = Robot.manipulator.getLidar().getFixedDistance() - minimumDistance;
        super.initialize();
        currentMethod = "DriveWithLIDAR.initialize();";
    }

    @Override
    protected void execute() {
        distance = Robot.manipulator.getLidar().getFixedDistance() - minimumDistance;
        updateDistance();
        currentMethod = "DriveWithLIDAR.execute();";
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() && Robot.manipulator.getLidar().getFixedDistance() - minimumDistance <= 0;
    }

    @Override
    protected void end() {
        super.end();
        currentMethod = "DriveWithLIDAR.end();";
    }
}
