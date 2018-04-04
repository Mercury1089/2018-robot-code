package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.usfirst.frc.team1089.robot.subsystems.Claw;

public class DelayableClaw extends CommandGroup {
    public DelayableClaw(double secsDelay, Claw.ClawState clawState) {
        addSequential(new WaitCommand(secsDelay));
        addSequential(new UseClaw(clawState));
    }
}
