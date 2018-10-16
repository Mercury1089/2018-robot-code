package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

/**
 * Command that simply waits for the encoders to reach a certain value.
 */
public class WaitForEncoder extends Command {
    private final Logger LOG = LogManager.getLogger(WaitForEncoder.class);
    private final DelayableLogger SLOW_LOG = new DelayableLogger(LOG, 1, TimeUnit.SECONDS);

    private final int TARGET_VALUE;
    private final double TARGET_THRESHOLD = 0.2;

    /**
     * Creates this command with the specified target value
     *
     * @param val target value to wait for (in feet)
     */
    public WaitForEncoder(int val) {
        requires(Robot.driveTrain);

        TARGET_VALUE = val;
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(Robot.driveTrain.getLeftEncPositionInFeet() - TARGET_VALUE) <= TARGET_THRESHOLD;
    }

    @Override
    protected void interrupted() {
        end();
    }
}
