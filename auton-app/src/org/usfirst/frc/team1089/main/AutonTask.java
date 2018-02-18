package org.usfirst.frc.team1089.main;

import javafx.util.StringConverter;

public enum AutonTask {
    GRAB_CUBE,
    SCORE_SWITCH,
    SCORE_SCALE,
    DO_NOTHING,
    DELETE,
    DONE;

    public static final StringConverter<AutonTask> STRING_CONVERTER = new StringConverter<AutonTask>() {
        @Override
        public String toString(AutonTask object) {
            if (object == null) {
                return "";
            }
            switch (object) {
                case GRAB_CUBE: {
                    return "Grab Cube";
                }
                case SCORE_SCALE: {
                    return "Score Scale";
                }
                case SCORE_SWITCH: {
                    return "Score Switch";
                }
                case DO_NOTHING: {
                    return "Do Nothing";
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
                case "Grab Cube": {
                    return AutonTask.GRAB_CUBE;
                }
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
