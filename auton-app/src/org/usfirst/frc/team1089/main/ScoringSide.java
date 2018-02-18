package org.usfirst.frc.team1089.main;

import javafx.util.StringConverter;

public enum ScoringSide {
    Front,
    Mid,
    Back,
    Not_Applicable;

    public static final StringConverter<ScoringSide> STRING_CONVERTER = new javafx.util.StringConverter<ScoringSide>() {
        @Override
        public String toString(ScoringSide object) {
            if (object == null) {
                return "";
            }
            switch (object) {
                case Not_Applicable:
                    return "Not Applicable";
                default:
                    return object.toString();
            }
        }

        @Override
        public ScoringSide fromString(String string) {
            switch (string) {
                case "Front": {
                    return ScoringSide.Front;
                }
                case "Middle": {
                    return ScoringSide.Mid;
                }
                case "Back": {
                    return ScoringSide.Back;
                }
                default:
                    return null;
            }
        }
    };
}
