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

public class DegreeRotate extends PIDCommand implements Recallable<Double> {
	private static Logger log = LogManager.getLogger(DegreeRotate.class);
	private double MIN_PERCENT_VBUS;
	private final int ONTARGET_THRESHOLD = 3;

	private double targetHeading;
	private RotationType rotationType;
	private int counter;

	private Gyro gyro;
	private Recallable<Double> originator;
	private RecallMethod treatment;

	public DegreeRotate() {
		this(0, RotationType.RELATIVE);
	}

	/**
	 * Constructs this command with a setClawState degree to rotate.
	 * @param targetHeading the relative number of degrees to rotate by
	 */
	public DegreeRotate(double targetHeading, RotationType rotationType) {
		super( // Sloppy, but configurable
			DriveTrainSettings.getPIDValues("degreeRotate")[0],
			DriveTrainSettings.getPIDValues("degreeRotate")[1],
			DriveTrainSettings.getPIDValues("degreeRotate")[2]
		);
		log.info(getName() + " Beginning constructor");
		requires(Robot.driveTrain);

    	this.targetHeading = targetHeading;
    	this.rotationType = rotationType;

    	this.gyro = Robot.driveTrain.getGyro();

    	MIN_PERCENT_VBUS = DriveTrainSettings.getRotMinPVBus();

        log.info("DegreeRotate constructed");
    }

	public DegreeRotate(double targetHeading, RotationType rotationType, double minPercVBus) {
		this(targetHeading, rotationType);
		MIN_PERCENT_VBUS = minPercVBus;
	}

	public DegreeRotate(Recallable<Double> o, RecallMethod t) {
		this(0, RotationType.RELATIVE);

		if (o.getType() == getType()) {
			originator = o;
			treatment = t;
		} else {
			log.warn("Recallable type not equal! Looking for " + getType() + ", found " + o.getType() + ".");
		}
	}

    // Called just before this Command runs the first time
    protected void initialize() {
	    double[] outputRange = DriveTrainSettings.getOutputRange("degreeRotate");

    	if (originator != null) {
			targetHeading = originator.recall();

			if (treatment == RecallMethod.REVERSE)
				targetHeading *= -1;
		}

	    getPIDController().setInputRange(-36000, 36000);
    	getPIDController().setOutputRange(outputRange[0], outputRange[1]);

    	//Set the controller to continuous AFTER setInputRange()
        getPIDController().setContinuous(true);
        getPIDController().setAbsoluteTolerance(1.5);

        if (rotationType == RotationType.ABSOLUTE) {
            getPIDController().setSetpoint(targetHeading);
        } else {
            getPIDController().setSetpoint(targetHeading + gyro.getAngle());
        }

        System.out.println("targetHeading = " + targetHeading);

		log.info("DegreeRotate initialized");
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
		log.info("DegreeRotate ended");
		Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		log.info("DegreeRotate interrupted");
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

    public enum RotationType{
        ABSOLUTE,
        RELATIVE
    }
}
