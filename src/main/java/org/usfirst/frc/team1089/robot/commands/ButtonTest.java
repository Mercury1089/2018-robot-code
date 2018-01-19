package org.usfirst.frc.team1089.robot.commands;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;
import org.usfirst.frc.team1089.util.DelayableLogger;
import org.usfirst.frc.team1089.util.TalonDrive;

import edu.wpi.first.wpilibj.command.Command;

public class ButtonTest extends Command {

	public ButtonTest() {
		setName("ButtonTest Command");
		System.out.println(getName() + " command created");
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println((getName() + " command initialized"));
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
		System.out.println((getName() + " command ended"));
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}

}
