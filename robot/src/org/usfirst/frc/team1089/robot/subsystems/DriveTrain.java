package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.DriveWithJoysticks;
import org.usfirst.frc.team1089.util.MercMath;
import org.usfirst.frc.team1089.util.TalonDrive;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

/**
 * Subsystem that encapsulates the drive train.
 * This contains the {@link TalonDrive} needed to drive manually
 * using the Talons.
 */
public class DriveTrain extends Subsystem implements PIDOutput {
    private Logger log = LogManager.getLogger(DriveTrain.class);
    public static final int TIMEOUT_MS = 10;
    public static final int SLOT_0 = 0;
    public static final int PRIMARY_PID_LOOP = 0;

    public static final double MAX_SPEED = 1.0;
    public static final double MIN_SPEED = .65;

    private WPI_TalonSRX tMasterLeft, tMasterRight;
    private BaseMotorController vFollowerLeft, vFollowerRight;

    private TalonDrive tDrive;
    // private NavX navX;
    private ADXRS450_Gyro gyroSPI;

	public static final int MAG_ENCODER_TICKS_PER_REVOLUTION = 4096;
	public static final double GEAR_RATIO;
    public static final double MAX_RPM;
    public static final double WHEEL_DIAMETER_INCHES;
    public static final DriveTrainSettings.DriveTrainLayout LAYOUT;

    static {
        LAYOUT = DriveTrainSettings.getControllerLayout();
        GEAR_RATIO = DriveTrainSettings.getGearRatio();
        MAX_RPM = DriveTrainSettings.getMaxRPM();
        WHEEL_DIAMETER_INCHES = DriveTrainSettings.getWheelDiameter();
	}

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

        // At this point it's based on what the layout is
        switch(LAYOUT) {
            case LEGACY:
                vFollowerLeft = new WPI_TalonSRX(bl);
                vFollowerRight = new WPI_TalonSRX(br);
                break;
			case DEFAULT:
			default:
				vFollowerLeft = new WPI_VictorSPX(bl);
				vFollowerRight = new WPI_VictorSPX(br);
				break;
        }

        //Initialize the gyro that is currently on the robot. Comment out the initialization of the one not in use.
        // navX = new NavX(SerialPort.Port.kUSB1);
        gyroSPI = new ADXRS450_Gyro();

        //Account for motor orientation.
        tMasterLeft.setInverted(true);
        vFollowerLeft.setInverted(true);
        tMasterRight.setInverted(false);
        vFollowerRight.setInverted(false);

        setNeutralMode(NeutralMode.Brake);

        //Account for encoder orientation.
        tMasterLeft.setSensorPhase(true);
        tMasterRight.setSensorPhase(true);

        tDrive = new TalonDrive(tMasterLeft, tMasterRight);

        // Set follower control on back talons. Use follow() instead of ControlMode.Follower so that Talons can follow Victors and vice versa.
        vFollowerLeft.follow(tMasterLeft);
        vFollowerRight.follow(tMasterRight);

        // Set up feedback sensors
        // Using CTRE_MagEncoder_Relative allows for relative ticks when encoder is zeroed out.
        // This allows us to measure the distance from any given point to any ending point.
        tMasterLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PRIMARY_PID_LOOP, TIMEOUT_MS);
        tMasterRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PRIMARY_PID_LOOP, TIMEOUT_MS);

        configVoltage(0, DriveTrainSettings.getMaxOutput());
        setMaxOutput(DriveTrainSettings.getMaxOutput());
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
        setDefaultCommand(new DriveWithJoysticks(DriveWithJoysticks.DriveType.ARCADE));
    }

    /**
     * Sets both of the front talons to have a forward output of nominalOutput and peakOutput with the reverse output setClawState to the negated outputs.
     *
     * @param nominalOutput The desired nominal voltage output of the left and right talons, both forward and reverse.
     * @param peakOutput    The desired peak voltage output of the left and right talons, both forward and reverse
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
     * Gets the gyro being used by the drive train.
     *
     * @return The gyro, either the NavX or Analog Gyro, currently in use on the robot
     */
    public Gyro getGyro() {
       /* if (navX != null) {
            return navX;
        } else */
        if (gyroSPI != null) {
            return gyroSPI;
        } else {
            return null;
        }
    }

    public int getLeftEncPositionInTicks() {
        return tMasterLeft.getSelectedSensorPosition(PRIMARY_PID_LOOP);
    }

    public double getRightEncPositionInTicks() {
        return tMasterRight.getSelectedSensorPosition(PRIMARY_PID_LOOP);
    }

    public double getLeftEncPositionInFeet() {
        return MercMath.getEncPosition(getLeftEncPositionInTicks());
    }

    public double getRightEncPositionInFeet() {
        return MercMath.getEncPosition(getRightEncPositionInTicks());
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

    public double getFeedForward() {
        return MercMath.calculateFeedForward(MAX_RPM);
    }

    public void pidWrite(double output) {
        tDrive.tankDrive(output, -output);
    }

    public void setMaxOutput(double maxOutput) {
        tDrive.setMaxOutput(maxOutput);
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        tMasterLeft.setNeutralMode(neutralMode);
        tMasterRight.setNeutralMode(neutralMode);
        vFollowerLeft.setNeutralMode(neutralMode);
        vFollowerRight.setNeutralMode(neutralMode);
    }
}
