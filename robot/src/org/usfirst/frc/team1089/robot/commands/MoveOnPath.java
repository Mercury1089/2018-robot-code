/**
 * 
 */
package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.Notifier;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import org.usfirst.frc.team1089.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.util.MercMath;

import java.io.File;

/**
 * Use motion profiling to move on a specified path
 */
public class MoveOnPath extends Command {
	private TalonSRX left;
	private TalonSRX right;

    private final double PROPORTIONAL = .1;
    private final double INTEGRAL = 0;
    private final double DERIVATIVE = .05;
    private Trajectory trajectoryR, trajectoryL;
    private int counter = 0;

    private MotionProfileStatus statusLeft, statusRight;
    private Notifier trajectoryProcessor = new Notifier(null);

    private int state = 0;

    /**
     * Creates this command using the file prefix to determine
     * the files to load.
     *
     * @param prefix prefix to file names, file names
     *        {@code <prefix>_left.csv, <prefix>_right.csv}
     */
	public MoveOnPath(String prefix) {
        requires(Robot.driveTrain);
        left = Robot.driveTrain.getLeft();
        right = Robot.driveTrain.getRight();

        trajectoryR = Pathfinder.readFromCSV(new File("/home/lvuser/" + prefix + "_left.csv"));
        trajectoryL = Pathfinder.readFromCSV(new File("/home/lvuser/" + prefix + "_right.csv"));

        trajectoryProcessor.setHandler(() -> {
            left.processMotionProfileBuffer();
            right.processMotionProfileBuffer();

            System.out.println("Processing!");
        });

        statusLeft = new MotionProfileStatus();
        statusRight = new MotionProfileStatus();
	}

	
	//Called just before this Command runs for the first time. 
	protected void initialize() {
	    /*left.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Robot.driveTrain.TIMEOUT_MS);
        right.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Robot.driveTrain.TIMEOUT_MS);

        //TODO Put different numbers. These are random numbers. Discuss later.
        left.configMotionCruiseVelocity(15000, Robot.driveTrain.TIMEOUT_MS);
        right.configMotionCruiseVelocity(15000, Robot.driveTrain.TIMEOUT_MS);

        left.configMotionAcceleration(6000, Robot.driveTrain.TIMEOUT_MS);
        left.configMotionAcceleration(6000, Robot.driveTrain.TIMEOUT_MS);


        Robot.driveTrain.configVoltage(0, .5);

        left.config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        right.config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        left.config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        right.config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        left.config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);
        right.config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);
        */

        left.config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        right.config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        left.config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        right.config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        left.config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);
        right.config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);

        left.config_kF(DriveTrain.SLOT_0, Robot.driveTrain.getFeedForward(), DriveTrain.TIMEOUT_MS);
        right.config_kF(DriveTrain.SLOT_0, Robot.driveTrain.getFeedForward(), DriveTrain.TIMEOUT_MS);

        //Methinks this is the factor by which max velocity is decreased.
        left.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        right.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);

        left.changeMotionControlFramePeriod(10);
        right.changeMotionControlFramePeriod(10);

        // Clear the trajectory buffer
        left.clearMotionProfileTrajectories();
        right.clearMotionProfileTrajectories();

        // Start processing
        trajectoryProcessor.startPeriodic(0.005);

        // Reset counter
        counter = 0;
	}

	//Called repeatedly when this Command is scheduled to run.
	protected void execute() {
	    switch (state) {
            case 0: // Fill buffer
                int zeroVelCount = 0;
                double currentPosL = trajectoryL.segments[counter].position;
                double currentPosR = trajectoryR.segments[counter].position;
                double velocityL = trajectoryL.segments[counter].velocity;
                double velocityR = trajectoryR.segments[counter].velocity;
                boolean isLastPointL = trajectoryL.segments.length == counter + 1;
                boolean isLastPointR = trajectoryR.segments.length == counter + 1;
                boolean isZero = counter == 0;

                TrajectoryPoint trajPointL = new TrajectoryPoint();
                TrajectoryPoint trajPointR = new TrajectoryPoint();

                // For each point, fill our structure and pass it to API
                trajPointL.position = MercMath.feetToEncoderTicks(currentPosL); //Convert Revolutions to Units
                trajPointR.position = MercMath.feetToEncoderTicks(currentPosR);
                trajPointL.velocity = MercMath.revsPerMinuteToTicksPerTenth(velocityL); //Convert RPM to Units/100ms
                trajPointR.velocity = MercMath.revsPerMinuteToTicksPerTenth(velocityR);
                /*trajPointL.headingDeg = 0; *//* future feature - not used in this example*//*
                trajPointR.headingDeg = 0;*/
                trajPointL.profileSlotSelect0 = DriveTrain.SLOT_0; /* which set of gains would you like to use [0,3]? */
                trajPointR.profileSlotSelect0 = DriveTrain.SLOT_0;
                // point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
                // TODO figure this out
                trajPointL.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;
                trajPointR.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;

                // Set these to true on the first point
                trajPointL.zeroPos = isZero;
                trajPointR.zeroPos = isZero;

                // Set these to true on the last point
                trajPointL.isLastPoint = isLastPointL;
                trajPointR.isLastPoint = isLastPointR;

                left.pushMotionProfileTrajectory(trajPointL);
                right.pushMotionProfileTrajectory(trajPointR);
                counter++;

                // Once the buffer is filled
                if (isLastPointL && isLastPointR)
                    state = 1;
                break;
            case 1: // Process?
                left.getMotionProfileStatus(statusLeft);
                right.getMotionProfileStatus(statusRight);

                if (statusLeft.btmBufferCnt >= 10 && statusRight.btmBufferCnt >= 10) {
                    left.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
                    right.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);

                    System.out.println("hecking");
                    state = 2;
                }
                break;
            case 2: // Debug
                left.getMotionProfileStatus(statusLeft);
                right.getMotionProfileStatus(statusRight);
                break;
        }
    }


	//Make this return true when this command no longer needs to run execute()
	protected boolean isFinished() {
	    if (state == 2)
            return statusLeft.activePointValid && statusLeft.isLast && statusRight.activePointValid && statusRight.isLast;

	    return false;
	}


	//Called once after isFinished() returns true
    @Override
	protected void end() {
	    System.out.println("Stop processor");
	    trajectoryProcessor.stop();

        left.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        right.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);

		left.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, Robot.driveTrain.TIMEOUT_MS);
        right.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, Robot.driveTrain.TIMEOUT_MS);
        Robot.driveTrain.stop();

        System.out.println("MoveOnPath finished");
    }
}
