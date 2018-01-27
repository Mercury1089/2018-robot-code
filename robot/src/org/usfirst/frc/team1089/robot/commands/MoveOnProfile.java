/**
 * 
 */
package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;

/**
 * Use motion profiling to move on a specified path
 */
public class MoveOnProfile extends Command {


	private TalonSRX leftFront;
	private TalonSRX rightFront;

    private final double PROPORTIONAL = .1;
    private final double INTEGRAL = 0;
    private final double DERIVATIVE = .05;

	public MoveOnProfile() {
        requires(Robot.driveTrain);
		leftFront = Robot.driveTrain.getLeft();
		rightFront = Robot.driveTrain.getRight();
	}

	
	//Called just before this Command runs for the first time. 
	protected void initialize() {

	    Robot.driveTrain.getLeft().setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Robot.driveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Robot.driveTrain.TIMEOUT_MS);

        //TODO Put different numbers. These are random numbers. Discuss later.
        Robot.driveTrain.getLeft().configMotionCruiseVelocity(15000, Robot.driveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().configMotionCruiseVelocity(15000, Robot.driveTrain.TIMEOUT_MS);

        Robot.driveTrain.getLeft().configMotionAcceleration(6000, Robot.driveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().configMotionAcceleration(6000, Robot.driveTrain.TIMEOUT_MS);


        Robot.driveTrain.configVoltage(0, .5);

        Robot.driveTrain.getLeft().config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);

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
		Robot.driveTrain.getLeft().setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, Robot.driveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, Robot.driveTrain.TIMEOUT_MS);
    }


}
