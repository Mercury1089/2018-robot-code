package org.usfirst.frc.team1089.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class containing data structures for representing different stats of the game
 */
public class GameData {

    private static final int MSG_LENGTH = 3;
    private static PlateSide[] plateSides;

    public enum PlateSide {
        LEFT('L'),
        RIGHT('R'),
        UNKNOWN('X');

        private final char CHAR;

        PlateSide(char c) {
            CHAR = c;
        }

        public static PlateSide toChar(char c) {
            for (PlateSide p : values()) {
                if (p.CHAR == c)
                    return p;
            }

            return null;
        }

        public char getChar() {
            return CHAR;
        }

        @Override
        public String toString() {
            return "" + CHAR;
        }
    }

    public static void updateGameData() {
        String msg = DriverStation.getInstance().getGameSpecificMessage();
        boolean validMsg = msg.length() == MSG_LENGTH && msg.charAt(0) == msg.charAt(MSG_LENGTH - 1);

        plateSides = new PlateSide[3];

        if (msg != null) {
            int i = 0;

            while (i < msg.length()) {
                if (validMsg) {
                    char cMsg = msg.charAt(i);
                    switch (cMsg) {
                        case 'L':
                        case 'R':
                            plateSides[i] = PlateSide.toChar(cMsg);
                            break;
                        default:
                            validMsg = false;
                            i = 0;
                            break;
                    }
                } else
                    plateSides[i] = PlateSide.UNKNOWN;

                i++;
            }
        }
    }

    public static PlateSide getSwitchSide() {
        return plateSides[0];
    }

    public static PlateSide getScaleSide() {
        return plateSides[1];
    }

    public static String getParsedString() {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < 3; i++)
            message.append(plateSides[i].getChar());

        return message.toString();
    }
}
