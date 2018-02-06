package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1089.robot.commands.MoveOnPath;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class AutonCommand extends CommandGroup {
    public AutonCommand(AutonPosition ap, AutonTask[] at, ScoringSide[] ss) {
    	String data = DriverStation.getInstance().getGameSpecificMessage();
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.

        for(int i = 0; i < at.length; i++) {
            AutonTask currTask = at[i];
            AutonTask prevTask = at[i - 1];
            switch(currTask) {
                case GRAB_CUBE: //TODO we need da technician
                    if(i != 0 && prevTask != AutonTask.GRAB_CUBE) {
                        switch (prevTask) {
                            case SCORE_SCALE:
                                if(ap != AutonPosition.MIDDLE) {
                                    switch (ap) {
                                        case LEFT:
                                            //addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.BACKWARD));
                                            //TODO add ScaleFrontRight as a profile
                                        case RIGHT:
                                            addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.BACKWARD));
                                    }
                                }

                        }
                    }
            }
        }
    }
}
