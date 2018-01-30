package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.util.MercMath;

import java.util.function.DoubleSupplier;

/**
 * Uses Talons and mag encoders to drive a set distance.
 */
public class DriveDistance extends Command {


    private final double PROPORTIONAL = .055;
    private final double INTEGRAL = 0;
    private final double DERIVATIVE = .05;

	private final double MOVE_THRESHOLD = 500;
	private final int ON_TARGET_MINIMUM_COUNT = 10;
    private int onTargetCount;

	protected double distance;
    private DoubleSupplier distanceSupplier;
	protected double percentVoltage; //Voltage is NOW from [-1, 1]


    /**
     * 
     * @param distance in inches
     * @param percentVoltage -1.0 to 1.0
     */
    public DriveDistance(double distance, double percentVoltage) {
    	requires(Robot.driveTrain);
    	this.distance = distance;
    	this.percentVoltage = percentVoltage;
    }

	public DriveDistance(DoubleSupplier distanceSupplier, double percentVoltage) {
        requires(Robot.driveTrain);
        this.percentVoltage = percentVoltage;
		this.distance = distanceSupplier.getAsDouble();
	}

    // Called just before this Command runs the first time
    protected void initialize() {
        if (distanceSupplier != null) {
            this.distance = distanceSupplier.getAsDouble();
        }

        Robot.driveTrain.resetEncoders();

		Robot.driveTrain.getLeft().config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);
		Robot.driveTrain.getRight().config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);

		Robot.driveTrain.configVoltage(0, percentVoltage);

         updateDistance();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
		boolean isFinished = false;

        double leftError = Robot.driveTrain.getLeft().getClosedLoopError(DriveTrain.PRIMARY_PID_LOOP);
        double rightError = Robot.driveTrain.getRight().getClosedLoopError(DriveTrain.PRIMARY_PID_LOOP);
		boolean isOnTarget = (Math.abs(rightError) < MOVE_THRESHOLD && Math.abs(leftError) < MOVE_THRESHOLD);

		if (isOnTarget) {
			onTargetCount++;
		} else {
			if (onTargetCount > 0) {
				onTargetCount = 0;
			} else {
				// we are definitely moving
			}
		}

		if (onTargetCount > ON_TARGET_MINIMUM_COUNT) {
			isFinished = true;
			onTargetCount = 0;
            System.out.println("DriveDistance ended");
		}

		return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    	Robot.driveTrain.resetEncoders();

    	//The voltage set on the Talons is global, so the talons must be reconfigured back to their original outputs.
		Robot.driveTrain.configVoltage(0, Robot.driveTrain.getTalonDrive().getMaxOutput());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("DriveDistance interrupted");
        Robot.driveTrain.configVoltage(0, Robot.driveTrain.getTalonDrive().getMaxOutput());
    }

    protected void updateDistance() {
        double endPosL, endPosR;
        //End position has to be calculated in initialize() because of the DistanceSupplier constructor rewriting the distance field.
        Robot.driveTrain.resetEncoders();
        endPosL = MercMath.inchesToEncoderTicks(distance);
        System.out.println(distance);
        // Per CTRE documentation, the encoder value need to increase when the Talon LEDs are green.
        // On Crossfire, the Talon LEDs are *red* when the robot is moving forward. For this reason, we need
        // to negate both endPosR and endPosL.
        // THIS MIGHT CHANGE on the 2018 robot!!
        endPosL = -endPosL;
        endPosR = endPosL;
        Robot.driveTrain.getLeft().set(ControlMode.Position, endPosL);
        Robot.driveTrain.getRight().set(ControlMode.Position, endPosR);
    }
}
