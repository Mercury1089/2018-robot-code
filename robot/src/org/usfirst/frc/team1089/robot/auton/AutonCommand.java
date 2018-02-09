package org.usfirst.frc.team1089.robot.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;

/**
 * Command group that specifies the commands to be run
 * during the autonomous period.
 *
 */
public class AutonCommand extends CommandGroup {
    private static Logger log = LogManager.getLogger(AutonCommand.class);
    private AutonPosition workingSide;
    private InitialMiddleSwitchSide initMidSS;
    private AutonTask[] autonTasks;
    private ScoringSide[] scoreSide;
    private String posStr;

    public AutonCommand(AutonBuilder autonBuilder) {
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

        workingSide = autonBuilder.getAutonPos();
        initMidSS = autonBuilder.getInitMidSS();
        autonTasks = autonBuilder.getAutonTasks();
        scoreSide = autonBuilder.getScoreSide();
        posStr = workingSide.toString();

        switch(workingSide) {
            case LEFT:
            case RIGHT:
                if (data.charAt(0) == data.charAt(1)) {
                    if(data.charAt(0) == workingSide.toString().charAt(0)) {
                        addSequential(new MoveOnPath("InitialSwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                        addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.FORWARD));
                    } else {
                        addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                        switchWorkingSide();
                        addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.FORWARD));
                    }
                } else {
                    if(data.charAt(0) == workingSide.toString().charAt(0)) {
                        addSequential(new MoveOnPath("InitialSwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                        switchWorkingSide();
                        addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.FORWARD));
                    } else {
                        addSequential(new MoveOnPath("SwitchBackOpp" + posStr, MoveOnPath.Direction.FORWARD));
                        addSequential(new MoveOnPath("CubeSetupPickupOpp" + posStr, MoveOnPath.Direction.FORWARD));
                    }
                }
                break;
            case MIDDLE:
                switch(initMidSS) {
                    case LEFT_MID:
                    case RIGHT_MID:
                        String initMidStr = initMidSS.toString();
                        addSequential(new MoveOnPath("SwitchMid" + initMidStr.substring(0, initMidStr.indexOf("_")),
                                MoveOnPath.Direction.FORWARD));
                        break;
                }
                break;
        }

        for(int i = 1; i < autonTasks.length; i++) {
            AutonTask currTask = autonTasks[i];
            AutonTask prevTask = autonTasks[i - 1];

            switch(currTask) {
                case GRAB_CUBE: //TODO we need da technician
                    if(prevTask != AutonTask.GRAB_CUBE && scoreSide[i - 1] != null) {
                        switch (prevTask) {
                            case SCORE_SCALE:
                                //scoreSide[i - 1] because it's looking for where it JUST scored.
                                if(workingSide != AutonPosition.MIDDLE && scoreSide[i - 1] != ScoringSide.BACK) {
                                    switch(scoreSide[i - 1]) {
                                        case FRONT:
                                            addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.BACKWARD));
                                            break;
                                        case MID: //TODO make SIDE return position
                                            addSequential(new MoveOnPath("ScaleSide" + posStr, MoveOnPath.Direction.BACKWARD));
                                            break;
                                    }
                                }
                                break;
                            case SCORE_SWITCH:
                                switch(scoreSide[i]) { //TODO make sure scoreSide isn't null
                                    case FRONT:
                                        addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.BACKWARD));
                                        break;
                                    case MID:
                                        addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.BACKWARD));
                                        break;
                                }
                                break;
                        }
                        //addSequential(new MoveOnPath(""));
                        //TODO auto cube grab method. putting the following here if that doesn't happen:
                        //TODO if not going to change, then add a drive distance so that it doesn't turn into a wall.
                        GetCube getCube = new GetCube();
                        addSequential(getCube);
                        addSequential(new UseClaw(Claw.ClawState.GRAB));
                        double dist = getCube.getDistanceTraveled();
                        addSequential(new DriveDistance(-dist, 0.5));
                        double ang = getCube.getAngleTurned();
                        addSequential(new RotateRelative(-ang));    //TODO use this and distance to make a profile to follow
                    }
                    break;
                case SCORE_SCALE:
                    if(workingSide != AutonPosition.MIDDLE && scoreSide[i] != ScoringSide.BACK) {
                        switch(scoreSide[i]) {
                            case FRONT:
                                addSequential(new MoveOnPath("ScaleFront" + posStr, MoveOnPath.Direction.FORWARD));
                                break;
                            case MID: //TODO make SIDE profiles
                                addSequential(new MoveOnPath("ScaleSide" + posStr, MoveOnPath.Direction.FORWARD));
                                break;
                        }
                    }
                    addSequential(new UseClaw(Claw.ClawState.EJECT));
                    break;
                case SCORE_SWITCH:
                    if(workingSide != AutonPosition.MIDDLE && scoreSide[i] != ScoringSide.FRONT) {
                        switch (scoreSide[i]) {
                            case BACK:
                                addSequential(new MoveOnPath("SwitchBack" + posStr, MoveOnPath.Direction.FORWARD));
                                //TODO elevator stuff
                                break;
                            case MID: //The side placement positions are the initial pickup setups, but the other way.
                                addSequential(new MoveOnPath("CubePickupSetup" + posStr, MoveOnPath.Direction.FORWARD));
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
        switch(workingSide) {
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
