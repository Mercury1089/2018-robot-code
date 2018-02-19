package org.usfirst.frc.team1089.robot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

/**
 * Autonomous alignment command that uses {@link org.usfirst.frc.team1089.robot.sensors.PiVision PiVision}
 * to align to a target.
 *
 * @deprecated see {@link org.usfirst.frc.team1089.robot.sensors.PiVision PiVision}
 */
public class AutoAlign extends RotateRelative {
//    private final double ANGLE_THRESHOLD;
//    private static Logger LOG = LogManager.getLogger(AutoAlign.class);
//    public AutoAlign() {
//        super();
//        LOG.info(getName() + " Beginning constructor");
//        // More or less will be the same value
//        // Someone should tell me if it's otherwise
//        ANGLE_THRESHOLD = DriveTrainSettings.getRotAbsTolerance();
//
//        requires(Robot.claw);
//        LOG.info(getName() + " Constructed");
//    }
//
//    @Override
//    protected void initialize() {
//        updateHeading(Robot.camera.getAngleFromCube());
//        LOG.info(getName() + " Initialized");
//        super.initialize();
//    }
//
//    @Override
//    protected boolean isFinished() {
//        boolean isFinished = super.isFinished();
//
//        if (isFinished && Robot.camera.isRecent()) {
//            if (Math.abs(Robot.camera.getAngleFromCube()) > ANGLE_THRESHOLD) {
//                updateHeading(Robot.camera.getAngleFromCube());
//                isFinished = false;
//            }
//        }
//        return isFinished;
//    }
}
