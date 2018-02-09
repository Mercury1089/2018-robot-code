package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.filters.LinearDigitalFilter;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.sensors.PixyCam;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

/**
 * Turns the robot towards a target found with a camera
 */
public class RotateToTarget extends PIDCommand {
	private static Logger log = LogManager.getLogger(RotateToTarget.class);
	private double targetHeading;
	private int counter;

	private final double MIN_PERCENT_VBUS;
	private final int ONTARGET_THRESHOLD = 3;

	private LinearDigitalFilter filter;

	/**
	 * Constructs this command with a set degree to rotate.
	 */
	public RotateToTarget() {
		super( // Sloppy, but configurable
				DriveTrainSettings.getPIDValues("rotateToTarget")[0],
				DriveTrainSettings.getPIDValues("rotateToTarget")[1],
				DriveTrainSettings.getPIDValues("rotateToTarget")[2]
		);

		requires(Robot.driveTrain);
		requires(Robot.vision);

		this.targetHeading = targetHeading;
		filter = LinearDigitalFilter.movingAverage(Robot.vision.getPixyCam(), 5);

		MIN_PERCENT_VBUS = DriveTrainSettings.getRotMinPVBus();

		log.info("RotateToTarget constructed");
	}


	// Called just before this Command runs the first time
	protected void initialize() {
		filter.reset();
		double[] outputRange = DriveTrainSettings.getRotOutputRange();

		getPIDController().setInputRange(-160, 160);
		getPIDController().setOutputRange(-.2, .2);

		//Set the controller to continuous AFTER setInputRange()
		getPIDController().setContinuous(false);
		getPIDController().setAbsoluteTolerance(5);

		getPIDController().setSetpoint(0);

		log.info("RotateToTarget initialized");
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (getPIDController().onTarget()) {
			counter++;
		} else {
			counter = 0;
		}
		return counter > ONTARGET_THRESHOLD;
	}

	// Called once after isFinished returns true
	protected void end() {
		log.info("RotateToTarget ended");
		Robot.driveTrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		log.info("RotateToTarget interrupted");
		end();
	}

	@Override
	protected double returnPIDInput() {
		return filter.pidGet();
	}

	@Override
	protected void usePIDOutput(double output) {
		if (getPIDController().onTarget()) {
			log.info("Output before: " + output);
			output = 0;
		} else if (Math.abs(output) < MIN_PERCENT_VBUS)
			output = Math.signum(output) * MIN_PERCENT_VBUS;

		log.info("Output after: " + output);

		Robot.driveTrain.pidWrite(-output);
	}
}

