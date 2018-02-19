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
import org.usfirst.frc.team1089.robot.commands.UseElevator;
import org.usfirst.frc.team1089.util.config.ManipulatorSettings;

public class Elevator extends Subsystem {
    private static Logger log = LogManager.getLogger(Elevator.class);

    private WPI_TalonSRX elevatorTalon;
    private WPI_VictorSPX elevatorVictorFollower;

    private DigitalInput limitSwitch;

    public enum ElevatorState {
        // TODO: Temporary Values
        SWITCH(150.0),
        SCALE_LOW(300.0),
        SCALE_HIGH(2000.0),
        STOP(0.0);

        public final double encPos;

        ElevatorState(double encPos) {
            this.encPos = encPos;
        }
    }

    private ElevatorState currentState;

    public static final double MAX_HEIGHT = ElevatorState.SCALE_HIGH.encPos; //TODO Random value, change to the max height of the elevator
    private double curHeight;

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
        elevatorTalon.setSensorPhase(true);

        elevatorTalon.configNominalOutputForward(.1, 10);
        elevatorTalon.configNominalOutputReverse(-.1, 10);
        elevatorTalon.configPeakOutputForward(.3, 10);
        elevatorTalon.configPeakOutputReverse(-.3, 10);

        elevatorTalon.configAllowableClosedloopError(0, 5, 10);

        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.PID.PRIMARY_PID_LOOP, DriveTrain.TIMEOUT_MS);

        elevatorTalon.configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0, 10);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseElevator(ElevatorState.STOP));
    }

    public WPI_TalonSRX getElevatorTalon() {
        return elevatorTalon;
    }

    public boolean isLimitSwitchClosed() {
        return elevatorTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    public ElevatorState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ElevatorState currentState) {
        this.currentState = currentState;
    }

    public double getHeight() {
        return elevatorTalon.getSelectedSensorPosition(RobotMap.PID.PRIMARY_PID_LOOP);
    }
}