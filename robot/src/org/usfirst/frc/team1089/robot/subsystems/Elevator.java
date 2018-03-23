package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.RobotMap;
import org.usfirst.frc.team1089.robot.commands.ManualElevator;
import org.usfirst.frc.team1089.util.config.ManipulatorSettings;

/**
 * Subsystem encapsulating elevator mechanism to move claw up and down.
 */
public class Elevator extends Subsystem {
    private static Logger log = LogManager.getLogger(Elevator.class);

    private WPI_TalonSRX elevatorTalon;
    private WPI_VictorSPX elevatorVictorFollower;

    private DigitalInput limitSwitch;

    /**
     * Enumeration of positions that the elevator can have.
     * This is more a representation of the target positions, and does not reflect
     * the exact height of the claw at any precise moment.
     */
    public enum ElevatorPosition {
        // TODO: Temporary Values
        SCALE_HIGH(80000.0),    // Scale at its highest point
        SCALE_LOW(58000.0),     // Scale at its lowest point
        INNER_STAGE(38000),     // Height of the inner stage
        SWITCH(21000),          // Above switch fence
        DRIVE_CUBE(5000.0),     // Height for driving around cube
        FLOOR(-2000.0);         // Elevator bottomed out

        public final double encPos;

        /**
         * Creates an elevator position, storing the encoder ticks
         * representing the height that the elevator should be at.
         *
         * @param ep encoder position, in ticks
         */
        ElevatorPosition(double ep) {
            encPos = ep;
        }
    }

    private ElevatorPosition position;

    public static final double MAX_HEIGHT = ElevatorPosition.SCALE_HIGH.encPos;
    private double curHeight;

    /**
     * Creates a new elevator, using the specified CAN IDs for the
     * leader controller (Talon SRX) and follower controller (Victor SPX).

     *
     * @param talonID  Leader (Talon SRX) CAN ID
     * @param victorID Follower (Victor SPX) CAN ID
     */
    public Elevator(int talonID, int victorID) {
        elevatorTalon = new WPI_TalonSRX(talonID);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorVictorFollower = new WPI_VictorSPX(victorID);
        elevatorVictorFollower.setNeutralMode(NeutralMode.Brake);

        elevatorVictorFollower.follow(elevatorTalon);

        double[] pid = ManipulatorSettings.getElevatorPID();

        // TODO: get proper values when elevator is made.
        elevatorTalon.config_kP(DriveTrain.PRIMARY_PID_LOOP, pid[0], 10);
        elevatorTalon.config_kI(DriveTrain.PRIMARY_PID_LOOP, pid[1], 10);
        elevatorTalon.config_kD(DriveTrain.PRIMARY_PID_LOOP, pid[2], 10);
        elevatorTalon.setSensorPhase(false);

        elevatorTalon.configNominalOutputForward(.075, 10);
        elevatorTalon.configNominalOutputReverse(-.075, 10);
        elevatorTalon.configPeakOutputForward(1, 10);
        elevatorTalon.configPeakOutputReverse(-1,10);

        elevatorTalon.configAllowableClosedloopError(0, 5, 10);

        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.PID.PRIMARY_PID_LOOP, DriveTrain.TIMEOUT_MS);

        elevatorTalon.configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0, 10);
    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new UseElevator(ElevatorPosition.FLOOR));
        setDefaultCommand(new ManualElevator());
    }

    public WPI_TalonSRX getElevatorTalon() {
        return elevatorTalon;
    }

    public boolean isLimitSwitchClosed() {
        return elevatorTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    /**
     * Gets the current {@link ElevatorPosition} for the elevator.
     *
     * @return the current ElevatorPosition
     */
    public ElevatorPosition getPosition() {
        return position;
    }

    /**
     * Sets the {@link ElevatorPosition} for the elevator.
     *
     * @param ep the new ElevatorPosition to set
     */
    public void setPosition(ElevatorPosition ep) {
        position = ep;
    }

    /**
     * Get current height of claw on elevator.
     *
     * @return height of claw as read by the encoder, in ticks
     */
    public int getCurrentHeight() {
        return elevatorTalon.getSelectedSensorPosition(RobotMap.PID.PRIMARY_PID_LOOP);
    }
}