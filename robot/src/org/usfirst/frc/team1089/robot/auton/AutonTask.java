package org.usfirst.frc.team1089.robot.auton;

public enum AutonTask {
    SCORE_SWITCH,
    SCORE_SCALE,
    DELETE,
    DONE;

    public static String toString(AutonTask object) {
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

    public static AutonTask fromString(String string) {
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

    public static String[] arrayToString(Object[] data) {
        String[] output = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = toString(((TaskConfig) data[i]).autonTask.getValue());
        }
        return output;
    }

    public static AutonTask[] arrayFromString(String[] strings) {
        AutonTask[] output = new AutonTask[strings.length];
        for (int i = 0; i < strings.length; i++) {
            output[i] = fromString(strings[i]);
        }
        return output;
    }
}
