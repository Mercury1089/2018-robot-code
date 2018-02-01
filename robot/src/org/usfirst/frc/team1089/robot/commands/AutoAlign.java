package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;

public class AutoAlign extends RotateRelative {
    private final int ANGLE_THRESHOLD = 1;
    public AutoAlign() {
        super();

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
