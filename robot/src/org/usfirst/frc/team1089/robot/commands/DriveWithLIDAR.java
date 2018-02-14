package org.usfirst.frc.team1089.robot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;

/**
 * Drives to the LIDAR's target a specified distance away using the LIDAR's distance measurements.
 */
public class DriveWithLIDAR extends DriveDistance {
    private final Logger LOG = LogManager.getLogger(DriveWithLIDAR.class);

    public double minimumDistance;

    /**
     * @param minDist The distance for the robot to be away from the LIDAR's target when it reaches said target.
     */
    public DriveWithLIDAR(double minDist, double percentVoltage) {
        super(0, percentVoltage);
        requires(Robot.driveTrain);
        minimumDistance = minDist;
        LOG.info("DriveWithLIDAR constructed with minimum distance of "  + minimumDistance);
    }

    @Override
    protected void initialize() {
        super.initialize();
        LOG.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        distance = Robot.claw.getLidar().getDistance() - minimumDistance;
        updateDistance();
        LOG.info(getName() + " executing");
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished() && Robot.claw.getLidar().getDistance() - minimumDistance <= 0;
    }

    @Override
    protected void end() {
        super.end();
        LOG.info(getName() + " ended");
    }
}
