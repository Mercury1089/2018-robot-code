package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.util.HistoryOriginator;

/**
 * Command group that calls both the AutoAlign command
 * and the DriveWithLIDAR command to autonomously target and
 * approach the cube.
 */
public class GetCube extends CommandGroup {
    private static Logger log = LogManager.getLogger(GetCube.class);
    private double angleTurned, distanceTraveled;
    private Command angleOriginator, distanceOriginator;
    public GetCube() {
        angleOriginator = new RotateToTarget();
        distanceOriginator = new DriveWithLIDAR(3.75, 0.3);

        log.info(getName() + " Beginning constructor");
        angleTurned = Robot.claw.getPixyCam().pidGet();
        addSequential(angleOriginator);
        addParallel(new UseClaw(Claw.ClawState.GRAB));
        addSequential(distanceOriginator);
        log.info(getName() + " Created");
    }

    public double getAngleTurned() {
        return angleTurned;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public HistoryOriginator getAngleOriginator() {
        return (HistoryOriginator) angleOriginator;
    }

    public HistoryOriginator getDistanceOriginator() {
        return (HistoryOriginator) distanceOriginator;
    }
}
