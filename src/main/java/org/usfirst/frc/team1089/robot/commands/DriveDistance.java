package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

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
     * @param percentVoltage -1.0 to 1.0
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
    	Robot.driveTrain.resetEncoders();
		Robot.driveTrain.disableTalonDrive();
		Robot.driveTrain.getLeft().config_kP(Robot.SLOT_0, .1, Robot.TIMEOUT_MS);
		Robot.driveTrain.getLeft().config_kI(Robot.SLOT_0, 0, Robot.TIMEOUT_MS);
		Robot.driveTrain.getLeft().config_kD(Robot.SLOT_0, .05, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().config_kP(Robot.SLOT_0, .1, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().config_kI(Robot.SLOT_0, 0, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().config_kD(Robot.SLOT_0, .05, Robot.TIMEOUT_MS);

		Robot.driveTrain.getLeft().configNominalOutputForward(0, Robot.TIMEOUT_MS);
		Robot.driveTrain.getLeft().configNominalOutputReverse(0, Robot.TIMEOUT_MS);
		Robot.driveTrain.getLeft().configPeakOutputForward(percentVoltage, Robot.TIMEOUT_MS);
		Robot.driveTrain.getLeft().configPeakOutputReverse(-percentVoltage, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().configNominalOutputForward(0, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().configNominalOutputReverse(0, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().configPeakOutputForward(percentVoltage, Robot.TIMEOUT_MS);
		Robot.driveTrain.getRight().configPeakOutputReverse(-percentVoltage, Robot.TIMEOUT_MS);

		Robot.driveTrain.getLeft().set(ControlMode.Position, endPosL);
		Robot.driveTrain.getRight().set(ControlMode.Position, endPosR);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;
        
        double leftPos = Robot.driveTrain.getLeft().getSelectedSensorPosition(Robot.PRIMARY_PID_LOOP);
        //System.out.println(leftPos);
        double rightPos = Robot.driveTrain.getRight().getSelectedSensorPosition(Robot.PRIMARY_PID_LOOP);
        if ((leftPos > endPosL - MOVE_THRESHOLD && leftPos < endPosL + MOVE_THRESHOLD)
				&& (rightPos > endPosR - MOVE_THRESHOLD && rightPos < endPosR + MOVE_THRESHOLD)) {
   			isFinished = true;
   			System.out.println(" I'm done");
   		}

    	return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    	Robot.driveTrain.enableTalonDrive();
    	//Robot.driveTrain.resetEncoders();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println(" I'm interrupted");
    }
}
