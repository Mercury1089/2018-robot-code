package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.GameData;
import org.usfirst.frc.team1089.util.HistoryOriginator;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class AutonCommand extends CommandGroup {
    private static Logger log = LogManager.getLogger(AutonCommand.class);
    private AutonPosition workingSide;
    private AutonTask[] autonTasks;
    private ScoringSide[] scoreSide;
    private String posStr;
    private final double
            CUBE_PICKUP_X_OFFSET = 38.825,
            CUBE_PICKUP_Y_CONSTANT_OFFSET = 12.25,
            CUBE_PICKUP_Y_CHANGING_OFFSET = 28.1;

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

        int cubesPickedUp = 0, rotationFactor;
        ;      //Number of cubes picked up
        workingSide = autonBuilder.getAutonPos();
        GameData.PlateSide comparableWorkingSide; //Our Working Side, comparable to the side of the Plate
        switch (workingSide) {
            case LEFT:
                comparableWorkingSide = GameData.PlateSide.LEFT;
                rotationFactor = 1;
                break;
            case RIGHT:
                comparableWorkingSide = GameData.PlateSide.RIGHT;
                rotationFactor = -1;
                break;
            case LEFT_MID:
            case RIGHT_MID:
            default:
                comparableWorkingSide = GameData.PlateSide.UNKNOWN;
                rotationFactor = 0;
        }
        autonTasks = autonBuilder.getAutonTasks();
        scoreSide = autonBuilder.getScoreSide();
        posStr = workingSide.toString();

        RotateRelative rotateRelative = null;      //History RotateRelative that will be used to return to pickup position

        switch (workingSide) {
            case LEFT:
            case RIGHT:
                switch (autonTasks[0]) {
                    case SCORE_SWITCH:
                        addParallel(new UseElevator(Elevator.ELEVATOR_STATE.SWITCH));
                        if (gameData.getSwitchSide() == gameData.getScaleSide()) {
                            //TODO make a path that replaces the "CubeSetupPickup" and "SwitchBack" and the following DriveDistance
                            if (gameData.getSwitchSide() == comparableWorkingSide) {
                                addSequential(new MoveOnPath("InitialSwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                addSequential(new MoveOnPath("SwitchBack" + posStr, MoveOnPath.Direction.BACKWARD));
                            } else {
                                addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                switchWorkingSide();
                                addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                            }
                        } else {
                            if (gameData.getSwitchSide() == comparableWorkingSide) {
                                addSequential(new MoveOnPath("InitialSwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                switchWorkingSide();
                                addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                            } else {
                                addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.BACKWARD));
                            }
                        }
                        addSequential(new DriveDistance(43.5, .5));
                        rotateRelative = new RotateRelative(getCubeTurnAngleScale(0, rotationFactor, 0));
                        addSequential(rotateRelative);
                        break;
                    case SCORE_SCALE:
                        addParallel(new UseElevator(Elevator.ELEVATOR_STATE.SCALE_HIGH));
                        if (gameData.getSwitchSide() == comparableWorkingSide) {
                            addSequential(new MoveOnPath("InitialScaleFront" + posStr, MoveOnPath.Direction.FORWARD));
                        } else {
                            addSequential(new MoveOnPath("InitialScaleFrontOpp" + posStr, MoveOnPath.Direction.FORWARD));
                            switchWorkingSide();
                        }
                        addSequential(new UseClaw(Claw.ClawState.EJECT));
                        addSequential(new DriveDistance(-51.825, .5));
                        rotateRelative = new RotateRelative(getCubeTurnAngleScale(0, rotationFactor, 90));
                        addSequential(rotateRelative);
                }
                break;
            case LEFT_MID:
            case RIGHT_MID:
                addSequential(new MoveOnPath("SwitchMid" + autonTasks.toString().substring(0, autonTasks.toString().indexOf("_")),
                        MoveOnPath.Direction.FORWARD));
                break;

        }

        //TODO check all RotateRelative angle signs.
        for (int i = 1; i < autonTasks.length; i++) {
            AutonTask taskToComplete = autonTasks[i];           //The task to execute
            AutonTask previousTask = autonTasks[i - 1];         //The task just executed
            ScoringSide sideToScoreOn = scoreSide[i];           //The side to score on
            ScoringSide previousSide = scoreSide[i - 1];        //The side just scored on

            //GRAB CUBE
            if (previousSide != null) {
                if (i != 0) {
                    addSequential(new RotateRelative(rotateRelative, HistoryOriginator.HistoryTreatment.REVERSE));
                } else {
                    addSequential(new RotateRelative(getCubeTurnAngleScale(0, rotationFactor, 0)));
                }
                addSequential(new GetCube());
            }

            switch (taskToComplete) {
                case SCORE_SCALE:
                    if ((workingSide != AutonPosition.LEFT_MID && workingSide != AutonPosition.RIGHT_MID) && sideToScoreOn != ScoringSide.BACK) {
                        switch (sideToScoreOn) {
                            case FRONT:
                                //addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.FORWARD));
                                //addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.BACKWARD));
                                rotateRelative = new RotateRelative(getCubeTurnAngleScale(i, rotationFactor, 90));
                                addSequential(rotateRelative);
                                addSequential(new DriveDistance(51.825, .5));
                                addSequential(new DriveDistance(-51.825, .5));
                                break;
                            case MID:       //Will probably never use this...
                                rotateRelative = new RotateRelative(getCubeTurnAngleScale(i, rotationFactor, 0));
                                addSequential(rotateRelative);
                                addSequential(new DriveDistance(-43.5, .5));
                                addSequential(new MoveOnPath("ScaleSide" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new MoveOnPath("ScaleSide" + posStr, MoveOnPath.Direction.BACKWARD));
                                addSequential(new DriveDistance(43.5, .5));
                                break;
                        }
                    }
                    addSequential(new UseClaw(Claw.ClawState.EJECT));
                    break;
                case SCORE_SWITCH:
                    if ((workingSide != AutonPosition.LEFT_MID && workingSide != AutonPosition.RIGHT_MID) && sideToScoreOn != ScoringSide.FRONT) {
                        addSequential(new RotateRelative());
                        switch (sideToScoreOn) {
                            case BACK:
                                if (gameData.getSwitchSide() == gameData.getScaleSide()) {
                                    rotateRelative = new RotateRelative(getCubeTurnAngleSwitch(i, rotationFactor, 0));
                                    addSequential(new DriveDistance(15, .5));
                                    addSequential(new DriveDistance(-15, .5));  //TODO change these arbitrary values
                                }
                                //TODO elevator stuff
                                break;
                            case MID: //The side placement positions are the initial pickup setups, but the other way.
                                if (gameData.getSwitchSide() == gameData.getScaleSide()) {
                                    addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.FORWARD));
                                    addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.BACKWARD));
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

    private double getCubeTurnAngleScale(int cubesPickedUp, int rotationFactor, int addDeg) {
        return rotationFactor * (addDeg + Math.toDegrees(Math.atan(CUBE_PICKUP_X_OFFSET / (CUBE_PICKUP_Y_CHANGING_OFFSET * cubesPickedUp + CUBE_PICKUP_Y_CONSTANT_OFFSET))));
    }

    private double getCubeTurnAngleSwitch(int cubesPickedUp, int rotationFactor, int addDeg) {
        return rotationFactor * (addDeg - Math.toDegrees(Math.atan(CUBE_PICKUP_X_OFFSET / (CUBE_PICKUP_Y_CHANGING_OFFSET * cubesPickedUp + CUBE_PICKUP_Y_CONSTANT_OFFSET))));
    }

    /*private DriveDistance generateDriveAndStore(double dist, double pVolt) {
        historyDriveDistance = new DriveDistance(dist, pVolt);
        return historyDriveDistance;
    }

    private RotateRelative generateRotationAndStore(double ang) {
        historyRotateRelative = new RotateRelative(ang);
        return historyRotateRelative;
    }

    private DriveDistance getHistoryDriveDistanceReverse(double pVolt) {
        return new DriveDistance(historyDriveDistance, HistoryOriginator.HistoryTreatment.REVERSE, pVolt);
    }

    private RotateRelative getHistoryRotateRelativeReverse() {
        return new RotateRelative(historyRotateRelative, HistoryOriginator.HistoryTreatment.REVERSE);
    }*/
}
