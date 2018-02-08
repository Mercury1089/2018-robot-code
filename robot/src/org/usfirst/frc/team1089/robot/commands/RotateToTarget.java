package org.usfirst.frc.team1089.robot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.sensors.PixyCam;

/**
 * Turns the robot towards a target found with a camera
 */
public class RotateToTarget extends RotateRelative {
	private static Logger log = LogManager.getLogger(RotateToTarget.class);
	private final PixyCam SRC;

	public RotateToTarget() {
		super(0);

		requires(Robot.vision);
		SRC = Robot.vision.getPixyCam();

        log.info("RotateRelative constructed");
    }

	@Override
	protected double returnPIDInput() {
		return SRC.getDisplacement();
	}

	@Override
	protected void usePIDOutput(double output) {
		if (getPIDController().onTarget()) {
			log.info("Output before: " + output);
			output = 0;
		} else if (Math.abs(output) > 1) {
			output = Math.signum(output);
		}

		Robot.driveTrain.pidWrite(-output);
	}
}
