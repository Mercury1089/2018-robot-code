package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDOutput;
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
public class DriveTrain extends Subsystem implements PIDOutput {
	public static final int TIMEOUT_MS = 10;
	public static final int SLOT_0 = 0;
	public static final int PRIMARY_PID_LOOP = 0;

	private WPI_TalonSRX tFrontLeft, tFrontRight, tBackLeft, tBackRight;
	private TalonDrive tDrive;

	public static final double WHEEL_DIAMETER_INCHES = 4.0 ;
	public static final int MAG_ENCODER_TICKS_PER_REVOLUTION = 4096; //TODO Old Crossfire values
	public static final double GEAR_RATIO = 1.0;                      //TODO Old Crossfire values

	/**
	 * Creates the drivetrain, assuming that there are four talons.
	 *
	 * @param fl Front-left Talon ID
	 * @param fr Front-right Talon ID
	 * @param bl Back-left Talon ID
	 * @param br Back-right Talon ID
	 */
	public DriveTrain(int fl, int fr, int bl, int br) {

		//Use WPI_TalonSRX instead of TalonSRX to make sure it interacts properly with WPILib.
		tFrontLeft = new WPI_TalonSRX(fl);
		tFrontRight = new WPI_TalonSRX(fr);
		tBackLeft = new WPI_TalonSRX(bl);
		tBackRight = new WPI_TalonSRX(br);


		//Account for motor orientation.
		tFrontLeft.setInverted(true);
		tBackLeft.setInverted(true);
		tFrontRight.setInverted(false);
		tBackRight.setInverted(false);

		//Account for encoder orientation.
		tFrontLeft.setSensorPhase(true);
		tFrontRight.setSensorPhase(true);

		tDrive = new TalonDrive(tFrontLeft, tFrontRight);

		// Set follower control on back talons. Use follow() instead of ControlMode.Follower so that Talons can follow Victors and vice versa.
		tBackLeft.follow(tFrontLeft);
		tBackRight.follow(tFrontRight);

		// Set up feedback sensors
		// Using CTRE_MagEncoder_Relative allows for relative ticks when encoder is zeroed out.
		// This allows us to measure the distance from any given point to any ending point.
		tFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PRIMARY_PID_LOOP, TIMEOUT_MS);
		tFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PRIMARY_PID_LOOP, TIMEOUT_MS);
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

	public void resetEncoders() {
    	tFrontLeft.getSensorCollection().setQuadraturePosition(0, TIMEOUT_MS);
    	tFrontRight.getSensorCollection().setQuadraturePosition(0, TIMEOUT_MS);
    }

	/**
	 * Stops the motors by zeroing the left and right Talons.
	 */
	public void stop() {
		tFrontLeft.set(ControlMode.Velocity, 0);
		tFrontRight.set(ControlMode.Velocity, 0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveArcade());
	}

	public double getLeftEncPositionInFeet() {
		double ticks = tFrontLeft.getSelectedSensorPosition(PRIMARY_PID_LOOP);
		//Convert encoder ticks to feet
		return ((Math.PI * WHEEL_DIAMETER_INCHES) / (MAG_ENCODER_TICKS_PER_REVOLUTION * GEAR_RATIO) * ticks) / 12;
	}

	public double getRightEncPositionInFeet() {
		double ticks = tFrontRight.getSelectedSensorPosition(PRIMARY_PID_LOOP);
		//Convert encoder ticks to feet
		return ((Math.PI * WHEEL_DIAMETER_INCHES) / (MAG_ENCODER_TICKS_PER_REVOLUTION * GEAR_RATIO) * ticks) / 12;
	}

	/**
     * <pre>s
	 * public double feetToEncoderTicks(double feet)
	 * </pre>
	 * Returns a value in ticks based on a certain value in feet using
	 * the Magnetic Encoder.
	 * @param feet The value in feet
	 * @return The value in ticks
     */
	public double feetToEncoderTicks(double feet) {
		return (MAG_ENCODER_TICKS_PER_REVOLUTION * GEAR_RATIO) / (Math.PI * WHEEL_DIAMETER_INCHES) * feet * 12.0;
	}

	public void pidWrite(double output) {
		tDrive.tankDrive(output, -output);
	}

	//  enable/disableTalonDrive methods are WIP.
	public void enableTalonDrive() {
		tFrontLeft.setSafetyEnabled(true);
		tFrontRight.setSafetyEnabled(true);
	}

	public void disableTalonDrive() {
		tFrontLeft.setSafetyEnabled(false);
		tFrontRight.setSafetyEnabled(false);

	}
}
