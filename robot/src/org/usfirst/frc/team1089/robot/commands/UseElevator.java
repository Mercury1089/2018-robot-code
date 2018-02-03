package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

public class UseElevator extends Command {
    private static Logger log = LogManager.getLogger(UseElevator.class);
    private DelayableLogger exeLog = new DelayableLogger(log, 1, TimeUnit.SECONDS);

    Elevator.ELEVATOR_STATE target_state;

    public UseElevator(Elevator.ELEVATOR_STATE target_state) {
        requires(Robot.elevator);
        setName(target_state + "Elevator");
        this.target_state = target_state;
    }

    @Override
    protected void initialize() {
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
