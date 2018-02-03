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
        addSequential(new MoveOnPath("SwitchMidRight", MoveOnPath.Direction.FORWARD));
        addSequential(new MoveOnPath("CubePickupSetupRight", MoveOnPath.Direction.FORWARD));
        addSequential(new MoveOnPath("GrabCubeDeadReckoning", MoveOnPath.Direction.FORWARD));
        addSequential(new MoveOnPath("GrabCubeDeadReckoning", MoveOnPath.Direction.BACKWARD));
        addSequential(new MoveOnPath("ScaleFrontRight", MoveOnPath.Direction.FORWARD));
    }
}
