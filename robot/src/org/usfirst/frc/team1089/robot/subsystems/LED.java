package org.usfirst.frc.team1089.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1089.robot.commands.GetLEDStatus;

public class LED extends Subsystem {
    public LED() {

    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new GetLEDStatus());
    }
}
