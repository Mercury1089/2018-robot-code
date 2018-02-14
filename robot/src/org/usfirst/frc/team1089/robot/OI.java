package org.usfirst.frc.team1089.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.RobotMap.DS_USB;
import org.usfirst.frc.team1089.robot.auton.AutonBuilder;
import org.usfirst.frc.team1089.robot.auton.AutonPosition;
import org.usfirst.frc.team1089.robot.auton.TestAutonBuilder;
import org.usfirst.frc.team1089.robot.commands.*;
import org.usfirst.frc.team1089.robot.subsystems.Claw;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.HistoryOriginator;
import org.usfirst.frc.team1089.util.ShuffleDash;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 *
 * <h3>CREATING BUTTONS</h3>
 * One type of button is a joystick button which is any button on a joystick.
 * You create one by telling it which joystick it's on and which button
 * number it is.
 *
 * <pre>
 * Joystick stick = new Joystick(port);
 * Button button = new JoystickButton(stick, buttonNumber);
 * </pre>
 *
 * There are a few additional built in buttons you can use. Additionally,
 * by subclassing Button you can create custom triggers and bind those to
 * commands the same as any other Button.
 *
 * <h3>TRIGGERING COMMANDS WITH BUTTONS</h3>
 * Once you have a button, it's trivial to bind it to a button in one of
 * three ways:
 * <ul>
 * <li>
 * Start the command when the button is pressed and let it run the command
 * until it is finished as determined by it's isFinished method.
 * <pre>button.whenPressed(new ExampleCommand());</pre>
 * </li>
 *
 * <li>
 * Run the command while the button is being held down and interrupt it once
 * the button is released.
 * <pre>button.whileHeld(new ExampleCommand());</pre>
 * </li>
 *
 * <li>
 * Start the command when the button is released and let it run the command
 * until it is finished as determined by it's isFinished method.
 * <pre>button.whenReleased(new ExampleCommand());</pre>
 * </li>
 * </ul>
 *
 */
public class OI {
	private final double DEADZONE = 0.30;
	private SendableChooser<AutonPosition> startingPosition;
	private static Logger log = LogManager.getLogger(OI.class);

	private Joystick leftStick, rightStick, gamepad;

	private JoystickButton left1, left2, left3, left4;
	private JoystickButton right1, right2, right3, right4, right5;
	private JoystickButton gamepad_a, gamepad_b, gamepad_x, gamepad_y, gamepad_rb, gamepad_lb;

	private UseClaw useClaw;

	private AutonBuilder autonBuilderLLL, autonBuilderLRL, autonBuilderRLR, autonBuilderRRR;

	private ShuffleDash shuffleboard;

	public OI() {
		leftStick = new Joystick(DS_USB.LEFT_STICK);
		rightStick = new Joystick(DS_USB.RIGHT_STICK);
		gamepad = new Joystick(DS_USB.GAMEPAD);

		// Left stick button binds
		left1 = new JoystickButton(leftStick, RobotMap.JOYSTICK_BUTTONS.BTN1);
		left1.whenPressed(new RotateRelative(90));
		left2 = new JoystickButton(leftStick, RobotMap.JOYSTICK_BUTTONS.BTN2);
		left2.whenPressed(new DriveWithLIDAR(4, .3));
		left3 = new JoystickButton(leftStick, RobotMap.JOYSTICK_BUTTONS.BTN3);
		left3.whenPressed(new DriveDistance(24.0, .12));
		left4 = new JoystickButton(leftStick, RobotMap.JOYSTICK_BUTTONS.BTN4);
		left4.whenPressed(new GetCube());

		// Right stick button binds
		right1 = new JoystickButton(rightStick,1);
		right1.whenPressed(new MoveOnPath("CubePickupSetupRight", MoveOnPath.Direction.BACKWARD));
		right2 = new JoystickButton(rightStick, 2);
		right2.whenPressed(new MoveOnPath("InitialSwitchBackRight", MoveOnPath.Direction.FORWARD));
		//right2.whenPressed(new TestAutonBuilder());
		//right2.whenPressed(new TestAutonBuilder());
		right3 = new JoystickButton(rightStick, 3);
		right3.whenPressed(new GetCube());

		DriveDistance dd = new DriveDistance(24, 0.2);
		DriveWithLIDAR dwl = new DriveWithLIDAR(20, 0.2);

		right4 = new JoystickButton(rightStick, 4);
		right4.whenPressed(dd);
		right5 = new JoystickButton(rightStick, 5);
		right5.whenPressed(new DriveDistance(dd, HistoryOriginator.HistoryTreatment.REVERSE, 0.2));

		// Gamepad button binds
		gamepad_a = new JoystickButton(gamepad, RobotMap.GAMEPAD_BUTTONS.A);
		gamepad_b = new JoystickButton(gamepad, RobotMap.GAMEPAD_BUTTONS.B);
		gamepad_x = new JoystickButton(gamepad, RobotMap.GAMEPAD_BUTTONS.X);
		gamepad_y = new JoystickButton(gamepad, RobotMap.GAMEPAD_BUTTONS.Y);
		//gamepad_a.whenPressed(new UseElevator(Elevator.ELEVATOR_STATE.STOP));
		//gamepad_x.whenPressed(new UseElevator(Elevator.ELEVATOR_STATE.SWITCH));
		gamepad_a.whenPressed(new DriveArcade());
		gamepad_x.whenPressed(new DriveTank());
		gamepad_b.whenPressed(new UseElevator(Elevator.ELEVATOR_STATE.SCALE_HIGH));
		gamepad_y.whenPressed(new UseElevator(Elevator.ELEVATOR_STATE.SCALE_LOW));
		gamepad_lb = new JoystickButton(gamepad, RobotMap.GAMEPAD_BUTTONS.LB);
		gamepad_lb.whenPressed(new UseClaw(Claw.ClawState.GRAB));
		gamepad_rb = new JoystickButton(gamepad, RobotMap.GAMEPAD_BUTTONS.RB);
		gamepad_rb.whenPressed(new UseClaw(Claw.ClawState.EJECT));

		log.info("OI initialized");

		shuffleboard = new ShuffleDash();


	}
	
	/**
	 * Gets the y-value for driving forwards/backwards.
	 * A deadzone is applied to this value.
	 * 
	 * @param id the device ID of the axis.
	 * @return y-axis of joystick, deadzone applied.
	 */
	public double getY(int id) {
		double val;
		
		switch(id) {
			case DS_USB.LEFT_STICK:
				val = leftStick.getY();
				break;
			case DS_USB.RIGHT_STICK:
				val = rightStick.getY();
				break;
			default:
				val = -1;
		}
		
		return applyDeadzone(val);
	}
	
	/**
	 * Gets the x-value for rotating right/left.
	 * A deadzone is applied to this value.
	 * 
	 * @param id the device ID of the axis.
	 * @return x-axis of joystick, deadzone applied.
	 */
	public double getX(int id) {
		double val;
		
		switch(id) {
			case DS_USB.LEFT_STICK:
				val = leftStick.getX();
				break;
			case DS_USB.RIGHT_STICK:
				val = rightStick.getX();
				break;
			default:
				val = -1;
		}
		
		return applyDeadzone(val);
	}
	
	/**
	 * Applies the deadzone to a given value.
	 * 
	 * @param val value to apply a deadzone to.
	 * @return {@code val} if |{@code val}| >= 0.1, 0 otherwise.
	 */
	private double applyDeadzone(double val) {
		return Math.abs(val) >= DEADZONE ? val : 0;
	}

	private void buildAuton() {

	}
}
