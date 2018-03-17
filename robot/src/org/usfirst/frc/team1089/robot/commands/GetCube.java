package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.util.Recallable;

/**
 * Command group that calls both the AutoAlign command
 * and the DriveWithLIDAR command to autonomously target and
 * approach the cube.
 */
public class GetCube extends CommandGroup {
        private static Logger log = LogManager.getLogger(GetCube.class);
        private Command angleOriginator, distanceOriginator;

        public GetCube() {
            angleOriginator = new RotateToTarget();
            distanceOriginator = new DriveWithLIDAR(3.75, 0.3);

            log.info(getName() + " Beginning constructor");
            addSequential(angleOriginator);
            addParallel(new UseClaw(Claw.ClawState.GRAB));
            addSequential(distanceOriginator);
            log.info(getName() + " Created");
        }

    public Recallable<Double> getAngleOriginator() {
        return (Recallable<Double>) angleOriginator;
    }

    public Recallable<Double> getDistanceOriginator() {
        return (Recallable<Double>) distanceOriginator;
    }
}
