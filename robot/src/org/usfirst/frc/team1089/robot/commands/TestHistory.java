package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1089.util.HistoryOriginator;

public class TestHistory extends CommandGroup {
    public TestHistory() {
        RotateToTarget cmd = new RotateToTarget();

        addSequential(cmd);
        addSequential(new RotateRelative(cmd, HistoryOriginator.HistoryTreatment.REVERSE));
    }
}
