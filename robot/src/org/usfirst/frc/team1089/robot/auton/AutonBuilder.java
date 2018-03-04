package org.usfirst.frc.team1089.robot.auton;

/**
 * Builds an auton based on certain positions and tasks.
 *
 * This is good for modular autons that are easily customizable.
 */
public class AutonBuilder {
    private AutonPosition autonPos;
    private TaskConfig.AutonTask[] autonTasks;
    private TaskConfig.ScoringSide[] scoreSide;
    private FieldSide fieldSide;

    public AutonBuilder(AutonPosition autonPos, FieldSide fieldSide, TaskConfig.AutonTask[] autonTasks, TaskConfig.ScoringSide[] scoreSide) {
        this.autonPos = autonPos;
        this.fieldSide = fieldSide;
        this.autonTasks = autonTasks;
        this.scoreSide = scoreSide;
    }

    public AutonPosition getAutonPos() {
        return autonPos;
    }

    public FieldSide getFieldSide() { return fieldSide; }

    public TaskConfig.AutonTask[] getAutonTasks() {
        return autonTasks;
    }

    public TaskConfig.ScoringSide[] getScoreSide() {
        return scoreSide;
    }

}
