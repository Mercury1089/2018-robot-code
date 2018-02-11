package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

/**
 * Command that utilizes the claw depending on the state given.
 */
public class UseClaw extends Command {
    private static final Logger LOG = LogManager.getLogger(UseClaw.class);

    private Claw.ClawState targetState;
    private DelayableLogger exeLog = new DelayableLogger(LOG, 1, TimeUnit.SECONDS);
    private final double minimumDistance = 8, maximumDistance = 18;

    public UseClaw(Claw.ClawState state) {
        requires(Robot.claw);
        setName(state + "_Claw");
        targetState = state;
    }

    @Override
    protected void initialize() {
        LOG.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        Robot.claw.set(targetState);
        exeLog.run( log -> log.debug(getName() + " executing"));
    }

    @Override
    protected void interrupted() {
        LOG.info(getName() + " interrupted");
    }

    @Override
    protected void end() {
        LOG.info(getName() + " ended");
        if((targetState == Claw.ClawState.GRAB))
            Robot.claw.setHasCube(true);
        else if (targetState == Claw.ClawState.EJECT)
            Robot.claw.setHasCube(false);
        Robot.claw.set(Claw.ClawState.STOP);
    }

    @Override
    protected boolean isFinished() {
        if (targetState == Claw.ClawState.GRAB)
            return Robot.claw.getLidar().getDistance() - minimumDistance <= 0;

        return maximumDistance - Robot.claw.getLidar().getDistance() <= 0;
    }
}
