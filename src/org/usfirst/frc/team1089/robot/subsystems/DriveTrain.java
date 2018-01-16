package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;
import org.usfirst.frc.team1089.robot.commands.DriveArcade;
import org.usfirst.frc.team1089.robot.commands.DriveTank;
import org.usfirst.frc.team1089.util.TalonDrive;

/**
 * Subsystem that encapsulates the drive train.
 * This contains the {@link TalonDrive} needed to drive manually
 * using the Talons.
 */
public class DriveTrain extends Subsystem {
	private TalonSRX tFrontLeft, tFrontRight, tBackLeft, tBackRight;
	private TalonDrive tDrive;
	public static final double WHEEL_DIAMETER = 5.0;
	
	/**
	 * Creates the drivetrain, assuming that there are four talons.
	 * 
	 * @param fl Front-left Talon ID
	 * @param fr Front-right Talon ID
	 * @param bl Back-left Talon ID	 
	 * @param br Back-right Talon ID
	 */
	public DriveTrain(int fl, int fr, int bl, int br) {
		tFrontLeft = new TalonSRX(fl);
		tFrontRight = new TalonSRX(fr);
		tBackLeft = new TalonSRX(bl);
		tBackRight = new TalonSRX(br);
		
		// Set follower control on back talons.
		tBackLeft.set(ControlMode.Follower, fl);
		tBackRight.set(ControlMode.Follower, fr);
	}
	
	/**
	 * Gets the Talon based on the ID.
	 * 
	 * @param id The device ID of the Talon.
	 * @return The Talon bound to the ID port,
	 *         or {@code null} if no drivetrain Talon was found.
	 *         
	 * @see CAN RobotMap.CAN
	 */
	public TalonSRX getTalon(int id) {
		switch(id) {
			case CAN.TALON_DRIVETRAIN_FL:
				return tFrontLeft;
			case CAN.TALON_DRIVETRAIN_FR:
				return tFrontRight;
			case CAN.TALON_DRIVETRAIN_BR:
				return tBackLeft;
			case CAN.TALON_DRIVETRAIN_BL:
				return tBackRight;
			default: // Not a drivetrain Talon!
				return null;
		}
	}
	
	public TalonSRX getLeft() {
		return tFrontLeft;
	}
	
	public TalonSRX getRight() {
		return tFrontRight;
	}
	
	public TalonDrive getTalonDrive() {
		return tDrive;
	}
	
	/**
	 * Stops the motors by zeroing the left and right Talons.
	 */
	public void stop() {
		tFrontLeft.set(ControlMode.Velocity, 0);
		tFrontRight.set(ControlMode.Velocity, 0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveTank());
	}
}
