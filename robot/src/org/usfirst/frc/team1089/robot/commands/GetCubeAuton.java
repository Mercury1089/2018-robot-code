package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.util.HistoryOriginator;

/**
 * GetCube autonomous routine.
 *
 * Should turn 180, get the cube, and return to its previous position.
 */
public class GetCubeAuton extends CommandGroup {
    private static Logger log = LogManager.getLogger(GetCubeAuton.class);

    public GetCubeAuton() {
        log.info(getName() + " Beginning constructor");
        GetCube getCubeCmd = new GetCube();
        addSequential(getCubeCmd);
        addSequential(new RotateRelative(getCubeCmd.getAngleOriginator(), HistoryOriginator.HistoryTreatment.REVERSE));
        log.info(getName() + " Created");
    }
}
