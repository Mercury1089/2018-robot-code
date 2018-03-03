package org.usfirst.frc.team1089.robot.auton;

public enum FieldSide {
    LEFT_SIDE("L"),
    RIGHT_SIDE("R");

    private final String SIDE;

    FieldSide(String s) {
        SIDE = s;
    }

    @Override
    public String toString() {
        return SIDE;
    }

    public static FieldSide fromString(String fieldSideString) {
        for (FieldSide fs : values()) {
            if (fs.SIDE.equals(fieldSideString)) {
                return fs;
            }
        }
        return null;
    }
}
