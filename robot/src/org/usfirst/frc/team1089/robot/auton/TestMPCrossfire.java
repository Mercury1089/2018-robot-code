package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class TestMPCrossfire extends CommandGroup {
    private static Logger log = LogManager.getLogger(TestMPCrossfire.class);

    public TestMPCrossfire() {
        addSequential(new MoveOnPath("SwitchMidRight", MoveOnPath.Direction.FORWARD));
        addSequential(new MoveOnPath("CubePickupSetupLeft", MoveOnPath.Direction.BACKWARD));    //TODO refactor trajectory names
//        addSequential(new MoveOnPath("GrabCubeDeadReckoning", MoveOnPath.Direction.FORWARD));   //cuz this should be RIGHT
//        addSequential(new MoveOnPath("GrabCubeDeadReckoning", MoveOnPath.Direction.BACKWARD));
        addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.FORWARD));

        addSequential(
                new AutonCommand(AutonPosition.RIGHT,
                new AutonTask[]{AutonTask.SCORE_SWITCH, AutonTask.GRAB_CUBE, AutonTask.SCORE_SCALE, AutonTask.GRAB_CUBE},
                new ScoringSide[]{ScoringSide.MID, null, ScoringSide.BACK, null},
                InitialMiddleSwitchSide.RIGHT_MID
                ));
    }
}
