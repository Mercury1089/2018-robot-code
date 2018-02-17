package org.usfirst.frc.team1089.robot.commands;

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

    public GetCube() {
        RotateToTarget rtt = new RotateToTarget();
        RotateRelative rotate180 = new RotateRelative(180);
        DriveWithLIDAR dwl = new DriveWithLIDAR(8, 0.3);
        log.info(getName() + " Beginning constructor");
        angleTurned = Robot.vision.getPixyCam().pidGet();
        addSequential(rotate180);
        addSequential(rtt);
        distanceTraveled = Robot.claw.getLidar().getDistance();
        addParallel(new UseClaw(Claw.ClawState.GRAB));
        addSequential(dwl);
        addSequential(new DriveDistance(dwl, HistoryOriginator.HistoryTreatment.REVERSE, 0.3));
        addSequential(new RotateRelative(rtt, HistoryOriginator.HistoryTreatment.REVERSE));
        addSequential(new RotateRelative(rotate180, HistoryOriginator.HistoryTreatment.REVERSE));
        log.info(getName() + " Created");
        //TODO updated history code
    }

    public double getAngleTurned() {
        return angleTurned;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }
}
