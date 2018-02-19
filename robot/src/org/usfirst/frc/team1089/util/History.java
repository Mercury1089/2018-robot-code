package org.usfirst.frc.team1089.util;

/**
 * Object representing a value that can be used to repeat
 * or reverse a comand
 *
 * @param <T> the data type of the history
 */
public class History<T> {
    T history;

    public History(T value) {
        history = value;
    }

    public T getValue() {
        return history;
    }
}
