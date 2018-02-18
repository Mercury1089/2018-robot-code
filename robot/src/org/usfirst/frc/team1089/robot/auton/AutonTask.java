package org.usfirst.frc.team1089.robot.auton;

import javafx.util.StringConverter;

public enum AutonTask {
    SCORE_SWITCH,
    SCORE_SCALE,
    DELETE,
    DONE;

    public static final StringConverter<AutonTask> STRING_CONVERTER = new StringConverter<AutonTask>() {
        @Override
        public String toString(AutonTask object) {
            if (object == null) {
                return "";
            }
            switch (object) {
                case SCORE_SCALE: {
                    return "Score Scale";
                }
                case SCORE_SWITCH: {
                    return "Score Switch";
                }
                case DELETE: {
                    return "Delete";
                }
                case DONE: {
                    return "Done";
                }
                default:
                    return object.toString();
            }
        }

        @Override
        public AutonTask fromString(String string) {
            switch (string) {
                case "Score Scale": {
                    return AutonTask.SCORE_SCALE;
                }
                case "Score Switch": {
                    return AutonTask.SCORE_SWITCH;
                }
                case "Done": {
                    return AutonTask.DONE;
                }
                case "Delete": {
                    return AutonTask.DELETE;
                }
                default:
                    return null;
            }
        }
    };
}
