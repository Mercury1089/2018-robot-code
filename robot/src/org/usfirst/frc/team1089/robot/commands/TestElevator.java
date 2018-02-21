package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

/**
 * Command that gives direct control of the elevator using the gamepad's joysticks
 */
public class TestElevator extends Command {
    private final Logger LOG = LogManager.getLogger(TestElevator.class);
    private final DelayableLogger SLOW_LOG = new DelayableLogger(LOG, 1, TimeUnit.SECONDS);

    private int counter = 0;

    public TestElevator() {
        requires(Robot.elevator);

        LOG.info(getName() + " Constructed");
    }

    @Override
    protected void initialize() {
        LOG.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        SLOW_LOG.run(log -> log.debug(getName() + " executing"));

        Robot.elevator.getElevatorTalon().set(ControlMode.PercentOutput, Robot.oi.getY(RobotMap.DS_USB.GAMEPAD) * 0.5);
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
