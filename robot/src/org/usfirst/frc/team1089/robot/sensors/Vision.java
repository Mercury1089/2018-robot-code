package org.usfirst.frc.team1089.robot.sensors;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1089.robot.Robot;


public class Vision extends Subsystem {
	private PixyCam pixyCam;

	public Vision() {
		// Open a pipeline to a Pixy camera.
		pixyCam = new PixyCam(0);
	}

	@Override
	public void periodic() {
		try {
			pixyCam.getBoxes(1000);
			SmartDashboard.putNumber("PixyCam: centerX", Robot.vision.getPixyCam().BOXES.get(0).getCenterX());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PixyCam getPixyCam() {
		return pixyCam;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}

