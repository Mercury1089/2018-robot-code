package org.usfirst.frc.team1089.main;

import javafx.util.StringConverter;

public enum ScoringSide {
    FRONT,
    MID,
    BACK,
    NOT_APPLICABLE;

    public static final StringConverter<ScoringSide> STRING_CONVERTER = new StringConverter<ScoringSide>() {
        @Override
        public String toString(ScoringSide object) {
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

        @Override
        public ScoringSide fromString(String string) {
            switch (string) {
                case "Front":
                    return ScoringSide.FRONT;
                case "Middle":
                    return ScoringSide.MID;
                case "Back":
                    return ScoringSide.BACK;
                case "Not Applicable":
                    return NOT_APPLICABLE;
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
