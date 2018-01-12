package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;
import org.usfirst.frc.team1089.util.InfrequentLogger;
import org.usfirst.frc.team1089.util.TalonDrive;

import java.util.concurrent.TimeUnit;

/**
 * Command that puts the drive train into a manual control mode.
 * This puts the robot in arcade drive.
 */
public class DriveArcade extends Command {
	private TalonDrive tDrive;
	private Logger log = LogManager.getLogger();
	private InfrequentLogger everySecond = new InfrequentLogger(log, 1_000, TimeUnit.MILLISECONDS);
	//TODO: think of better naming convention for InfrequentLogger


	public DriveArcade() {
		requires(Robot.driveTrain);
		setName("DriveArcade Command");
		log.debug(getName() + " command created");
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		tDrive = Robot.driveTrain.getTalonDrive();
		log.info(getName() + " command initialized");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		tDrive.arcadeDrive(Robot.oi.getMoveValue(DS_USB.LEFT_STICK), -Robot.oi.getRotateValue(DS_USB.RIGHT_STICK), true);
		everySecond.run(log -> log.info("arcade driving"));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.driveTrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		log.info("interrupted");
		end();
	}
}
