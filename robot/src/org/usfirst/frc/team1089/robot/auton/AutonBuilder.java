package org.usfirst.frc.team1089.robot.auton;

/**
 * Builds an auton based on certain positions and tasks.
 *
 * This is good for modular autons that are easily customizable.
 */
public class AutonBuilder {
    private AutonPosition autonPos;
    private AutonTask[] autonTasks;
    private ScoringSide[] scoreSide;
    private FieldSide fieldSide;

    public AutonBuilder(AutonPosition autonPos, FieldSide fieldSide, AutonTask[] autonTasks, ScoringSide[] scoreSide) {
        this.autonPos = autonPos;
        this.fieldSide = fieldSide;
        this.autonTasks = autonTasks;
        this.scoreSide = scoreSide;
    }

    public AutonPosition getAutonPos() {
        return autonPos;
    }

    public FieldSide getFieldSide() { return fieldSide; }

    public AutonTask[] getAutonTasks() {
        return autonTasks;
    }

    public ScoringSide[] getScoreSide() {
        return scoreSide;
    }

}
