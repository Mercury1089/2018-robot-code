package org.usfirst.frc.team1089.robot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;


/**
 * Drives to the LIDAR's target a specified distance away using the LIDAR's distance measurements.
 */
public class DriveWithLIDAR extends DriveDistance {

    public double minimumDistance;
    private static Logger log = LogManager.getLogger(DriveWithLIDAR.class);

    private static String currentMethod = "";

    /**
     * @param minimumDistance The distance for the robot to be away from the LIDAR's target when it reaches said target.
     */
    public DriveWithLIDAR(double minimumDistance, double percentVoltage) {
        super(Robot.claw.getLidar().getDistance() - minimumDistance, percentVoltage);
        requires(Robot.driveTrain);
        this.minimumDistance = minimumDistance;
        log.info("DriveWithLIDAR constructed with minimum distance of "  + minimumDistance);
    }


    @Override
    protected void initialize() {
        distance = Robot.claw.getLidar().getDistance() - minimumDistance;
        super.initialize();
        log.info(getName() + " Initialized");
        currentMethod = "DriveWithLIDAR.initialize();";
    }

    @Override
    protected void execute() {
        distance = Robot.claw.getLidar().getDistance() - minimumDistance;
        updateDistance();
        currentMethod = "DriveWithLIDAR.execute();";
        log.info(getName() + " executing");
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() && Robot.claw.getLidar().getDistance() - minimumDistance <= 0;
    }

    @Override
    protected void end() {
        super.end();
        currentMethod = "DriveWithLIDAR.end();";
        log.info(getName() + " Ended");
    }
}
