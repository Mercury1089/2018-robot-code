package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.RelativeTimePatternConverter;
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
    private GameData.PlateSide comparableWorkingSide; //Our Working Side, comparable to the side of the Plate
    private AutonTask[] autonTasks;
    private ScoringSide[] scoreSide;
    private String posStr;
    private int rotationFactor;
    private final double
            CUBE_PICKUP_X_OFFSET = 38.825,
            CUBE_PICKUP_Y_CONSTANT_OFFSET = 12.25,
            CUBE_PICKUP_Y_CHANGING_OFFSET = 28.1,
            SCALE_OFFSET = 43.5,
            SWITCH_OFFSET = 30,
            AUTO_LINE = 168;

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
        posStr = workingSide.toString();
        switch (workingSide) {
            case LEFT:
                comparableWorkingSide = GameData.PlateSide.LEFT;
                rotationFactor = 1;
                break;
            case RIGHT:
                comparableWorkingSide = GameData.PlateSide.RIGHT;
                rotationFactor = -1;
                break;
            case MID:
                if (switchSide == GameData.PlateSide.LEFT)
                    posStr = "Left";
                else
                    posStr = "Right";
                comparableWorkingSide = GameData.PlateSide.UNKNOWN;
            default:
                rotationFactor = 0;
        }

        autonTasks = autonBuilder.getAutonTasks();
        scoreSide = autonBuilder.getScoreSide();

        log.info(getName() + ": constructing with starting position " + posStr + ", rotation factor " + rotationFactor);

        if (autonTasks[0] == AutonTask.AUTO_LINE) {
            log.info(getName() + "Moving to AutoLine!");
            switch (workingSide) {
                case MID:
                    addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                    addSequential(new MoveOnPath("SwitchFront" + posStr, MoveOnPath.Direction.FORWARD));
                    break;
                default:
                    addSequential(new DriveDistance(AUTO_LINE, 1.0));
            }
            log.info(getName() + "AutoLine complete!");
            return;
        }

        DegreeRotate degreeRotate = null;//History DegreeRotate that will be used to return to pickup position

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
                                if (autonTasks.length != 1) {
                                    addSequential(new MoveOnPath("InitialCubeSetupPickup" + posStr, MoveOnPath.Direction.BACKWARD));  //TODO tune this
                                    degreeRotate = new DegreeRotate(getCubeTurnAngleScale(0, -rotationFactor, -90), DegreeRotate.RotationType.RELATIVE);
                                    addSequential(degreeRotate);
                                    log.info(getName() + ": SwitchMid, Eject, InitialCubeSetupPickup, DegreeRotate constructed.");
                                }
                            } else {
                                addSequential(new DriveDistance(AUTO_LINE, 1.0));
                                log.info("Unsafe to run profile, resorting to AutoLine!");
                                return;
                            }
                        } else {
                            if (switchSide == comparableWorkingSide) {
                                addSequential(new MoveOnPath("SwitchMid" + posStr, MoveOnPath.Direction.FORWARD));
                                addSequential(new UseClaw(Claw.ClawState.EJECT));
                                //addSequential(new MoveOnPath("InitialCubeSetupPickup" + posStr, MoveOnPath.Direction.BACKWARD));
                                degreeRotate = new DegreeRotate(getCubeTurnAngleScale(0, -rotationFactor, -90), DegreeRotate.RotationType.RELATIVE);
                                //addSequential(degreeRotate);
                                log.info(getName() + ": SwitchMid, Eject, InitialCubeSetupPickup, DegreeRotate constructed.");
                                log.info("Don't want to go to scale side from position, aborting!");
                            } else {
                                addSequential(new DriveDistance(AUTO_LINE, 1.0));
                                log.info("Unsafe to run profile, resorting to AutoLine!");
                            }
                            return;
                        }
                        break;
                    case SCORE_SCALE:
                        if (scaleSide == comparableWorkingSide) {
                            addParallel(new UseElevator(Elevator.ElevatorPosition.SCALE_HIGH));
                            addSequential(new MoveOnPath("InitialScaleFront" + posStr, MoveOnPath.Direction.FORWARD));
                            log.info(getName() + ": added Scale height parallel to InitialScaleFront. Set for cube drop (SCALE).");
                        } else {
                            addParallel(new DelayableElevator(3.5, Elevator.ElevatorPosition.SCALE_HIGH, true));
                            addSequential(new MoveOnPath("InitialScaleFrontOpp" + posStr, MoveOnPath.Direction.FORWARD));
                            boolean continueAuto = switchWorkingSide();
                            if (!continueAuto) {
                                log.info(getName() + ".switchWorkingSide() may throw errors, aborting!");
                                return;
                            }
                            addSequential(new WaitCommand(1.5));
                            addSequential(new DegreeRotate(70, DegreeRotate.RotationType.RELATIVE));
                            addSequential(new DriveDistance(20, 1.0));
                            log.info(getName() + ": added Scale height parallel to InitialScaleFrontOpp. Set for cube drop (SCALE).");
                        }
                        addSequential(new UseClaw(Claw.ClawState.EJECT));
                        addParallel(new DelayableElevator(0.7, Elevator.ElevatorPosition.FLOOR, false));
                        addSequential(new DriveDistance(-SCALE_OFFSET, 1.0));

                        degreeRotate = new DegreeRotate(getCubeTurnAngleScale(0, rotationFactor, 90), DegreeRotate.RotationType.RELATIVE,  1.5);
                        addSequential(degreeRotate);
                        log.info(getName() + ": Eject, Floor height (parallel), DriveDistance, DegreeRotate constructed. Set for cube pickup.");
                }
                break;
            case MID:
                addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                addSequential(new MoveOnPath("SwitchFront" + posStr, MoveOnPath.Direction.FORWARD));
                addSequential(new UseClaw(Claw.ClawState.EJECT));
                log.info(getName() + ": Switch height (parallel), SwitchFront constructed. Set for cube drop (SWITCH)!");
                break;
        }

        for (int i = 1; i < autonTasks.length; i++) {
            AutonTask taskToComplete = autonTasks[i];                      //The task to execute
            TaskConfig.ScoringSide previousSide = scoreSide[i - 1];        //The side just scored on

            //GRAB CUBE
            if (previousSide != null) {
                if (i != 1) {
                    if (degreeRotate == null) {
                        log.info(getName() + ": Unsafe to run next Rotate, aborting!");
                        return;
                    }
                    addSequential(new DegreeRotate(degreeRotate, Recallable.RecallMethod.REVERSE));
                }
                addSequential(new GetCubeAuton());
            }

            log.info(getName() + ": DegreeRotate, GetCubeAuton constructed. Set for cube pickup.");

            switch (taskToComplete) {
                case SCORE_SCALE:
                    if (workingSide != AutonPosition.MID) {
                        addParallel(new UseElevator(Elevator.ElevatorPosition.SCALE_HIGH));
                        degreeRotate = new DegreeRotate(getCubeTurnAngleScale(i, -rotationFactor, 90), DegreeRotate.RotationType.RELATIVE);
                        addSequential(degreeRotate);
                        addSequential(new DriveDistance(SCALE_OFFSET, 1.0));
                        addSequential(new UseClaw(Claw.ClawState.EJECT));
                        addParallel(new DelayableElevator(0.7, Elevator.ElevatorPosition.FLOOR, false));
                        addSequential(new DriveDistance(-SCALE_OFFSET, 1.0));
                        log.info(getName() + ": Dropping cube number " + i + " into Scale constructed.");
                    }
                    break;
                case SCORE_SWITCH:
                    if (workingSide != AutonPosition.MID) {
                        addParallel(new UseElevator(Elevator.ElevatorPosition.SWITCH));
                        addSequential(new DriveDistance(SWITCH_OFFSET, 1.0));
                        addSequential(new UseClaw(Claw.ClawState.EJECT));
                        addParallel(new DelayableElevator(0.7, Elevator.ElevatorPosition.FLOOR, false));
                        addSequential(new DriveDistance(-SWITCH_OFFSET, .8));
                        log.info(getName() + ": Dropping cube number " + i + " into Switch constructed.");
                    }
                    break;
            }
        }
    }

    private boolean switchWorkingSide() {
        switch (workingSide) {
            case RIGHT:
                workingSide = AutonPosition.LEFT;
                comparableWorkingSide = GameData.PlateSide.LEFT;
                posStr = workingSide.toString();
                rotationFactor = 1;
                return true;
            case LEFT:
                workingSide = AutonPosition.RIGHT;
                comparableWorkingSide = GameData.PlateSide.RIGHT;
                posStr = workingSide.toString();
                rotationFactor = -1;
                return true;
            default:
                return false;
        }
    }

    public AutonCommand() {
        log.info("Constructing DriveDistance only AutonCommand, likely another kUnassigned exception.");
        addSequential(new DriveDistance(168, 0.8));
    }

    private double getCubeTurnAngleScale(int cubesPickedUp, int rotationFactor, int addDeg) {
        return rotationFactor * (addDeg + Math.toDegrees(Math.atan(CUBE_PICKUP_X_OFFSET / (CUBE_PICKUP_Y_CHANGING_OFFSET * cubesPickedUp + CUBE_PICKUP_Y_CONSTANT_OFFSET))));
    }

    private double getCubeTurnAngleSwitch(int cubesPickedUp, int rotationFactor, int addDeg) {
        return rotationFactor * (addDeg - Math.toDegrees(Math.atan(CUBE_PICKUP_X_OFFSET / (CUBE_PICKUP_Y_CHANGING_OFFSET * cubesPickedUp + CUBE_PICKUP_Y_CONSTANT_OFFSET))));
    }
}
