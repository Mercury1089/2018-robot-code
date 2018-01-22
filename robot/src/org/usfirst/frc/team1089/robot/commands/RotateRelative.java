package org.usfirst.frc.team1089.robot.commands;
 import org.usfirst.frc.team1089.robot.Robot;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 * Turns the robot a set amount of degrees relative to its current angle.
 */
public class RotateRelative extends PIDCommand {

	private double _heading;
	
	private int counter;
	private final int ONTARGET_THRESHOLD = 5;
    
    private final double MIN_PERCENT_VBUS = 0.15;

	/**
	 * Constructs this command with a set degree to rotate.
	 *
	 * @param heading the amount of degrees to rotate by
	 */
	public RotateRelative(double heading) {
		super(0.060, 0.05, 0.5);
		requires(Robot.driveTrain);

		System.out.println("RotateRelative constructed");
    	_heading = heading;
    	//MercLogger.logMessage(Level.INFO, "RotateRelative: Constructed using RotateRelative(double heading).");
    }


    // Called just before this Command runs the first time
    protected void initialize() {
		Robot.navX.reset();

    	getPIDController().setContinuous(true);
    	getPIDController().setAbsoluteTolerance(1.5);
    	
    	getPIDController().setInputRange(-180, 180);
    	getPIDController().setOutputRange(-.2, .2);

    	getPIDController().setSetpoint(_heading);

		System.out.println("RotateRelative initialized");
    	//MercLogger.logMessage(Level.INFO, "RotateRelative: Initialized with heading: " + _heading);
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
		System.out.println("RotateRelative ended");
		Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//MercLogger.logMessage(Level.INFO, "RotateRelative: Interrupted");
		System.out.println("RotateRelative interrupted");
    	end();
    }

	@Override
	protected double returnPIDInput() {
		return Robot.navX.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		if(getPIDController().onTarget()) {
			output = 0;
		} else if(Math.abs(output) < MIN_PERCENT_VBUS) {
			output = Math.signum(output) * MIN_PERCENT_VBUS;
		}
		Robot.driveTrain.pidWrite(output);
	}
	
	protected void updateHeading(double heading) {
		_heading = heading;
		getPIDController().setSetpoint(_heading);
	}
}
