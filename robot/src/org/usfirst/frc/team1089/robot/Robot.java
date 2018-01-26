
package org.usfirst.frc.team1089.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

import org.usfirst.frc.team1089.util.Config;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import org.usfirst.frc.team1089.robot.RobotMap.*;
import org.usfirst.frc.team1089.robot.subsystems.*;

import org.usfirst.frc.team1089.util.NavX;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static DriveTrain driveTrain;
	public static Manipulator manipulator;
	public static OI oi;
	public static PDP pdp;
	public static final Config.RobotType robotType = Config.RobotType.SPEEDY_BOI;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
	    driveTrain = new DriveTrain(
	            CAN.DRIVETRAIN_ML,
                CAN.DRIVETRAIN_MR,
                CAN.DRIVETRAIN_SL,
                CAN.DRIVETRAIN_SR
                );

		pdp = new PDP();

		manipulator = new Manipulator(CAN.CANIFIER, PWM.LIDAR);

		Robot.driveTrain.resetEncoders();

		// OI NEEDS to be constructed as the last line for everything to work.
		oi = new OI();
	}


	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		// Keep this line; it's needed for commands.
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		// Keep this line; it's needed for commands.
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		// Keep this line; it's needed for commands.
		Scheduler.getInstance().run();
	}



}
