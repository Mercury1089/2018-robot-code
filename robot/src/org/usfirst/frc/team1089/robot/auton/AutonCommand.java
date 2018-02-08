package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.commands.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class AutonCommand extends CommandGroup {
    public AutonCommand(AutonPosition autonPos, AutonTask[] autonTasks, ScoringSide[] scoreSide, InitialMiddleSwitchSide initMidSS) {
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

        switch(autonPos) {
            case LEFT:
                addSequential(new MoveOnPath("SwitchFrontLeft", MoveOnPath.Direction.FORWARD));
                addSequential(new MoveOnPath("CubePickupSetupLeft", MoveOnPath.Direction.FORWARD));
                break;
            case RIGHT:
                addSequential(new MoveOnPath("SwitchFrontRight", MoveOnPath.Direction.FORWARD));
                addSequential(new MoveOnPath("CubePickupSetupRight", MoveOnPath.Direction.FORWARD));
                break;
            case MIDDLE:
                switch(initMidSS) {
                    case LEFT_MID:
                        addSequential(new MoveOnPath("SwitchMidLeft", MoveOnPath.Direction.FORWARD));
                    case RIGHT_MID:
                        addSequential(new MoveOnPath("SwitchMidRight", MoveOnPath.Direction.FORWARD));
                }
                break;
        }

        for(int i = 1; i < autonTasks.length; i++) {
            AutonTask currTask = autonTasks[i];
            AutonTask prevTask = autonTasks[i - 1];
            if (scoreSide[i] == null)
                return;

            switch(currTask) {
                case GRAB_CUBE: //TODO we need da technician
                    if(prevTask != AutonTask.GRAB_CUBE) {
                        switch (prevTask) {
                            case SCORE_SCALE:
                                if(autonPos != AutonPosition.MIDDLE) {
                                    switch(scoreSide[i]) {
                                        case FRONT:
                                            switch(autonPos) {
                                                case LEFT:
                                                    addSequential(new MoveOnPath("ScaleFrontLeft", MoveOnPath.Direction.BACKWARD));
                                                    break;
                                                case RIGHT:
                                                    addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.BACKWARD));
                                                    break;
                                            }
                                            break;
                                        case MID: //TODO make SIDE return position
                                            switch(autonPos) {
                                                case LEFT:
                                                    //addSequential(new MoveOnPath("", MoveOnPath.Direction.BACKWARD));
                                                    break;
                                                case RIGHT:
                                                    //addSequential(new MoveOnPath("", MoveOnPath.Direction.BACKWARD));
                                                    break;
                                            }
                                            break;
                                    }
                                    //addSequential(new MoveOnPath(""));
                                    //TODO auto cube grab method. putting the following here if that doesnt happen:
                                    GetCube getCube = new GetCube();
                                    addSequential(getCube);
                                    addSequential(new UseClaw(Claw.ClawState.GRAB));
                                    double dist = getCube.getDistanceTraveled();
                                    addSequential(new DriveDistance(-dist, 0.5));
                                    double ang = getCube.getAngleTurned();
                                    addSequential(new RotateRelative(-ang));    //TODO use this and distance to make a profile to follow
                                }
                                break;
                            case SCORE_SWITCH:
                                switch(scoreSide[i]) { //TODO make sure scoreSide isn't null
                                    case FRONT:
                                        switch(autonPos) {
                                            case LEFT:
                                                addSequential(new MoveOnPath("ScaleFrontLeft", MoveOnPath.Direction.BACKWARD));
                                                break;
                                            case RIGHT:
                                                addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.BACKWARD));
                                                break;
                                        }
                                        break;
                                    case MID:
                                        switch(autonPos) {
                                            case LEFT:
                                                addSequential(new MoveOnPath("CubePickupSetupLeft", MoveOnPath.Direction.BACKWARD));
                                                break;
                                            case RIGHT:
                                                addSequential(new MoveOnPath("CubePickupSetupRight", MoveOnPath.Direction.BACKWARD));
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                    break;
                case SCORE_SCALE:
                    if(autonPos != AutonPosition.MIDDLE && scoreSide[i] != ScoringSide.BACK) {
                        switch(scoreSide[i]) {
                            case FRONT:
                                switch(autonPos) {
                                    case LEFT:
                                        addSequential(new MoveOnPath("ScaleFrontLeft", MoveOnPath.Direction.FORWARD));
                                        break;
                                    case RIGHT:
                                        addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.FORWARD));
                                        break;
                                }
                                break;
                            case MID: //TODO make SIDE profiles
                                switch(autonPos) {
                                    case LEFT:
                                        //addSequential(new MoveOnPath("", MoveOnPath.Direction.FORWARD));
                                        break;
                                    case RIGHT:
                                        //addSequential(new MoveOnPath("", MoveOnPath.Direction.FORWARD));
                                        break;
                                }
                                break;
                        }
                    }
                    addSequential(new UseClaw(Claw.ClawState.EJECT));
                    break;
                case SCORE_SWITCH:
                    if(autonPos != AutonPosition.MIDDLE && scoreSide[i] != ScoringSide.BACK) {
                        switch (scoreSide[i]) {
                            case BACK:
                                switch (autonPos) {
                                    case LEFT:
                                        addSequential(new MoveOnPath("SwitchBackLeft", MoveOnPath.Direction.FORWARD));
                                        break;
                                    case RIGHT:
                                        addSequential(new MoveOnPath("SwitchBackRight", MoveOnPath.Direction.FORWARD));
                                        break;
                                }
                                //TODO elevator stuff
                                break;
                            case MID: //The side placement positions are the initial pickup setups, but the other way.
                                switch (autonPos) {
                                    case LEFT:
                                        addSequential(new MoveOnPath("CubePickupSetupLeft", MoveOnPath.Direction.FORWARD));
                                        break;
                                    case RIGHT:
                                        addSequential(new MoveOnPath("CubePickupSetupRight", MoveOnPath.Direction.FORWARD));
                                        break;
                                }
                                //TODO elevator stuff
                                break;
                        }
                    }
                    addSequential(new UseClaw(Claw.ClawState.EJECT));
                    break;
            }
        }
    }
}
