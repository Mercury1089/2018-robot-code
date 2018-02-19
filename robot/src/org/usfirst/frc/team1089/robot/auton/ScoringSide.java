package org.usfirst.frc.team1089.robot.auton;

public enum ScoringSide {
    FRONT,
    MID,
    BACK,
    NOT_APPLICABLE;

    public static String toString(ScoringSide object) {
        if (object == null) {
            return "";
        }

        switch (object) {
            case NOT_APPLICABLE:
                return "Not Applicable";
            case FRONT:
                return "Front";
            case MID:
                return "Mid";
            case BACK:
                return "Back";
            default:
                return "";
        }
    }

    public static ScoringSide fromString(String string) {
        switch (string) {
            case "Front":
                return ScoringSide.FRONT;
            case "Mid":
                return ScoringSide.MID;
            case "Back":
                return ScoringSide.BACK;
            case "Not Applicable":
                return NOT_APPLICABLE;
            default:
                return null;
        }
    }

    public static String[] arrayToString(Object[] data) {
        String[] output = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = toString(((TaskConfig) data[i]).scoringSide.getValue());
        }
        return output;
    }

    public static ScoringSide[] arrayFromString(String[] strings) {
        ScoringSide[] output = new ScoringSide[strings.length];
        for (int i = 0; i < strings.length; i++) {
            output[i] = fromString(strings[i]);
        }
        return output;
    }
}
