package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

public class AutoAlign extends RotateRelative {
    private final double ANGLE_THRESHOLD;

    public AutoAlign() {
        super();

        // More or less will be the same value
        // Someone should tell me if it's otherwise
        ANGLE_THRESHOLD = DriveTrainSettings.getRotAbsTolerance();

        requires(Robot.manipulator);
    }

    @Override
    protected void initialize() {
        updateHeading(Robot.camera.getAngleFromCube());

        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = super.isFinished();

        if (isFinished && Robot.camera.isRecent()) {
            if (Math.abs(Robot.camera.getAngleFromCube()) > ANGLE_THRESHOLD) {
                updateHeading(Robot.camera.getAngleFromCube());
                isFinished = false;
            }
        }
        return isFinished;
    }
}
