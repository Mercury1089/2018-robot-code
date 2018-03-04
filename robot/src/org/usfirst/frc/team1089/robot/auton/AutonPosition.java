package org.usfirst.frc.team1089.robot.auton;

/**
 * Enumeration of possible auton positions.
 */
public enum AutonPosition {
    LEFT("Left"),
    LEFT_MID("Mid Left"),
    RIGHT_MID("Mid Right"),
    RIGHT("Right");

    private final String POSITION;

    AutonPosition(String p) {
        POSITION = p;
    }

    public String toString() {
        return POSITION;
    }

    public static AutonPosition fromString(String positionString) {
        for (AutonPosition ap : values()) {
            if (ap.POSITION.equals(positionString)) {
                return ap;
            }
        }
        return null;
    }
}
