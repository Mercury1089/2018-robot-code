package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;

/**
 * Command group that calls both the AutoAlign command
 * and the DriveWithLIDAR command to autonomously target and
 * approach the cube.
 */
public class GetCube extends CommandGroup {
    private static Logger log = LogManager.getLogger(GetCube.class);
    private double angleTurned, distanceTraveled;
    public GetCube() {
        angleTurned = Robot.vision.getPixyCam().getDisplacement();
        addSequential(new RotateToTarget());
        distanceTraveled = Robot.manipulator.getLidar().getDistance();
        addSequential(new DriveWithLIDAR(5, .3));
    }
    public double getAngleTurned() {
        return angleTurned;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }
}
