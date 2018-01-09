package org.usfirst.frc.team1089.robot.subsystems;

import org.usfirst.frc.team1089.util.TalonDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem that encapsulates the drive train.
 * This contains the {@link TalonDrive} needed to drive manually
 * using the Talons.
 */
public class DriveTrain extends Subsystem {
	private TalonSRX tFrontLeft, tFrontRight, tBackLeft, tBackRight;
	private TalonDrive tDrive;
	
	public DriveTrain(int fr, int fl, int br, int bl) {
		tFrontRight = new TalonSRX(fr);
		tFrontLeft = new TalonSRX(fl);
		tBackRight = new TalonSRX(br);
		tBackLeft = new TalonSRX(bl);
		
		// Set follower control on back talons.
		tBackLeft.set(ControlMode.Follower, fl);
		tBackRight.set(ControlMode.Follower, fr);
		
		tDrive = new TalonDrive(tFrontLeft, tFrontRight, tBackLeft, tBackRight);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
	
	
}
