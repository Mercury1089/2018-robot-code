package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

public class GetLEDStatus extends Command {
    private final Logger LOG = LogManager.getLogger(GetLEDStatus.class);
    private final DelayableLogger SLOW_LOG = new DelayableLogger(LOG, 5, TimeUnit.SECONDS);


    public GetLEDStatus() {
        requires(Robot.ledIndicators);
    }

    @Override
    protected void initialize() {
        LOG.info("GetLEDStatus initialized");
    }

    @Override
    protected void execute () {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
