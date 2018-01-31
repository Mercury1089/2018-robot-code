/**
 * 
 */
package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.command.InstantCommand;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.util.MercMath;

import java.io.File;

/**
 * Use motion profiling to move on a specified path
 */
public class MoveOnProfile extends Command {


	private TalonSRX leftFront;
	private TalonSRX rightFront;

    private final double PROPORTIONAL = .1;
    private final double INTEGRAL = 0;
    private final double DERIVATIVE = .05;
    private Trajectory trajectoryR, trajectoryL;
    private int counter = 0;

    TrajectoryPoint trajPointL, trajPointR;

	public MoveOnProfile(File trajectoryFileR, File trajectoryFileL) {
        requires(Robot.driveTrain);
        leftFront = Robot.driveTrain.getLeft();
        rightFront = Robot.driveTrain.getRight();
        try {
            trajectoryR = Pathfinder.readFromFile(trajectoryFileR);
            trajectoryL = Pathfinder.readFromFile(trajectoryFileL);
        } catch (Exception e) {
            System.out.println("Trajectory file could not be read. Check the path.");
            e.printStackTrace();
        }
    }

	
	//Called just before this Command runs for the first time. 
	protected void initialize() {
	    /*Robot.driveTrain.getLeft().setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Robot.driveTrain.TIMEOUT_MS);
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
        */

        Robot.driveTrain.getLeft().config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kP(DriveTrain.SLOT_0, PROPORTIONAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kI(DriveTrain.SLOT_0, INTEGRAL, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kD(DriveTrain.SLOT_0, DERIVATIVE, DriveTrain.TIMEOUT_MS);

        Robot.driveTrain.getLeft().config_kF(DriveTrain.SLOT_0, Robot.driveTrain.getFeedForward(), DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kF(DriveTrain.SLOT_0, Robot.driveTrain.getFeedForward(), DriveTrain.TIMEOUT_MS);

        leftFront.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value); //Methinks this is the factor by which max velocity is decreased.
        rightFront.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
        leftFront.clearMotionProfileTrajectories();
        rightFront.clearMotionProfileTrajectories();
	}


	//Called repeatedly when this Command is scheduled to run.
	protected void execute() {
	    int zeroVelCount = 0;
        trajPointL = new TrajectoryPoint();
        trajPointR = new TrajectoryPoint();
        double currentPosL = trajectoryL.segments[counter].position;
        double currentPosR = trajectoryR.segments[counter].position;
        double velocityL = trajectoryL.segments[counter].velocity;
        double velocityR = trajectoryR.segments[counter].velocity;
        /* for each point, fill our structure and pass it to API */
        trajPointL.position = MercMath.feetToEncoderTicks(currentPosL); //Convert Revolutions to Units
        trajPointR.position = MercMath.feetToEncoderTicks(currentPosR);
        trajPointL.velocity = MercMath.revsPerMinuteToTicksPerTenth(velocityL); //Convert RPM to Units/100ms
        trajPointR.velocity = MercMath.revsPerMinuteToTicksPerTenth(velocityR);
        trajPointL.headingDeg = 0; /* future feature - not used in this example*/
        trajPointR.headingDeg = 0;
        trajPointL.profileSlotSelect0 = DriveTrain.SLOT_0; /* which set of gains would you like to use [0,3]? */
        trajPointR.profileSlotSelect0 = DriveTrain.SLOT_0;
        //point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */ //TODO figure this out
        trajPointL.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;
        trajPointR.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;
        trajPointL.zeroPos = false;
        if (counter == 0)
            trajPointL.zeroPos = true; /* set this to true on the first point */

        trajPointL.isLastPoint = false;
        if (trajectoryL.segments.length == counter + 1) {
            trajPointL.isLastPoint = true;
        }
        if (counter == 0)
            trajPointR.zeroPos = true; /* set this to true on the first point */

        trajPointR.isLastPoint = false;
        if (trajectoryR.segments.length == counter + 1) {
            trajPointR.isLastPoint = true;
        }

        Robot.driveTrain.getLeft().pushMotionProfileTrajectory(trajPointL);
        Robot.driveTrain.getRight().pushMotionProfileTrajectory(trajPointR);
    }


	//Make this return true when this command no longer needs to run execute()
	protected boolean isFinished() {
	    if(trajPointL.isLastPoint && trajPointR.isLastPoint) {
	        return true;
        }
        return false;
    }


	//Called once after isFinished() returns true
	protected void end() {
		Robot.driveTrain.getLeft().setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, Robot.driveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, Robot.driveTrain.TIMEOUT_MS);
        leftFront.set(ControlMode.Velocity, 0);
        rightFront.set(ControlMode.Velocity, 0);
    }


}
