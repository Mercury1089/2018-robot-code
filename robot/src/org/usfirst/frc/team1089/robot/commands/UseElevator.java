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
    private final Logger LOG = LogManager.getLogger(UseElevator.class);
    private final DelayableLogger SLOW_LOG = new DelayableLogger(LOG, 1, TimeUnit.SECONDS);

    private Elevator.ElevatorState targetState;

    private int counter = 0;

    public UseElevator(Elevator.ElevatorState targetState) {
        requires(Robot.elevator);
        setName("UseElevator (" + targetState + ")");
        this.targetState = targetState;
        LOG.info(getName() + " Constructed");
    }

    @Override
    protected void initialize() {
        Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetState.encPos);
        Robot.elevator.setCurrentState(targetState);

        LOG.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        SLOW_LOG.run(log -> log.debug(getName() + " executing"));

        if (targetState == Elevator.ElevatorState.STOP) {
            if (Math.abs(Robot.elevator.getHeight()) <= 2.0 && !Robot.elevator.isLimitSwitchClosed()) {
                Robot.elevator.getElevatorTalon().set(ControlMode.Position, counter--);

                LOG.info("Did not reach, setting setpoint to " + targetState.encPos + counter);
            } else {
                counter = 0;
                Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetState.encPos);
                LOG.info("Reached!");
            }
        }

        double maxOut = DriveTrain.MAX_SPEED - (Robot.elevator.getHeight() / Elevator.MAX_HEIGHT) * (DriveTrain.MAX_SPEED - DriveTrain.MIN_SPEED);
        LOG.info("Set max output to " + maxOut);
        Robot.driveTrain.setMaxOutput(maxOut); //TODO test this
    }

    @Override
    protected void interrupted() {
        LOG.info(getName() + " interrupted");
    }

    @Override
    protected void end() {
        LOG.info(getName() + "elevator ended");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
