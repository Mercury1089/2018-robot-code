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

    private int counter = 0;

    public UseElevator(Elevator.ELEVATOR_STATE targetState) {
        requires(Robot.elevator);
        setName("UseElevator (" + targetState + ")");
        this.targetState = targetState;
        log.info(getName() + " Constructed");
    }

    @Override
    protected void initialize() {
        Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetState.encPos);
        Robot.elevator.setCurrentState(targetState);

        log.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        exeLog.run(log -> log.debug(getName() + " executing"));

        if (targetState == Elevator.ELEVATOR_STATE.STOP) {
            if (Math.abs(Robot.elevator.getCurHeight()) <= 2.0 && !Robot.elevator.isLimitSwitchClosed()) {
                Robot.elevator.getElevatorTalon().set(ControlMode.Position, counter--);

                log.info("Did not reach, setting setpoint to " + targetState.encPos + counter);
            } else {
                counter = 0;
                Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetState.encPos);
                log.info("Reached!");
            }
        }

        Robot.driveTrain.setMaxOutput(DriveTrain.MAX_SPEED - (Robot.elevator.getCurHeight() / Elevator.MAX_HEIGHT) * (DriveTrain.MAX_SPEED - DriveTrain.MIN_SPEED)); //TODO test this
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
