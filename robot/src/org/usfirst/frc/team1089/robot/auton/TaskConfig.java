package org.usfirst.frc.team1089.robot.auton;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TaskConfig {
    public ObjectProperty<ScoringSide> scoringSide = new SimpleObjectProperty<>();
    public ObjectProperty<AutonTask> autonTask = new SimpleObjectProperty<>();

    public TaskConfig(AutonTask autonTask, ScoringSide scoringSide) {
        this.autonTask.setValue(autonTask);
        this.scoringSide.setValue(scoringSide);
    }

    /**
     * Enumerations of tasks that can be done during auton
     */
    public enum AutonTask {
        AUTO_LINE("Auto Line"),
        SCORE_SWITCH("Score Switch"),
        SCORE_SCALE("Score Scale");

        private final String TASK;

        AutonTask(String t) {
            TASK = t;
        }

        public String toString() {
            return TASK;
        }

        public static AutonTask fromString(String string) {
            for (AutonTask at : values()) {
                if (at.TASK.equals(string)) {
                    return at;
                }
            }
            return null;
        }

        public static String[] arrayToString(TaskConfig[] tasks) {
            String[] output = new String[tasks.length];
            for (int i = 0; i < tasks.length; i++) {
                output[i] = tasks[i].autonTask.getValue().toString();
            }
            return output;
        }

        public static AutonTask[] arrayFromString(String[] strings) {
            AutonTask[] output = new AutonTask[strings.length];
            for (int i = 0; i < strings.length; i++) {
                output[i] = fromString(strings[i]);
            }
            return output;
        }
    }

    public enum ScoringSide {
        FRONT("Front"),
        MID("Mid"),
        BACK("Back"),
        NOT_APPLICABLE("Not Applicable");

        private final String SIDE;

        ScoringSide(String s) {
            SIDE = s;
        }

        @Override
        public String toString() {
            return SIDE;
        }

        public static ScoringSide fromString(String string) {
            for (ScoringSide ss : ScoringSide.values()) {
                if (ss.SIDE.equals(string)) {
                    return ss;
                }
            }
            return null;
        }

        public static String[] arrayToString(TaskConfig[] configs) {
            String[] output = new String[configs.length];
            for (int i = 0; i < configs.length; i++) {
                output[i] = configs[i].scoringSide.getValue().toString();
            }
            return output;
        }

        public static ScoringSide[] arrayFromString(String[] strings) {
            ScoringSide[] output = new ScoringSide[strings.length];
            for (int i = 0; i < strings.length; i++) {
                output[i] = fromString(strings[i]);
            }
            return output;
        }
    }

}
