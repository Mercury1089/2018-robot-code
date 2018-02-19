package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.History;
import org.usfirst.frc.team1089.util.HistoryOriginator;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

/**
 * Turns the robot a set amount of degrees relative to its current angle.
 */
public class RotateRelative extends PIDCommand implements HistoryOriginator {
	private static Logger log = LogManager.getLogger(RotateRelative.class);
	private final double MIN_PERCENT_VBUS;
	private final int ONTARGET_THRESHOLD = 3;

	private double targetHeading;
	private int counter;

	private Gyro gyro;
	private HistoryOriginator originator;
	private HistoryTreatment treatment;

	public RotateRelative() {
		this(0);
	}

	/**
	 * Constructs this command with a setClawState degree to rotate.
	 * @param targetHeading the relative number of degrees to rotate by
	 */
	public RotateRelative(double targetHeading) {
		super( // Sloppy, but configurable
			DriveTrainSettings.getPIDValues("rotateRelative")[0],
			DriveTrainSettings.getPIDValues("rotateRelative")[1],
			DriveTrainSettings.getPIDValues("rotateRelative")[2]
		);
		log.info(getName() + " Beginning constructor");
		requires(Robot.driveTrain);

    	this.targetHeading = targetHeading;
    	this.gyro = Robot.driveTrain.getGyro();

    	MIN_PERCENT_VBUS = DriveTrainSettings.getRotMinPVBus();

        log.info("RotateRelative constructed");
    }

	public RotateRelative(HistoryOriginator o, HistoryTreatment t) {
		this(0);

		originator = o;
		treatment = t;
	}

    // Called just before this Command runs the first time
    protected void initialize() {
	    gyro.reset();
	    double[] outputRange = DriveTrainSettings.getOutputRange("rotateRelative");

    	if (originator != null) {
			targetHeading = (Double) originator.getHistory().getValue();

			if (treatment == HistoryTreatment.REVERSE)
				targetHeading *= -1;
		}

	    getPIDController().setInputRange(-180, 180);
    	getPIDController().setOutputRange(outputRange[0], outputRange[1]);

    	//Set the controller to continuous AFTER setInputRange()
        getPIDController().setContinuous(true);
        getPIDController().setAbsoluteTolerance(1.5);

    	getPIDController().setSetpoint(targetHeading);

		log.info("RotateRelative initialized");
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
		log.info("RotateRelative ended");
		Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		log.info("RotateRelative interrupted");
    	end();
    }

	@Override
	protected double returnPIDInput() {
	    return gyro.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		if(getPIDController().onTarget()) {
           log.info("Output before: " + output);
			output = 0;
		} else if(Math.abs(output) < MIN_PERCENT_VBUS) {
			output = Math.signum(output) * MIN_PERCENT_VBUS;
		}
		log.info("Output after: " + output);
		// TODO See if the NavX and Gyro's definition of a positive and negative angle match. If they do not match, then have the NavX class return the negated angle
		// and remove the negation on the next line.
		log.info("Output after: " + output);
		//TODO See if the NavX and Gyro's definition of a positive and negative angle match. If they do not match, then have the NavX class return the negated angle
		//and remove the negation on the next line.
        Robot.driveTrain.pidWrite(-output);
	}
	
	protected void updateHeading(double heading) {
		this.targetHeading = heading;
		getPIDController().setSetpoint(this.targetHeading);
	}

	@Override
	public History<Double> getHistory() {
		return new History<>(targetHeading);
	}

	@Override
	public CommandType getType() {
		return CommandType.DISTANCE;
	}
}
