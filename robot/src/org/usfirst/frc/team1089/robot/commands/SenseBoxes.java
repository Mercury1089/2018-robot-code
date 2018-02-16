package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;

public class SenseBoxes extends Command {

    /**
     * C'tor
     */
    private static Logger log = LogManager.getLogger(SenseBoxes.class);
    public SenseBoxes() {
        log.info(getName() + " Beginning constructor");
    }
    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.claw.getCanifier().setLEDOutput(100.0, CANifier.LEDChannel.LEDChannelA);
        Robot.claw.getCanifier().setLEDOutput(59.0, CANifier.LEDChannel.LEDChannelB);
        Robot.claw.getCanifier().setLEDOutput(0.0, CANifier.LEDChannel.LEDChannelC);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
