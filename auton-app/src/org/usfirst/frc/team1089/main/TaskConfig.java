package org.usfirst.frc.team1089.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TaskConfig {
    public ObjectProperty<ScoringSide> scoringSide = new SimpleObjectProperty<>();
    public ObjectProperty<AutonTask> autonTask = new SimpleObjectProperty<>();

    public TaskConfig(AutonTask autonTask, ScoringSide scoringSide) {
        this.autonTask.setValue(autonTask);
        this.scoringSide.setValue(scoringSide);
    }

    public boolean equals(TaskConfig other) {
        return this.autonTask.get() == other.autonTask.get()
                && this.scoringSide.get() == other.scoringSide.get();
    }
}
