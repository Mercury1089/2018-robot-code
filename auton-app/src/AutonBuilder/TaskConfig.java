package AutonBuilder;

import javafx.beans.property.SimpleObjectProperty;

public class TaskConfig {
    public SimpleObjectProperty<ScoringSide> scoringSide = new SimpleObjectProperty<>();
    public SimpleObjectProperty<AutonTask> autonTask = new SimpleObjectProperty<>();

    public TaskConfig(AutonTask autonTask, ScoringSide scoringSide) {
        this.autonTask.setValue(autonTask);
        this.scoringSide.setValue(scoringSide);
    }
}
