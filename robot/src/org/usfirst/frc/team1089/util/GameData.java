package org.usfirst.frc.team1089.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class cntaining data structures for representing
 * different stats of the game
 */
public class GameData {

    private static GameData gameData;
    private PlateSide[] plateSides;

    public enum PlateSide {
        LEFT,
        RIGHT,
        UNKNOWN
    }

    public GameData() {
        String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
        plateSides = new PlateSide[3];
        if (gameSpecificMessage != null) {
            for (int i = 0; i < gameSpecificMessage.length(); i++) {
                switch (gameSpecificMessage.charAt(i)) {
                    case 'L':
                        plateSides[i] = PlateSide.LEFT;
                        break;
                    case 'R':
                        plateSides[i] = PlateSide.RIGHT;
                        break;
                    default:
                        plateSides[i] = PlateSide.UNKNOWN;
                }
            }
        }
    }

    public static GameData getInstance() {
        if(gameData == null) {
            gameData = new GameData();

        }
        return gameData;
    }

    public PlateSide getSwitchSide() {
        return plateSides[0];
    }

    public PlateSide getScaleSide() {
        return plateSides[1];
    }

    @Override
    public String toString() {
        String message = "";
        for(int i = 0; i < 3; i++) {
            message += plateSides[i];
        }
        return message;
    }
}
