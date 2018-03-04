package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.Robot;

public class CalibrateGyro extends Command {

    public CalibrateGyro() {
        setRunWhenDisabled(true);
    }

    public void initialize() {
        System.out.println("Calibrating gyro...");
        Robot.driveTrain.getGyro().calibrate();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
