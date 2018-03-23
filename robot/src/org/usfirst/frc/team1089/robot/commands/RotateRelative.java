package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.util.Recallable;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

/**
 * Turns the robot a set amount of degrees relative to its current angle.
 */
public class RotateRelative extends PIDCommand implements Recallable<Double> {
	private static Logger log = LogManager.getLogger(RotateRelative.class);
	private final double MIN_PERCENT_VBUS;
	private final int ONTARGET_THRESHOLD = 3;

	private double targetHeading;
	private int counter;

	private Gyro gyro;
	private Recallable<Double> originator;
	private RecallMethod treatment;

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

	public RotateRelative(Recallable<Double> o, RecallMethod t) {
		this(0);

		if (o.getType() == getType()) {
			originator = o;
			treatment = t;
		} else {
			log.warn("Recallable type not equal! Looking for " + getType() + ", found " + o.getType() + ".");
		}
	}

    // Called just before this Command runs the first time
    protected void initialize() {
	    gyro.reset();
	    double[] outputRange = DriveTrainSettings.getOutputRange("rotateRelative");

    	if (originator != null) {
			targetHeading = originator.recall();

			if (treatment == RecallMethod.REVERSE)
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

        Robot.driveTrain.pidWrite(-output);
	}
	
	protected void updateHeading(double heading) {
		targetHeading = heading;

		getPIDController().setSetpoint(targetHeading);
	}

	@Override
	public Double recall() {
		return targetHeading;
	}

	@Override
	public CommandType getType() {
		return CommandType.ROTATION;
	}
}
