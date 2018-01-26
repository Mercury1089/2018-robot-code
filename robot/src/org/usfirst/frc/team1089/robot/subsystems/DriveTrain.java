package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;
import org.usfirst.frc.team1089.robot.commands.DriveArcade;
import org.usfirst.frc.team1089.robot.commands.DriveTank;
import org.usfirst.frc.team1089.util.Config;
import org.usfirst.frc.team1089.util.NavX;
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

	private WPI_TalonSRX tMasterLeft, tMasterRight;
	private BaseMotorController vSlaveLeft, vSlaveRight;

	private TalonDrive tDrive;
    private NavX navX;
    private AnalogGyro analogGyro;

	public static final double WHEEL_DIAMETER_INCHES = 4.0 ;
	public static final int MAG_ENCODER_TICKS_PER_REVOLUTION = 4096; //TODO Old Crossfire values
	public static final double GEAR_RATIO = 1.0;                      //TODO Old Crossfire values
    public static final double MAX_RPM_CROSSFIRE = 454.1;
    public static final double MAX_RPM = 0;

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
		tMasterLeft = new WPI_TalonSRX(fl);
		tMasterRight = new WPI_TalonSRX(fr);
        switch(Robot.robotType) {
            case THE_QUESTIONNAIRE:
                vSlaveLeft = new WPI_VictorSPX(bl);
                vSlaveRight = new WPI_VictorSPX(br);
                break;
            case CROSS_SUPA_HOT_FIYA:
            case PROTO_BOI:
                vSlaveLeft = new WPI_TalonSRX(bl);
                vSlaveRight = new WPI_TalonSRX(br);
                break;
        }

		//Initialize the gyro that is currently on the robot. Comment out the initialization of the one not in use.
        navX = new NavX(SerialPort.Port.kUSB1);
        //analogGyro = new AnalogGyro(Port#);


		//Account for motor orientation.
		tMasterLeft.setInverted(true);
		vSlaveLeft.setInverted(true);
		tMasterRight.setInverted(false);
		vSlaveRight.setInverted(false);

		tMasterLeft.setNeutralMode(NeutralMode.Brake);
		vSlaveLeft.setNeutralMode(NeutralMode.Brake);
		tMasterRight.setNeutralMode(NeutralMode.Brake);
		vSlaveRight.setNeutralMode(NeutralMode.Brake);

		//Account for encoder orientation.
		tMasterLeft.setSensorPhase(true);
		tMasterRight.setSensorPhase(true);

		tDrive = new TalonDrive(tMasterLeft, tMasterRight);

		// Set follower control on back talons. Use follow() instead of ControlMode.Follower so that Talons can follow Victors and vice versa.
		vSlaveLeft.follow(tMasterLeft);
		vSlaveRight.follow(tMasterRight);

		// Set up feedback sensors
		// Using CTRE_MagEncoder_Relative allows for relative ticks when encoder is zeroed out.
		// This allows us to measure the distance from any given point to any ending point.
		tMasterLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PRIMARY_PID_LOOP, TIMEOUT_MS);
		tMasterRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PRIMARY_PID_LOOP, TIMEOUT_MS);
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
			case CAN.TALON_DRIVETRAIN_ML:
				return tMasterLeft;
			case CAN.TALON_DRIVETRAIN_MR:
				return tMasterRight;
			default: // Not a drivetrain Talon!
				return null;
		}
	}

    public BaseMotorController getBack(int id) {
        switch(id) {
            case CAN.VICTOR_DRIVETRAIN_SR:
                return vSlaveRight;
            case CAN.VICTOR_DRIVETRAIN_SL:
                return vSlaveLeft;
            default:
                return null;
        }
    }

    public TalonSRX getLeft() {
		return tMasterLeft;
	}

	public TalonSRX getRight() {
		return tMasterRight;
	}

	public TalonDrive getTalonDrive() {
		return tDrive;
	}

	public void resetEncoders() {
    	tMasterLeft.getSensorCollection().setQuadraturePosition(0, TIMEOUT_MS);
    	tMasterRight.getSensorCollection().setQuadraturePosition(0, TIMEOUT_MS);
    }

	/**
	 * Stops the motors by zeroing the left and right Talons.
	 */
	public void stop() {
		tMasterLeft.set(ControlMode.Velocity, 0);
		tMasterRight.set(ControlMode.Velocity, 0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveArcade());
	}

	/**
	 * Sets both of the front talons to have a forward output of nominalOutput and peakOutput with the reverse output set to the negated outputs.
	 * @param nominalOutput The desired nominal voltage output of the left and right talons, both forward and reverse.
	 * @param peakOutput The desired peak voltage output of the left and right talons, both forward and reverse
	 */
	public void configVoltage(double nominalOutput, double peakOutput) {
		tMasterLeft.configNominalOutputForward(nominalOutput, TIMEOUT_MS);
		tMasterLeft.configNominalOutputReverse(-nominalOutput, TIMEOUT_MS);
		tMasterLeft.configPeakOutputForward(peakOutput, TIMEOUT_MS);
		tMasterLeft.configPeakOutputReverse(-peakOutput, TIMEOUT_MS);
		tMasterRight.configNominalOutputForward(nominalOutput, TIMEOUT_MS);
		tMasterRight.configNominalOutputReverse(-nominalOutput, TIMEOUT_MS);
		tMasterRight.configPeakOutputForward(peakOutput, TIMEOUT_MS);
		tMasterRight.configPeakOutputReverse(-peakOutput, TIMEOUT_MS);
	}

    /**
     * @return The gyro, either the NavX or Analog Gyro, currently in use on the robot.
     */
    public Gyro getGyro() {
        if (navX != null) {
            return navX;
        } else if (analogGyro != null){
            return analogGyro;
        } else {
            return null;
        }
    }

	public double getLeftEncPositionInFeet() {
		double ticks = tMasterLeft.getSelectedSensorPosition(PRIMARY_PID_LOOP);
		//Convert encoder ticks to feet
		return ((Math.PI * WHEEL_DIAMETER_INCHES) / (MAG_ENCODER_TICKS_PER_REVOLUTION * GEAR_RATIO) * ticks) / 12;
	}

	public double getRightEncPositionInFeet() {
		double ticks = tMasterRight.getSelectedSensorPosition(PRIMARY_PID_LOOP);
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

	public double inchesToEncoderTicks(double inches) {
        return (inches / (Math.PI * WHEEL_DIAMETER_INCHES)) * MAG_ENCODER_TICKS_PER_REVOLUTION;
    }

    /**
     * TODO document this
     * @param ticksPerTenthSecond
     * @return revs per minute
     */
    public double ticksPerTenthToRevsPerMinute(double ticksPerTenthSecond) {
	    return ticksPerTenthSecond / MAG_ENCODER_TICKS_PER_REVOLUTION * 600;
    }

    public double getFeedForward() {
        final int MAX_MOTOR_OUTPUT = 1023;
        final double NATIVE_UNITS_PER_100 = MAX_RPM * 1/600 * MAG_ENCODER_TICKS_PER_REVOLUTION;
        return MAX_MOTOR_OUTPUT/NATIVE_UNITS_PER_100;
    }

	public void pidWrite(double output) {
		tDrive.tankDrive(output, -output);
	}

}
