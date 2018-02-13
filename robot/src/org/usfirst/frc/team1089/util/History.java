package org.usfirst.frc.team1089.util;

import edu.wpi.first.wpilibj.command.Command;

public class History<T> {
    T history;

    public History(T value) {
        history = value;
    }

    public T getValue() {
        return history;
    }
}
