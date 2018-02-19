package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.util.DelayableLogger;
import org.usfirst.frc.team1089.util.History;
import org.usfirst.frc.team1089.util.HistoryOriginator;
import org.usfirst.frc.team1089.util.MercMath;
import org.usfirst.frc.team1089.util.config.DriveTrainSettings;

import java.util.concurrent.TimeUnit;

import static org.usfirst.frc.team1089.robot.subsystems.DriveTrain.PRIMARY_PID_LOOP;

/**
 * Uses Talons and mag encoders to drive a setClawState distance.
 */
public class DriveDistance extends Command implements HistoryOriginator {
    private final double MOVE_THRESHOLD = 500;
    private final int ON_TARGET_MINIMUM_COUNT = 10;
    private int onTargetCount;

    private static Logger log = LogManager.getLogger(DriveDistance.class);
    private static final DelayableLogger SLOW_LOG = new DelayableLogger(log, 1, TimeUnit.SECONDS);
    protected double distance;
    protected double percentVoltage; // Voltage is NOW from [-1, 1]

    private double distanceTraveled = Double.NEGATIVE_INFINITY;
    private HistoryOriginator originator;
    private HistoryTreatment treatment;

    /**
     * @param dist  distance to travel, in inches
     * @param pVolt voLtage to use when driving, -1.0 to 1.0
     */
    public DriveDistance(double dist, double pVolt) {
        log.info(getName() + " Beginning constructor");
        requires(Robot.driveTrain);
        distance = dist;
        percentVoltage = pVolt;
        log.info(getName() + " Constructed");
    }

    public DriveDistance(HistoryOriginator o, HistoryTreatment t, double pVolt) {
        this(0, pVolt);

        originator = o;
        treatment = t;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        double[]
            pid = DriveTrainSettings.getPIDValues("driveDistance"),
            volts = DriveTrainSettings.getOutputRange("driveDistance");

        distanceTraveled = Double.NEGATIVE_INFINITY;

        if (originator != null) {
            distance = (Double) originator.getHistory().getValue();

            if (treatment == HistoryTreatment.REVERSE)
                distance *= -1;
        }

        setPIDF(pid[0], pid[1], pid[2], 0);
        Robot.driveTrain.configVoltage(volts[0], volts[1]);

        updateDistance();

        log.info(getName() + " initialized");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;

        double leftError = Robot.driveTrain.getLeft().getClosedLoopError(PRIMARY_PID_LOOP);
        double rightError = Robot.driveTrain.getRight().getClosedLoopError(PRIMARY_PID_LOOP);
        boolean isOnTarget = (Math.abs(rightError) < MOVE_THRESHOLD && Math.abs(leftError) < MOVE_THRESHOLD);

        SLOW_LOG.run(logger -> logger.info("leftError: " + leftError));

        if (isOnTarget) {
            onTargetCount++;
        } else {
            if (onTargetCount > 0) {
                onTargetCount = 0;
            } else {
                // we are definitely moving
            }
        }

        if (onTargetCount > ON_TARGET_MINIMUM_COUNT) {
            isFinished = true;
            onTargetCount = 0;
            log.info("DriveDistance ended");
        }

        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.stop();

        // Convert the average encoder position to inches
        distanceTraveled = Robot.driveTrain.getLeftEncPositionInFeet() * 12.0;

        // Negate distanceTraveled since encoder position is read backwards
        distanceTraveled *= -1;

        Robot.driveTrain.resetEncoders();

        //The voltage setClawState on the Talons is global, so the talons must be reconfigured back to their original outputs.
        Robot.driveTrain.configVoltage(0, Robot.driveTrain.getTalonDrive().getMaxOutput());

        log.info("Final Distance: " + distanceTraveled);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        log.info("DriveDistance interrupted");
        Robot.driveTrain.configVoltage(0, Robot.driveTrain.getTalonDrive().getMaxOutput());
        this.end();
    }

    /**
     * Updates the setpoint for PID.
     */
    protected void updateDistance() {
        double endPosL = 0, endPosR = 0;

        endPosL = MercMath.inchesToEncoderTicks(distance);
        log.info(distance);

        // Per CTRE documentation, the encoder value need to increase when the Talon LEDs are green.
        // On Crossfire, the Talon LEDs are *red* when the robot is moving forward. For this reason, we need
        // to negate both endPosR and endPosL.
        // THIS MIGHT CHANGE on the 2018 robot!!
        endPosL = -endPosL;
        endPosR = endPosL;

        endPosL += Robot.driveTrain.getLeftEncPositionInTicks();
        endPosR += Robot.driveTrain.getRightEncPositionInTicks();

        Robot.driveTrain.getLeft().set(ControlMode.Position, endPosL);
        Robot.driveTrain.getRight().set(ControlMode.Position, endPosR);
    }

    /**
     * Sets PID values on both leader talons
     *
     * @param p proportional value
     * @param i integral value
     * @param d derivative value
     * @param f feed-forward value
     */
    private void setPIDF(double p, double i, double d, double f) {
        Robot.driveTrain.getLeft().config_kP(DriveTrain.SLOT_0, p, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kP(DriveTrain.SLOT_0, p, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kI(DriveTrain.SLOT_0, i, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kI(DriveTrain.SLOT_0, i, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kD(DriveTrain.SLOT_0, d, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kD(DriveTrain.SLOT_0, d, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getLeft().config_kF(DriveTrain.SLOT_0, f, DriveTrain.TIMEOUT_MS);
        Robot.driveTrain.getRight().config_kF(DriveTrain.SLOT_0, f, DriveTrain.TIMEOUT_MS);

    }

    @Override
    public History<Double> getHistory() {
        if (distanceTraveled > Double.NEGATIVE_INFINITY)
            return new History<>(distanceTraveled);

        return null;
    }

    @Override
    public CommandType getType() {
        return CommandType.DISTANCE;
    }
}
