package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

public class UseClaw extends Command {

    private Claw.ClawState targetState;
    private static Logger log = LogManager.getLogger(UseClaw.class);
    private DelayableLogger exeLog = new DelayableLogger(log, 1, TimeUnit.SECONDS);
    private final double minimumDistance = .3, maximumDistance = 1.5;

    public UseClaw(Claw.ClawState state) {
        requires(Robot.claw);
        setName(state + "_Claw");
        targetState = state;
    }

    @Override
    protected void initialize() {
        log.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        Robot.claw.set(targetState);
        exeLog.run( log -> log.debug(getName() + " executing"));
    }

    @Override
    protected void interrupted() {
        log.info(getName() + " interrupted");
    }

    @Override
    protected void end() {
        log.info(getName() + " ended");
        Robot.claw.set(Claw.ClawState.STOP);
    }

    @Override
    protected boolean isFinished() {
        if(targetState == Claw.ClawState.GRAB) {
            return Robot.manipulator.getLidar().getDistance() - minimumDistance <= 0;
        }
        return maximumDistance - Robot.manipulator.getLidar().getDistance() <= 0;
    }
}
