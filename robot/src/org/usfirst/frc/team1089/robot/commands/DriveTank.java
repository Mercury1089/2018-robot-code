package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;
import org.usfirst.frc.team1089.util.DelayableLogger;
import org.usfirst.frc.team1089.util.TalonDrive;

import java.util.concurrent.TimeUnit;

/**
 * Command that puts the drive train into a manual control mode.
 * This puts the robot in tank drive.
 */
public class DriveTank extends Command {
	private TalonDrive tDrive;
	private static Logger log = LogManager.getLogger(DriveTank.class);
	private DelayableLogger everySecond = new DelayableLogger(log, 1_000, TimeUnit.MILLISECONDS);
	//TODO: think of better naming convention for InfrequentLogger

	public DriveTank() {
		requires(Robot.driveTrain);
		setName("DriveTank Command");
		log.debug(getName() + " command created");
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		tDrive = Robot.driveTrain.getTalonDrive();
		Robot.driveTrain.getRight().setNeutralMode(NeutralMode.Brake);
		Robot.driveTrain.getLeft().setNeutralMode(NeutralMode.Brake);
		log.info(getName() + " command initialized");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (tDrive != null) {
			tDrive.tankDrive(Robot.oi.getY(DS_USB.LEFT_STICK), Robot.oi.getY(DS_USB.RIGHT_STICK));
		} else {
			log.info("Talon Drive is not initialized!");
		}
		everySecond.run(log -> log.info("tank driving"));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		//Robot.driveTrain.stop();
		log.info("TankDrive has ended");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		log.info("interrupted");
		end();
	}
}
