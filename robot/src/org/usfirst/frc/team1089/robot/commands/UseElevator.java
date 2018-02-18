package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

public class UseElevator extends Command {
    private static Logger log = LogManager.getLogger(UseElevator.class);
    private DelayableLogger exeLog = new DelayableLogger(log, 10, TimeUnit.SECONDS);

    private Elevator.ELEVATOR_STATE targetState;

    public UseElevator(Elevator.ELEVATOR_STATE targetState) {
        // requires(Robot.elevator);
        setName(targetState + "Elevator");
        this.targetState = targetState;
        log.info(getName() + " Constructed");
    }

    @Override
    protected void initialize() {
        if (targetState != Elevator.ELEVATOR_STATE.STOP) {
            Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetState.encPos);
            Robot.elevator.setCurrentState(targetState);
        }
        log.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        exeLog.run( log -> log.debug(getName() + " executing"));
        if (targetState == Elevator.ELEVATOR_STATE.STOP) {
            while(!Robot.elevator.getLimitSwitch().get()) {
                Robot.elevator.getElevatorTalon().set(ControlMode.PercentOutput, -0.3);
            }

            Robot.elevator.setCurHeight(Robot.elevator.getElevatorTalon().getSensorCollection().getQuadraturePosition());
            Robot.elevator.setCurrentState(targetState);
            Robot.elevator.getElevatorTalon().getSensorCollection().setQuadraturePosition((int)Elevator.ELEVATOR_STATE.STOP.encPos, 0);
        }
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
