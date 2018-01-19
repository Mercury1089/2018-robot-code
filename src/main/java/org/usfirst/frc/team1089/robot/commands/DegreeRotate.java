package org.usfirst.frc.team1089.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.logging.Level;

import org.usfirst.frc.team1089.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class DegreeRotate extends PIDCommand {

	protected double _heading;
	private DoubleSupplier _angleSupplier = null;
	
	private int counter;
	private final int ONTARGET_THRESHOLD = 5;
    
    private final double MIN_PERCENT_VBUS = 0.15;
	
    protected DegreeRotate() {
    	super(0.460, 0.002, 0.0);							//super(0.195 -> 0.225 -> 0.260 -> 0.360, 0.0008 -> 0.001 -> 0.002, 0.0);
    	_heading = 0.0;
    	requires(Robot.driveTrain);    	
    }
    
    /**
     * Construct a DegreeRotate command that gets its angle during initialize by calling
     * the provided DoubleSupplier method.
     * @param angleSupplier The DoubleSupplier method to output the angle.
     */
    public DegreeRotate(DoubleSupplier angleSupplier) {
    	this();
    	_angleSupplier = angleSupplier;
    	//MercLogger.logMessage(Level.INFO, "DegreeRotate: Constructed using DegreeRotate(DoubleSupplier angleSupplier)");
	}
    
    public DegreeRotate(double heading) {
    	this();
    	_heading = heading;
    	//MercLogger.logMessage(Level.INFO, "DegreeRotate: Constructed using DegreeRotate(double heading).");
    }
    

    // Called just before this Command runs the first time
    protected void initialize() {
    	//MercLogger.logMessage(Level.INFO, "Entering DegreeRotate.initialize()");
    	if (_angleSupplier != null) {
    		_heading = _angleSupplier.getAsDouble();
    	}
    	getPIDController().setContinuous(true);
    	getPIDController().setAbsoluteTolerance(1.5);
    	
    	getPIDController().setInputRange(-180, 180);
    	getPIDController().setOutputRange(-.6, .6);
    	
    	Robot.ahrs.reset();
    	getPIDController().setSetpoint(_heading);

    	//MercLogger.logMessage(Level.INFO, "DegreeRotate: Initialized with heading: " + _heading);
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
    	//MercLogger.logMessage(Level.INFO, "Entering DegreeRotate.end()");
    	//MercLogger.logMessage(Level.INFO, "Current setpoint: " + getPIDController().getSetpoint());
    	Robot.driveTrain.stop();
    	//MercLogger.logMessage(Level.INFO, "Gyro reads: " + Robot.driveTrain.getGyro().getAngle() + " degrees.");
		//MercLogger.logMessage(Level.INFO, "DegreeRotate: Completed");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//MercLogger.logMessage(Level.INFO, "DegreeRotate: Interrupted");
    	end();
    }

	@Override
	protected double returnPIDInput() {
		return Robot.ahrs.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		if(getPIDController().onTarget()) {
			output = 0;
		} else if(Math.abs(output) < MIN_PERCENT_VBUS) {
			output = Math.signum(output) * MIN_PERCENT_VBUS;
		}
	}
	
	protected void updateHeading(double heading) {
		_heading = heading;
		getPIDController().setSetpoint(_heading);
	}
}
