package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

public class ButtonTest extends Command {

	private static Logger log = LogManager.getLogger(UseClaw.class);
	private DelayableLogger exeLog = new DelayableLogger(log, 1, TimeUnit.SECONDS);
	public ButtonTest() {
		setName("ButtonTest Command");
		log.info(getName() + " command created");
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		log.info((getName() + " command initialized"));
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
		log.info((getName() + " command ended"));
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}

}
