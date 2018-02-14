
package org.usfirst.frc.team1089.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;
import org.usfirst.frc.team1089.robot.RobotMap.PWM;
import org.usfirst.frc.team1089.robot.auton.AutonBuilder;
import org.usfirst.frc.team1089.robot.auton.AutonCommand;
import org.usfirst.frc.team1089.robot.sensors.CameraVision;
import org.usfirst.frc.team1089.robot.subsystems.*;
import org.usfirst.frc.team1089.util.GameData;
import org.usfirst.frc.team1089.util.config.*;
import org.usfirst.frc.team1089.robot.sensors.Vision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Subsystems
	public static DriveTrain driveTrain;
	public static PDP pdp;
	public static CameraVision camera;
    public static Claw claw;
    public static Elevator elevator;
    public static Vision vision;
	private static Logger log = LogManager.getLogger(Robot.class);

	private AutonCommand autonCommand;
	private AutonBuilder autonBuilderLLL, autonBuilderRRR, autonBuilderRLR, autonBuilderLRL;

	public static OI oi;

    static {
		DriveTrainSettings.initialize();
		SensorsSettings.initialize();
		ManipulatorSettings.initialize();
    }

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

	    driveTrain.getTalonDrive().setMaxOutput(DriveTrainSettings.getMaxOutput());

		driveTrain.resetEncoders();

		pdp = new PDP();

		camera = new CameraVision();
		claw = new Claw(CAN.CANIFIER, PWM.LIDAR, CAN.TALON_CLAW_LEADER, CAN.TALON_CLAW_FOLLOWER);
		// elevator = new Elevator(CAN.TALON_ELEVATOR);
		vision = new Vision();

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
		switch (DriverStation.getInstance().getGameSpecificMessage()) {
			case "LLL":
				autonCommand = new AutonCommand(autonBuilderLLL);
			case "RRR":
				autonCommand = new AutonCommand(autonBuilderRRR);
			case "LRL":
				autonCommand = new AutonCommand(autonBuilderLRL);
			case "RLR":
				autonCommand = new AutonCommand(autonBuilderRLR);
			default:

		}
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