package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;

/**
 * Turns the robot a set amount of degrees relative to its current angle.
 */
public class RotateRelative extends PIDCommand {

	private double targetHeading;
    private final double MIN_PERCENT_VBUS = 0.12;

	private int counter;
	private final int ONTARGET_THRESHOLD = 5;

	private Gyro gyro;
	private static Logger log = LogManager.getLogger(UseClaw.class);

	public RotateRelative() {
		this(0);
	}

	/**
	 * Constructs this command with a set degree to rotate.
	 * @param targetHeading the relative number of degrees to rotate by
	 */
	public RotateRelative(double targetHeading) {
		super(0.005, 0, 0);
		requires(Robot.driveTrain);

    	this.targetHeading = targetHeading;
    	this.gyro = Robot.driveTrain.getGyro();

        log.info("RotateRelative constructed");
    }


    // Called just before this Command runs the first time
    protected void initialize() {
	    gyro.reset();
    	
    	getPIDController().setInputRange(-180, 180);
    	getPIDController().setOutputRange(-.2, .2);

    	//Set the controller to continuous AFTER setInputRange()
        getPIDController().setContinuous(true);
        getPIDController().setAbsoluteTolerance(1.5);

    	getPIDController().setSetpoint(targetHeading);

		log.info("RotateRelative initialized");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(getPIDController().onTarget()) {
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
		//TODO See if the NavX and Gyro's definition of a positive and negative angle match. If they do not match, then have the NavX class return the negated angle
		//and remove the negation on the next line.
        Robot.driveTrain.pidWrite(-output);
	}
	
	protected void updateHeading(double heading) {
		this.targetHeading = heading;
		getPIDController().setSetpoint(this.targetHeading);
	}
}
