package org.usfirst.frc.team1089.util;

/**
 * Interface to implement for objects that can create history.
 */
public interface HistoryOriginator {
    public enum CommandType {
        ROTATION,
        DISTANCE
    }

    public enum HistoryTreatment {
        REVERSE,
        REPEAT
    }

    public History getHistory();

    public CommandType getType();
}