package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.util.GameData;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class AutonCommand extends CommandGroup {
    private static Logger log = LogManager.getLogger(AutonCommand.class);
    private AutonPosition workingSide;
    private InitialMiddleSwitchSide initialMidSwitchSide;
    private AutonTask[] autonTasks;
    private ScoringSide[] scoreSide;
    private String posStr;

    public AutonCommand(AutonBuilder autonBuilder) {
        String data = DriverStation.getInstance().getGameSpecificMessage();
        GameData gameData = GameData.getInstance();
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

        workingSide = autonBuilder.getAutonPos();
        GameData.PlateSide comparableWorkingSide; //Our Working Side, comparable to the side of the Plate
        switch (workingSide.toString().charAt(0)) {
            case 'L':
                comparableWorkingSide = GameData.PlateSide.LEFT;
                break;
            case 'R':
                comparableWorkingSide = GameData.PlateSide.RIGHT;
                break;
            default:
                comparableWorkingSide = GameData.PlateSide.UNKNOWN;
        }
        initialMidSwitchSide = autonBuilder.getInitMidSS();
        autonTasks = autonBuilder.getAutonTasks();
        scoreSide = autonBuilder.getScoreSide();
        posStr = workingSide.toString();
        int cubesPickedUp = 0;      //Number of cubes picked up from the working side after the robot has scored in switch

        switch (workingSide) {
            case LEFT:
            case RIGHT:
                if (gameData.getSwitchSide() == gameData.getScaleSide()) {
                    if (gameData.getSwitchSide() == comparableWorkingSide) {
                        addSequential(new MoveOnPath("InitialSwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                        addSequential(new MoveOnPath("SwitchBack" + posStr, MoveOnPath.Direction.BACKWARD));
                    } else {
                        addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                        switchWorkingSide();
                        addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                    }
                } else {
                    if (gameData.getSwitchSide() == comparableWorkingSide) {
                        addSequential(new MoveOnPath("InitialSwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                        switchWorkingSide();
                        addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                    } else {
                        addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                        addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                    }
                }
                addSequential(new DriveDistance(43.5, .5));
                break;
            case MIDDLE:
                switch (initialMidSwitchSide) {
                    case LEFT_MID:
                    case RIGHT_MID:
                        String initMidStr = initialMidSwitchSide.toString();
                        addSequential(new MoveOnPath("SwitchMid" + initMidStr.substring(0, initMidStr.indexOf("_")),
                                MoveOnPath.Direction.FORWARD));
                        break;
                }
                break;
        }

        for (int i = 1; i < autonTasks.length; i++) {
            AutonTask taskToComplete = autonTasks[i];           //The task to execute
            AutonTask previousTask = autonTasks[i - 1];         //The task just executed
            ScoringSide sideToScoreOn = scoreSide[i];           //The side to score on
            ScoringSide previousSide = scoreSide[i - 1];        //The side just scored on

            switch (taskToComplete) {
                case GRAB_CUBE:
                    if (previousTask != AutonTask.GRAB_CUBE && previousSide != null) {
                        //addSequential(new MoveOnPath(""));
                        //TODO auto cube grab method. putting the following here if that doesn't happen:
                        //TODO if not going to change, then add a drive distance so that it doesn't turn into a wall.
                        /*GetCube getCube = new GetCube();
                        addSequential(getCube);
                        addSequential(new UseClaw(Claw.ClawState.GRAB));
                        double dist = getCube.getDistanceTraveled();
                        addSequential(new DriveDistance(-dist, 0.5));
                        double ang = getCube.getAngleTurned();
                        addSequential(new RotateRelative(-ang));*/    //TODO use this and distance to make a profile to follow

                        //TODO double check these values and document (symbolic constants)
                        if (cubesPickedUp == 0) {
                            addSequential(new RotateRelative(Math.atan(38.825/12.25)));
                        } else if (cubesPickedUp > 0) {
                            addSequential(new RotateRelative(90 + Math.atan(38.825/(28.1*cubesPickedUp+12.25))));
                        }
                        cubesPickedUp++;
                    }
                    break;
                case SCORE_SCALE:
                    if (workingSide != AutonPosition.MIDDLE && sideToScoreOn != ScoringSide.BACK) {
                        switch (sideToScoreOn) {
                            case FRONT:
                                addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.BACKWARD));
                                break;
                            case MID:
                                addSequential(new MoveOnPath("ScaleSide" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new MoveOnPath("ScaleSide" + posStr, MoveOnPath.Direction.BACKWARD));
                                break;
                        }
                    }
                    addSequential(new UseClaw(Claw.ClawState.EJECT));
                    break;
                case SCORE_SWITCH:
                    if (workingSide != AutonPosition.MIDDLE && sideToScoreOn != ScoringSide.FRONT) {
                        switch (sideToScoreOn) {
                            case BACK:
                                if (gameData.getSwitchSide() == gameData.getScaleSide()) {
                                    addSequential(new MoveOnPath("SwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                                    addSequential(new MoveOnPath("SwitchBack" + posStr, MoveOnPath.Direction.BACKWARD));
                                } else {
                                    addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                                    addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                                }
                                //TODO elevator stuff
                                break;
                            case MID: //The side placement positions are the initial pickup setups, but the other way.
                                if (gameData.getSwitchSide() == gameData.getScaleSide()) {
                                    addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.FORWARD));
                                    addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.BACKWARD));
                                } else {
                                    //TODO possibly make OppMid delivery thing
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

    private void switchWorkingSide() {
        switch (workingSide) {
            case RIGHT:
                workingSide = AutonPosition.LEFT;
                posStr = workingSide.toString();
                break;
            case LEFT:
                workingSide = AutonPosition.RIGHT;
                posStr = workingSide.toString();
                break;
        }
    }
}
