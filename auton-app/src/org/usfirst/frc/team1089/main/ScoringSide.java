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
}
