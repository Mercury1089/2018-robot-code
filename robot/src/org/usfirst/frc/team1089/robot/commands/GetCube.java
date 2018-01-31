package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Command group that calls both the AutoAlign command
 * and the DriveWithLIDAR command to autonomously target and
 * approach the cube.
 */
public class GetCube extends CommandGroup {
    public GetCube() {
        addSequential(new AutoAlign());
        addSequential(new DriveWithLIDAR(2, .3));
    }
}
