/**
 * 
 */
package org.usfirst.frc.team1089.robot.commands;

import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Use motion profiling to move on a specified path
 */
public class MoveOnProfile extends Command {

	private double endPos, maxVel, maxAccel;
	private boolean curved;
	private int numPoints;
	private TalonSRX leftFront;
	private TalonSRX rightFront;
	private TalonSRX leftBack;
	private TalonSRX rightBack;
	
	
	/**
	 * 
	 * Creates and uses a motion profile using the given parameters to the given position setpoint.
	 * 
	 * @param endPosition The final location of the robot after the motion profile has ended. 
	 * @param maxVelocity The maximum velocity allowed at any given point in the motion profile
	 * @param maxAcceleration The maximum acceleration allowed at any given point in the motion profile
	 * @param curvedPath If true, the robot will travel on a curved path to the given endPos. If false, it will travel in a linear motion to the endPos.
	 * @param numberOfPoints The number of trajectory points that will be in the motion profile.
	 */
	public MoveOnProfile(double endPosition, double maxVelocity, double maxAcceleration, boolean curvedPath, int numberOfPoints) {
		endPos = endPosition;
		maxVel = maxVelocity;
		maxAccel = maxAcceleration;
		curved = curvedPath;
		numPoints = numberOfPoints;
		

		leftFront = Robot.driveTrain.getTalon(CAN.TALON_DRIVETRAIN_FL);
		rightFront = Robot.driveTrain.getTalon(CAN.TALON_DRIVETRAIN_FR);
		leftBack = Robot.driveTrain.getTalon(CAN.TALON_DRIVETRAIN_BL);
		rightBack = Robot.driveTrain.getTalon(CAN.TALON_DRIVETRAIN_BR);
		
		requires(Robot.driveTrain);
	}

	
	//Called just before this Command runs for the first time. 
	protected void initialize() {
		leftFront.set(ControlMode.MotionProfile, 0);
		rightFront.set(ControlMode.MotionProfile, 0);
	}


	//Called repeatedly when this Command is scheduled to run.
	protected void execute() {
		// TODO Auto-generated method stub
		super.execute();
	}


	//Make this return true when this command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}


	//Called once after isFinished() returns true
	protected void end() {
		
	}


}
