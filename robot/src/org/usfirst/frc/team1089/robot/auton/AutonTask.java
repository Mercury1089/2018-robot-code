package org.usfirst.frc.team1089.robot.auton;

/**
 * Enumerations of tasks that can be done during auton
 */
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

    public static String[] arrayToString(AutonTask[] tasks) {
        String[] output = new String[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            output[i] = toString(tasks[i]);
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
