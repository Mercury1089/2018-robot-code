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
        addSequential(
                new AutonCommand(AutonPosition.RIGHT,
                new AutonTask[]{AutonTask.SCORE_SWITCH, AutonTask.GRAB_CUBE, AutonTask.SCORE_SCALE, AutonTask.GRAB_CUBE},
                new ScoringSide[]{ScoringSide.MID, null, ScoringSide.BACK, null},
                InitialMiddleSwitchSide.RIGHT_MID
                ));
    }
}
