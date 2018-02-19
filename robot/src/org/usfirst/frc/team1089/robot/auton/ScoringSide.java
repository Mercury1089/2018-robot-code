package org.usfirst.frc.team1089.robot.auton;

import javafx.util.StringConverter;

/**
 * Enumeration of sides to score on for the scale or switch
 *
 * <h3>SIDES</h3>
 * <ul>
 *     <li>FRONT - Far side of scale/switch</li>
 *     <li>MID - Outer edge of scale/switch</li>
 *     <li>BACK - Near side of scale/switch</li>
 * </ul>
 */
public enum ScoringSide {
    FRONT,
    MID,
    BACK,
    NOT_APPLICABLE;

    public static final StringConverter<ScoringSide> STRING_CONVERTER = new javafx.util.StringConverter<ScoringSide>() {
        @Override
        public String toString(ScoringSide object) {
            if (object == null) {
                return "";
            }
            switch (object) {
                case NOT_APPLICABLE:
                    return "Not Applicable";
                default:
                    return object.toString();
            }
        }

        @Override
        public ScoringSide fromString(String string) {
            switch (string) {
                case "FRONT": {
                    return ScoringSide.FRONT;
                }
                case "Middle": {
                    return ScoringSide.MID;
                }
                case "BACK": {
                    return ScoringSide.BACK;
                }
                default:
                    return null;
            }
        }
    };

    public static String[] arrayToString(ScoringSide[] sides) {
        String[] output = new String[sides.length];
        for (int i = 0; i < sides.length; i++) {
            output[i] = STRING_CONVERTER.toString(sides[i]);
        }
        return output;
    }

    public static ScoringSide[] arrayFromString(String[] strings) {
        ScoringSide[] output = new ScoringSide[strings.length];
        for (int i = 0; i < strings.length; i++) {
            output[i] = STRING_CONVERTER.fromString(strings[i]);
        }
        return output;
    }
}
