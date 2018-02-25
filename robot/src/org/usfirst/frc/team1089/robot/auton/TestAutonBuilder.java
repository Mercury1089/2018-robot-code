package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.GetCubeAuton;
import org.usfirst.frc.team1089.robot.commands.MoveOnPath;
import org.usfirst.frc.team1089.robot.commands.UseClaw;
import org.usfirst.frc.team1089.robot.subsystems.Claw;

/**
 * Test command to run {@link AutonBuilder} trials
 */
public class TestAutonBuilder extends CommandGroup {
    private static Logger log = LogManager.getLogger(TestAutonBuilder.class);

    public TestAutonBuilder() {
        //TODO make sure to setClawState up cubes and test this in a wide open space
        //TODO also make sure to input RRR in the driver station
        /*addSequential(new AutonCommand(new AutonBuilder(
                AutonPosition.RIGHT,
                new AutonTask[]{AutonTask.SCORE_SWITCH, AutonTask.GRAB_CUBE, AutonTask.SCORE_SCALE, AutonTask.GRAB_CUBE, AutonTask.SCORE_SCALE},
                new ScoringSide[]{ScoringSide.MID, null, ScoringSide.FRONT, null, ScoringSide.FRONT},
                null
        )));*/

        addSequential(new MoveOnPath("SwitchMidRight", MoveOnPath.Direction.FORWARD));
        addSequential(new UseClaw(Claw.ClawState.EJECT));
        addSequential(new MoveOnPath("CubePickupSetupRight", MoveOnPath.Direction.BACKWARD));
        addParallel(new UseClaw(Claw.ClawState.GRAB));
        addSequential(new GetCubeAuton());
    }
}
