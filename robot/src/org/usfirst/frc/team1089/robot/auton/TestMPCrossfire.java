package org.usfirst.frc.team1089.robot.auton;

import org.usfirst.frc.team1089.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1089.robot.commands.MoveOnPath;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class TestMPCrossfire extends CommandGroup {


    public TestMPCrossfire() {
        addSequential(
                new AutonCommand(AutonPosition.RIGHT,
                new AutonTask[]{AutonTask.SCORE_SWITCH, AutonTask.GRAB_CUBE, AutonTask.SCORE_SCALE, AutonTask.GRAB_CUBE},
                new ScoringSide[]{ScoringSide.MID, null, ScoringSide.BACK, null},
                InitialMiddleSwitchSide.RIGHT_MID
                ));
    }
}
