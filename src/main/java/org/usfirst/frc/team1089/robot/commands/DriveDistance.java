package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Uses Talons and mag encoders to drive a set distance.
 */
public class DriveDistance extends Command {

	private double distance;
	private double voltage;
    private double endPosL, endPosR;
    private double waitTime;
	
    /**
     * 
     * @param distance in inches
     * @param voltage -1.0 to 1.0
     */
    public DriveDistance(double distance, double voltage) {
    	this.distance = distance;
    	this.voltage = voltage;
    	endPosL = distance / (Math.PI * Robot.driveTrain.WHEEL_DIAMETER);
    	endPosR = -endPosL;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.getLeft().set(ControlMode.Position, endPosL);
		Robot.driveTrain.getRight().set(ControlMode.Position, endPosR);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean leftFinished = Robot.driveTrain.getLeft().getSelectedSensorPosition(0) == endPosL;
        boolean rightFinished = Robot.driveTrain.getLeft().getSelectedSensorPosition(0) == endPosR;
        
    	return leftFinished && rightFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
