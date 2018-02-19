package org.usfirst.frc.team1089.robot.auton;

/**
 * Enumeration of possible auton positions.
 */
public enum AutonPosition {
	LEFT("Left"),
	MIDDLE("Middle"),
	RIGHT("Right");

	private final String name;

	private AutonPosition(String n) {
		name = n;
	}

	public String toString() {
		return name;
	}
}
