package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap;
import org.usfirst.frc.team1089.robot.commands.UseElevator;
import org.usfirst.frc.team1089.util.config.ManipulatorSettings;

public class Elevator extends Subsystem {
    private static Logger log = LogManager.getLogger(Elevator.class);

    private WPI_TalonSRX elevatorTalon;
    private WPI_VictorSPX elevatorVictorFollower;

    private DigitalInput limitSwitch;

    private ELEVATOR_STATE currentState;

    private double curHeight;
    public static final double MAX_HEIGHT = 450.0; //TODO Random value, change to the max height of the elevator

    public Elevator(int talonID, int victorID) {
        elevatorTalon = new WPI_TalonSRX(talonID);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorVictorFollower = new WPI_VictorSPX(victorID);
        elevatorVictorFollower.setNeutralMode(NeutralMode.Brake);

        elevatorVictorFollower.follow(elevatorTalon);

        limitSwitch = new DigitalInput(RobotMap.DIGITAL_INPUT.ELEVATOR_LIMIT_SWITCH);

        double[] pid = ManipulatorSettings.getElevatorPID();

        //ToDo get proper values when elevator is made.
        elevatorTalon.config_kP(DriveTrain.PRIMARY_PID_LOOP, pid[0], 10);
        elevatorTalon.config_kI(DriveTrain.PRIMARY_PID_LOOP, pid[1], 10);
        elevatorTalon.config_kD(DriveTrain.PRIMARY_PID_LOOP, pid[2], 10);
        elevatorTalon.setSensorPhase(true);

        elevatorTalon.configNominalOutputForward(.05, 10);
        elevatorTalon.configNominalOutputReverse(-.05, 10);
        elevatorTalon.configPeakOutputForward(.3, 10);
        elevatorTalon.configPeakOutputReverse(-.3, 10);

        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, DriveTrain.PRIMARY_PID_LOOP, DriveTrain.TIMEOUT_MS);
    }

    public enum ELEVATOR_STATE {
        // TODO: Temporary Values
        SWITCH(150.0),
        SCALE_LOW(300.0),
        SCALE_HIGH(450.0),
        STOP(0.0);

        public final double encPos;

        ELEVATOR_STATE(double encPos) {
            this.encPos = encPos;
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseElevator(ELEVATOR_STATE.STOP));
    }

    public WPI_TalonSRX getElevatorTalon() {
        return elevatorTalon;
    }

    public DigitalInput getLimitSwitch() {
        return limitSwitch;
    }

    public ELEVATOR_STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ELEVATOR_STATE currentState) {
        this.currentState = currentState;
    }

    public void setCurHeight(double curHeight) {
        this.curHeight = curHeight;
    }

    public double getCurHeight() {
        return curHeight;
    }
}