package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

public class UseElevator extends Command {
    private static Logger log = LogManager.getLogger(UseElevator.class);
    private DelayableLogger exeLog = new DelayableLogger(log, 1, TimeUnit.SECONDS);

    private Elevator.ELEVATOR_STATE targetState;

    public UseElevator(Elevator.ELEVATOR_STATE targetState) {
        requires(Robot.elevator);
        setName(targetState + "Elevator");
        this.targetState = targetState;
    }

    @Override
    protected void initialize() {


        Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetState.encPos);
        log.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        exeLog.run( log -> log.debug(getName() + " executing"));
    }

    @Override
    protected void interrupted() {
        log.info(getName() + " interrupted");
    }

    @Override
    protected void end() {
        log.info(getName() + "elevator ended");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
