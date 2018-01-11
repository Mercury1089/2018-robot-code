package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;
import org.usfirst.frc.team1089.util.TalonDrive;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command that puts the drive train into a manual control mode.
 * This puts the robot in tank drive.
 */
public class DriveTank extends Command {
	private TalonDrive tDrive;
	
	public DriveTank() {
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		tDrive = Robot.driveTrain.getTalonDrive();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		tDrive.tankDrive(-Robot.oi.getMoveValue(DS_USB.LEFT_STICK), -Robot.oi.getMoveValue(DS_USB.RIGHT_STICK));
		System.out.println("driving");
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
		end();
	}
}
