package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;

public class AutoAlign extends RotateRelative {
    @Override
    protected void initialize() {
        requires(Robot.driveTrain);
        requires(Robot.manipulator);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
