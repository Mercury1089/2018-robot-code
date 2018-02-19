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

    public static String[] arrayToString(AutonTask[] tasks) {
        String[] output = new String[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            output[i] = STRING_CONVERTER.toString(tasks[i]);
        }
        return output;
    }

    public static AutonTask[] arrayFromString(String[] strings) {
        AutonTask[] output = new AutonTask[strings.length];
        for (int i = 0; i < strings.length; i++) {
            output[i] = STRING_CONVERTER.fromString(strings[i]);
        }
        return output;
    }
}
