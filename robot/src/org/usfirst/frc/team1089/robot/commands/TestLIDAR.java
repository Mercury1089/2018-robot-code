package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.LIDAR;

public class TestLIDAR extends Command {
    private LIDAR lidar;
    public TestLIDAR() {
        requires(Robot.manipulator);
    }

    protected void initialize() {
        lidar = Robot.manipulator.getLidar();
    }

    protected void execute() {
        //System.out.println("" + lidar.getDistance()[0] + " in.");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
