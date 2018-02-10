package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.sensors.LIDAR;

public class TestLIDAR extends Command {
    private LIDAR lidar;
    private Logger log = LogManager.getLogger(TestLIDAR.class);
    public TestLIDAR() {
        requires(Robot.manipulator);
    }

    protected void initialize() {
        lidar = Robot.manipulator.getLidar();
    }

    protected void execute() {
        //log.info(getName() + " Executing");
        //System.out.println("" + lidar.getDistance()[0] + " in.");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
