
package org.usfirst.frc.team1089.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.RobotMap.AIO;
import org.usfirst.frc.team1089.robot.RobotMap.CAN;
import org.usfirst.frc.team1089.robot.RobotMap.PWM;
import org.usfirst.frc.team1089.robot.auton.*;
import org.usfirst.frc.team1089.robot.auton.TaskConfig.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.robot.subsystems.PDP;
import org.usfirst.frc.team1089.util.GameData;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;
import org.usfirst.frc.team1089.util.config.ManipulatorSettings;
import org.usfirst.frc.team1089.util.config.SensorsSettings;

import java.util.Map;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private static Logger log = LogManager.getLogger(Robot.class);

	// Subsystems
	public static DriveTrain driveTrain;
	public static PDP pdp;
    public static Claw claw;
    public static Elevator elevator;

	public static Map<String, AutonTrajectoryGenerator.TrajectoryPair> autonTrajectories;
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
		int matchNumber = DriverStation.getInstance().getMatchNumber();

		log.info("-------------------ROBOT INIT-------------------");

		elevator = new Elevator(CAN.ELEVATOR_M, CAN.ELEVATOR_S);

	    driveTrain = new DriveTrain(
			CAN.DRIVETRAIN_ML,
			CAN.DRIVETRAIN_MR,
			CAN.DRIVETRAIN_SL,
			CAN.DRIVETRAIN_SR
		);

		driveTrain.resetEncoders();

		pdp = new PDP();

		claw = new Claw(AIO.ULTRASONIC, CAN.CANIFIER, PWM.LIDAR, CAN.LEFT_CLAW_LEADER, CAN.RIGHT_CLAW_FOLLOWER);

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
		log.info("-------------------AUTON INIT-------------------");
		GameData.updateGameData();

		try {
			NetworkTable
                    rootTable = NetworkTableInstance.getDefault().getTable("AutonConfiguration"),
                    lllTable = rootTable.getSubTable("LLL"),
                    lrlTable = rootTable.getSubTable("LRL"),
                    rlrTable = rootTable.getSubTable("RLR"),
                    rrrTable = rootTable.getSubTable("RRR");

			AutonPosition startingPosition = AutonPosition.RIGHT;
			NetworkTableValue startPosValue = rootTable.getEntry("startingPosition").getValue();

			// This value has the possibility to not exist in the table.
			// If it DOES exist, however, it will have a table type of kString.
			// We can pull the value from it then.
			if (startPosValue.getType() == NetworkTableType.kString) {
                startingPosition = AutonPosition.fromString(startPosValue.getString());
            }

			AutonTask[] lllTasks = AutonTask.arrayFromString(lllTable.getEntry("tasks").getValue().getStringArray());
			AutonTask[] lrlTasks = AutonTask.arrayFromString(lrlTable.getEntry("tasks").getValue().getStringArray());
			AutonTask[] rlrTasks = AutonTask.arrayFromString(rlrTable.getEntry("tasks").getValue().getStringArray());
			AutonTask[] rrrTasks = AutonTask.arrayFromString(rrrTable.getEntry("tasks").getValue().getStringArray());

			ScoringSide[] lllSides = ScoringSide.arrayFromString(lllTable.getEntry("sides").getValue().getStringArray());
			ScoringSide[] lrlSides = ScoringSide.arrayFromString(lrlTable.getEntry("sides").getValue().getStringArray());
			ScoringSide[] rlrSides = ScoringSide.arrayFromString(rlrTable.getEntry("sides").getValue().getStringArray());
			ScoringSide[] rrrSides = ScoringSide.arrayFromString(rrrTable.getEntry("sides").getValue().getStringArray());

			FieldSide lllFieldSide = FieldSide.fromString(lllTable.getEntry("fieldSide").getValue().getString());
			FieldSide lrlFieldSide = FieldSide.fromString(lrlTable.getEntry("fieldSide").getValue().getString());
			FieldSide rlrFieldSide = FieldSide.fromString(rlrTable.getEntry("fieldSide").getValue().getString());
			FieldSide rrrFieldSide = FieldSide.fromString(rrrTable.getEntry("fieldSide").getValue().getString());


			autonBuilderLLL = new AutonBuilder(startingPosition, lllFieldSide, lllTasks, lllSides);
			autonBuilderLRL = new AutonBuilder(startingPosition, lrlFieldSide, lrlTasks, lrlSides);
			autonBuilderRLR = new AutonBuilder(startingPosition, rlrFieldSide, rlrTasks, rlrSides);
			autonBuilderRRR = new AutonBuilder(startingPosition, rrrFieldSide, rrrTasks, rrrSides);

			switch (GameData.getParsedString()) {
                case "LLL":
                    autonCommand = new AutonCommand(autonBuilderLLL);
                    break;
                case "RRR":
                    autonCommand = new AutonCommand(autonBuilderRRR);
                    break;
                case "LRL":
                    autonCommand = new AutonCommand(autonBuilderLRL);
                    break;
                case "RLR":
                    autonCommand = new AutonCommand(autonBuilderRLR);
                    break;
                default:
                    return;
            }
		} catch (Exception e) {
			log.warn("AUTON COULD NOT INIT! DEFAULTING TO AUTO-LINE!");
			log.catching(e);
			autonCommand = new AutonCommand();
		}

		if (autonCommand != null) {
			autonCommand.start();
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
		log.info("-------------------TELEOP INIT-------------------");
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