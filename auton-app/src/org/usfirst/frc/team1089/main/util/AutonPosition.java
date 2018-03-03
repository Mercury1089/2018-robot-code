package org.usfirst.frc.team1089.main.util;

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
            if (ap.POSITION == positionString) {
                return ap;
            }
        }
        return null;
    }
}
