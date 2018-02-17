package org.usfirst.frc.team1089.robot.sensors;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1089.util.BoundingBox;


public class Vision extends Subsystem {
	private PixyI2C pixyCam;

	public Vision() {
		// Open a pipeline to a Pixy camera.
		pixyCam = new PixyI2C();
	}

	@Override
	public void periodic() {
		BoundingBox box = pixyCam.getTarget();

		SmartDashboard.putNumber("Pixy Cam: Displacement", pixyCam.pidGet());

		if (box != null) {
			SmartDashboard.putNumber("Pixy Cam: Center X", box.getX());
			SmartDashboard.putNumber("Pixy Cam: Width", box.getWidth());
		}
	}

	public PixyI2C getPixyCam() {
		return pixyCam;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}

