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

    public AutonBuilder(AutonPosition autonPos, AutonTask[] autonTasks, ScoringSide[] scoreSide) {
        this.autonPos = autonPos;
        this.autonTasks = autonTasks;
        this.scoreSide = scoreSide;
    }

    public AutonPosition getAutonPos() {
        return autonPos;
    }

    public AutonTask[] getAutonTasks() {
        return autonTasks;
    }

    public ScoringSide[] getScoreSide() {
        return scoreSide;
    }

}
