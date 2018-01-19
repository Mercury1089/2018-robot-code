package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Uses Talons and mag encoders to drive a set distance.
 */
public class DriveDistance extends Command {

	private static double MOVE_THRESHOLD = .1;
	private double distance;
	private double percentVoltage; //Voltage is NOW from [-1, 1]
    private double endPosL, endPosR;
    private double waitTime;
	
    /**
     * 
     * @param distance in inches
     * @param voltage -1.0 to 1.0
     */
    public DriveDistance(double distance, double percentVoltage) {
    	requires(Robot.driveTrain);
    	this.distance = distance;
    	this.percentVoltage = percentVoltage;
    	endPosL = (distance / (Math.PI * Robot.driveTrain.WHEEL_DIAMETER_INCHES)) * Robot.driveTrain.MAG_ENCODER_TICKS_PER_REVOLUTION;
    	endPosR = -endPosL;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
<<<<<<< HEAD
    	System.out.println("Drive with distance has been initialized");
    	
=======

>>>>>>> master
    	Robot.driveTrain.resetEncoders();

		Robot.driveTrain.getLeft().config_kP(0, .1, 0);
		Robot.driveTrain.getLeft().config_kI(0, 0, 0);
		Robot.driveTrain.getLeft().config_kD(0, .05, 0);
		Robot.driveTrain.getRight().config_kP(0, .1, 0);
		Robot.driveTrain.getRight().config_kI(0, 0, 0);
		Robot.driveTrain.getRight().config_kD(0, .05, 0);

		Robot.driveTrain.getLeft().configNominalOutputForward(0, 0);
		Robot.driveTrain.getLeft().configNominalOutputReverse(0, 0);
		Robot.driveTrain.getLeft().configPeakOutputForward(percentVoltage, 0);
		Robot.driveTrain.getLeft().configPeakOutputReverse(-percentVoltage, 0);
		Robot.driveTrain.getRight().configNominalOutputForward(0, 0);
		Robot.driveTrain.getRight().configNominalOutputReverse(0, 0);
		Robot.driveTrain.getRight().configPeakOutputForward(percentVoltage, 0);
		Robot.driveTrain.getRight().configPeakOutputReverse(-percentVoltage, 0);
<<<<<<< HEAD
		
		System.out.println(endPosL);
=======


>>>>>>> master
		Robot.driveTrain.getLeft().set(ControlMode.Position, endPosL);
		Robot.driveTrain.getRight().set(ControlMode.Position, endPosR);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
<<<<<<< HEAD
    	
=======

>>>>>>> master
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;
        
        double leftPos = Robot.driveTrain.getLeft().getSelectedSensorPosition(0);
        //System.out.println(leftPos);
        double rightPos = Robot.driveTrain.getRight().getSelectedSensorPosition(0);
<<<<<<< HEAD
        //System.out.println(rightPos);
 
=======

>>>>>>> master
        if ((leftPos > endPosL - MOVE_THRESHOLD && leftPos < endPosL + MOVE_THRESHOLD)
				&& (rightPos > endPosR - MOVE_THRESHOLD && rightPos < endPosR + MOVE_THRESHOLD)) {
   			isFinished = true;
   			System.out.println(" I'm done");
   		}

    	return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
<<<<<<< HEAD
    	
    	System.out.println(" Drive with distance has ended");
    	
=======

>>>>>>> master
    	Robot.driveTrain.stop();
    	//Robot.driveTrain.resetEncoders();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println(" I'm interrupted");
    }
}
