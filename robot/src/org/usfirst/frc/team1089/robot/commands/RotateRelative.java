package org.usfirst.frc.team1089.robot.commands;
 import edu.wpi.first.wpilibj.interfaces.Gyro;
 import org.usfirst.frc.team1089.robot.Robot;
import edu.wpi.first.wpilibj.command.PIDCommand;
 import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
 import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

/**
 * Turns the robot a set amount of degrees relative to its current angle.
 */
public class RotateRelative extends PIDCommand {

	private double targetHeading;
    private final double MIN_PERCENT_VBUS;

	private int counter;
	private final int ONTARGET_THRESHOLD = 5;

	private Gyro gyro;

	public RotateRelative() {
		this(0);
	}

	/**
	 * Constructs this command with a set degree to rotate.
	 * @param targetHeading the relative number of degrees to rotate by
	 */
	public RotateRelative(double targetHeading) {
		super( // Sloppy, but configurable
			DriveTrainSettings.getPIDValues()[0],
			DriveTrainSettings.getPIDValues()[1],
			DriveTrainSettings.getPIDValues()[2]
		);

		requires(Robot.driveTrain);

    	this.targetHeading = targetHeading;
    	this.gyro = Robot.driveTrain.getGyro();

    	MIN_PERCENT_VBUS = DriveTrainSettings.getRotMinPVBus();

        System.out.println("RotateRelative constructed");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
	    gyro.reset();

	    double[] outputRange = DriveTrainSettings.getRotOutputRange();

    	getPIDController().setInputRange(-180, 180);
    	getPIDController().setOutputRange(outputRange[0], outputRange[1]);

    	//Set the controller to continuous AFTER setInputRange()
        getPIDController().setContinuous(true);
        getPIDController().setAbsoluteTolerance(DriveTrainSettings.getRotAbsTolerance());

    	getPIDController().setSetpoint(targetHeading);

		System.out.println("RotateRelative initialized");
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
		System.out.println("RotateRelative ended");
		Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		System.out.println("RotateRelative interrupted");
    	end();
    }

	@Override
	protected double returnPIDInput() {
	    return gyro.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		if(getPIDController().onTarget()) {
            System.out.println("Output before: " + output);
			output = 0;
		} else if(Math.abs(output) < MIN_PERCENT_VBUS) {
			output = Math.signum(output) * MIN_PERCENT_VBUS;
		}
		System.out.println("Output after: " + output);
		// TODO See if the NavX and Gyro's definition of a positive and negative angle match. If they do not match, then have the NavX class return the negated angle
		// and remove the negation on the next line.
        Robot.driveTrain.pidWrite(-output);
	}
	
	protected void updateHeading(double heading) {
		this.targetHeading = heading;
		getPIDController().setSetpoint(this.targetHeading);
	}
}
