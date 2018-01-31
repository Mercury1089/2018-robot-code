package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GetCube extends CommandGroup {
    public GetCube() {
        addSequential(new AutoAlign());
        addSequential(new DriveWithLIDAR(2, .3));
    }
}
