package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.DelayableLogger;
import org.usfirst.frc.team1089.util.MercMath;
import org.usfirst.frc.team1089.util.TalonDrive;

import javax.xml.bind.Element;
import java.security.interfaces.ECKey;
import java.util.concurrent.TimeUnit;

/**
 * Command that puts the drive train into a manual control mode.
 * This puts the robot in arcade drive.
 */
public class DriveWithJoysticks extends Command {
	private TalonDrive tDrive;
	private static Logger log = LogManager.getLogger(DriveWithJoysticks.class);
	private DelayableLogger everySecond = new DelayableLogger(log, 1_000, TimeUnit.MILLISECONDS);
	private DriveType driveType;

	public enum DriveType {
		TANK,
		ARCADE
	}


	public DriveWithJoysticks(DriveType type) {
		requires(Robot.driveTrain);
		setName("DriveWithJoysticks Command");
		driveType = type;
		log.debug(getName() + " command created");
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		tDrive = Robot.driveTrain.getTalonDrive();
		log.info("Set max output to: " + tDrive.getMaxOutput());
		Robot.driveTrain.setNeutralMode(NeutralMode.Brake);
		log.info(getName() + " command initialized");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (tDrive != null) {
			double percent = Robot.elevator.getCurrentHeight() - Elevator.ElevatorPosition.INNER_STAGE.encPos;
			percent /= Elevator.MAX_HEIGHT - Elevator.ElevatorPosition.INNER_STAGE.encPos;

			// Should be an ease-out type of thing?
			tDrive.setMaxOutput(MercMath.lerp(Math.sqrt(percent), DriveTrain.MAX_SPEED, DriveTrain.MIN_SPEED));

			switch (driveType) {
				case TANK:
					tDrive.tankDrive(Robot.oi.getY(DS_USB.LEFT_STICK), Robot.oi.getY(DS_USB.RIGHT_STICK));
					break;
				case ARCADE:
					tDrive.arcadeDrive(Robot.oi.getY(DS_USB.LEFT_STICK), -Robot.oi.getX(DS_USB.RIGHT_STICK), true);
					break;
			}
		} else {
			log.info("Talon Drive is not initialized!");
		}
		everySecond.run(log -> log.info(driveType.toString() + " driving"));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
        Robot.driveTrain.setNeutralMode(NeutralMode.Brake);
		Robot.driveTrain.stop();
		log.info(getName() + "ended");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		log.info(getName() + "interrupted");
		end();
	}
}
