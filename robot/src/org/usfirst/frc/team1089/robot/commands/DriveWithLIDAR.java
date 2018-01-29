package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.LIDAR;

public class DriveWithLIDAR extends Command {
    public double currDist, percVoltage;
    public DriveWithLIDAR(double percentVoltage) {
        currDist = Robot.manipulator.getLidar().getPaddedDistance();
        percVoltage = percentVoltage;
    }
    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void execute() {
        super.execute();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        super.end();
    }
}
