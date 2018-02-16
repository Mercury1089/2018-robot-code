package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Claw;

/**
 * Command group that calls both the AutoAlign command
 * and the DriveWithLIDAR command to autonomously target and
 * approach the cube.
 */
public class GetCube extends CommandGroup {
    private static Logger log = LogManager.getLogger(GetCube.class);
    private double angleTurned, distanceTraveled;

    public GetCube() {
        log.info(getName() + " Beginning constructor");
        angleTurned = Robot.vision.getPixyCam().pidGet();
        addSequential(new RotateToTarget());
        distanceTraveled = Robot.claw.getLidar().getDistance();
        addParallel(new UseClaw(Claw.ClawState.GRAB));
        addSequential(new DriveWithLIDAR(8, .3));
        log.info(getName() + " Constructed");
        //TODO updated history code
    }

    public double getAngleTurned() {
        return angleTurned;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }
}
