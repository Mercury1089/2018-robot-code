package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.auton.TaskConfig.*;
import org.usfirst.frc.team1089.robot.commands.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.GameData;
import org.usfirst.frc.team1089.util.Recallable;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 */
public class AutonCommand extends CommandGroup {
    private static Logger log = LogManager.getLogger(AutonCommand.class);
    private AutonPosition workingSide;
    //TODO may not need fieldSide
    private FieldSide fieldSide;
    private GameData.PlateSide comparableFieldSide;
    private GameData.PlateSide comparableWorkingSide; //Our Working Side, comparable to the side of the Plate
    private AutonTask[] autonTasks;
    private ScoringSide[] scoreSide;
    private String posStr;
    private int rotationFactor;
    private final double
            CUBE_PICKUP_X_OFFSET = 38.825,
            CUBE_PICKUP_Y_CONSTANT_OFFSET = 12.25,
            CUBE_PICKUP_Y_CHANGING_OFFSET = 28.1;

    public AutonCommand(AutonBuilder autonBuilder) {
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

        GameData.PlateSide
                scaleSide = GameData.getScaleSide(),
                switchSide = GameData.getSwitchSide();
        workingSide = autonBuilder.getAutonPos();
        switch (workingSide) {
            case LEFT:
                posStr = "Left";
                comparableWorkingSide = GameData.PlateSide.LEFT;
                rotationFactor = 1;
                break;
            case RIGHT:
                posStr = "Right";
                comparableWorkingSide = GameData.PlateSide.RIGHT;
                rotationFactor = -1;
                break;
            case LEFT_MID:
                posStr = "Left";
                comparableWorkingSide = GameData.PlateSide.UNKNOWN;
                rotationFactor = 0;
                break;
            case RIGHT_MID:
                posStr = "Right";
                comparableWorkingSide = GameData.PlateSide.UNKNOWN;
                rotationFactor = 0;
                break;
            default:
                rotationFactor = 0;
        }
        fieldSide = autonBuilder.getFieldSide();
        switch(fieldSide) {
            case LEFT_SIDE:
                comparableFieldSide = GameData.PlateSide.LEFT;
                break;
            case RIGHT_SIDE:
                comparableFieldSide = GameData.PlateSide.RIGHT;
                break;
            default:
                comparableFieldSide = null;
        }

        autonTasks = autonBuilder.getAutonTasks();
        scoreSide = autonBuilder.getScoreSide();

        log.info(getName() + ": constructing with starting position " + posStr +" and final field side " + comparableFieldSide.toString());

        if(autonTasks[0] == AutonTask.AUTO_LINE) {
            log.info(getName() + "Moving to AutoLine!");
            switch(workingSide) {
                case LEFT_MID:
                    addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                    addSequential(new MoveOnPath("SwitchFrontLeft", MoveOnPath.Direction.FORWARD));
                    break;
                case RIGHT_MID:
                    addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                    addSequential(new MoveOnPath("SwitchFrontRight", MoveOnPath.Direction.FORWARD));
                    break;
                default:
                    addSequential(new DriveDistance(168, 0.8));
            }
            log.info(getName() + "AutoLine complete!");
            return;
        }

        RotateRelative rotateRelative = null;//History RotateRelative that will be used to return to pickup position

        switch (workingSide) {
            case LEFT:
            case RIGHT:
                switch (autonTasks[0]) {
                    case SCORE_SWITCH:
                        addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                        log.info(getName() + ": Elevator set to switch height!");
                        if (switchSide == scaleSide) {
                            if (switchSide == comparableWorkingSide) {
                                addSequential(new MoveOnPath("SwitchMid" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                addSequential(new MoveOnPath("InitialCubeSetupPickup" + posStr, MoveOnPath.Direction.BACKWARD));  //TODO tune this
                                rotateRelative = new RotateRelative(getCubeTurnAngleScale(0, -rotationFactor, -90));
                                addSequential(rotateRelative);
                                log.info(getName() + ": SwitchMid, Eject, InitialCubeSetupPickup, RotateRelative constructed.");
                            } else {
                                addSequential(new DriveDistance(168, 1.0));
                                log.info("Unsafe to run profile, resorting to AutoLine!");
                                return;
                            }
                        } else {
                            if (switchSide == comparableWorkingSide) {
                                addSequential(new MoveOnPath("SwitchMid" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                addSequential(new MoveOnPath("InitialCubeSetupPickup" + posStr, MoveOnPath.Direction.BACKWARD));
                                rotateRelative = new RotateRelative(getCubeTurnAngleScale(0, -rotationFactor, -90));
                                addSequential(rotateRelative);
                                log.info(getName() + ": SwitchMid, Eject, InitialCubeSetupPickup, RotateRelative constructed.");
                                log.info("Don't want to go to scale side from position, aborting!");
                            } else {
                                addSequential(new DriveDistance(168, 1.0));
                                log.info("Unsafe to run profile, resorting to AutoLine!");
                            }
                            return;
                        }
                        break;
                case SCORE_SCALE:
                    addParallel(new UseElevator(Elevator.ElevatorPosition.SCALE_HIGH));
                    if (switchSide == comparableWorkingSide) {
                        addSequential(new MoveOnPath("InitialScaleFront" + posStr, MoveOnPath.Direction.FORWARD));
                        log.info(getName() + ": added Scale height parallel to InitialScaleFront. Set for cube drop (SCALE).");
                    } else {
                        addSequential(new MoveOnPath("InitialScaleFrontOpp" + posStr, MoveOnPath.Direction.FORWARD));
                        switchWorkingSide();
                        log.info(getName() + ": added Scale height parallel to InitialScaleFrontOpp. Set for cube drop (SCALE).");
                    }
                    addSequential(new UseClaw(Claw.ClawState.EJECT));
                    addParallel(new UseElevator(Elevator.ElevatorPosition.FLOOR));
                    addSequential(new DriveDistance(-43.5, .8));
                    rotateRelative = new RotateRelative(getCubeTurnAngleScale(0, rotationFactor, 90));
                    addSequential(rotateRelative);
                    log.info(getName() + ": Eject, Floor height (parallel), DriveDistance, RotateRelative constructed. Set for cube pickup.");
                }
                break;
            case LEFT_MID:
            case RIGHT_MID:
                addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                addSequential(new MoveOnPath("SwitchMid" + posStr, MoveOnPath.Direction.FORWARD));
                log.info(getName() + ": Switch height (parallel), SwitchMid constructed. Set for cube drop (SWITCH)!");
                break;
        }

        for (int i = 1; i < autonTasks.length; i++) {
            AutonTask taskToComplete = autonTasks[i];                      //The task to execute
            TaskConfig.ScoringSide sideToScoreOn = scoreSide[i];           //The side to score on
            TaskConfig.ScoringSide previousSide = scoreSide[i - 1];        //The side just scored on

            //GRAB CUBE
            if (previousSide != null) {
                if (i != 0) {
                    addSequential(new RotateRelative(rotateRelative, Recallable.RecallMethod.REVERSE));
                }
                addSequential(new GetCubeAuton());
            }

            log.info(getName() + "RotateRelative, GetCubeAuton constructed. Set for cube pickup.");

            switch (taskToComplete) {
                case SCORE_SCALE:
                    if (workingSide != AutonPosition.LEFT_MID && workingSide != AutonPosition.RIGHT_MID) {
                        addParallel(new UseElevator(Elevator.ElevatorPosition.SCALE_HIGH));
                        rotateRelative = new RotateRelative(getCubeTurnAngleScale(i, -rotationFactor, 90));
                        addSequential(rotateRelative);
                        addSequential(new DriveDistance(43.5, .8));
                        addSequential(new UseClaw(Claw.ClawState.EJECT));
                        addSequential(new DriveDistance(-43.5, .8));
                        break;
                    }
                    break;
                case SCORE_SWITCH:
                    if (workingSide != AutonPosition.LEFT_MID && workingSide != AutonPosition.RIGHT_MID) {
                        addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                        rotateRelative = new RotateRelative(getCubeTurnAngleSwitch(i, rotationFactor, -75));
                        addSequential(rotateRelative);
                        addSequential(new DriveDistance(30, .8));
                        addSequential(new UseClaw(Claw.ClawState.EJECT));
                        addSequential(new DriveDistance(-30, .8));
                    }
                    break;
            }
        }
    }

    private void switchWorkingSide() {
        switch (workingSide) {
            case RIGHT:
                workingSide = AutonPosition.LEFT;
                comparableWorkingSide = GameData.PlateSide.LEFT;
                posStr = workingSide.toString();
                rotationFactor = 1;
                break;
            case LEFT:
                workingSide = AutonPosition.RIGHT;
                comparableWorkingSide = GameData.PlateSide.RIGHT;
                posStr = workingSide.toString();
                rotationFactor = -1;
                break;
        }
    }

    public AutonCommand() {
        addSequential(new DriveDistance(168, 0.8));
    }

    private double getCubeTurnAngleScale(int cubesPickedUp, int rotationFactor, int addDeg) {
        return rotationFactor * (addDeg + Math.toDegrees(Math.atan(CUBE_PICKUP_X_OFFSET/(CUBE_PICKUP_Y_CHANGING_OFFSET * cubesPickedUp + CUBE_PICKUP_Y_CONSTANT_OFFSET))));
    }

    private double getCubeTurnAngleSwitch(int cubesPickedUp, int rotationFactor, int addDeg) {
        return rotationFactor * (addDeg - Math.toDegrees(Math.atan(CUBE_PICKUP_X_OFFSET/(CUBE_PICKUP_Y_CHANGING_OFFSET * cubesPickedUp + CUBE_PICKUP_Y_CONSTANT_OFFSET))));
    }
}
