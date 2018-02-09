package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.UseElevator;

public class Elevator extends Subsystem {
    private WPI_TalonSRX elevatorTalon;
    private static Logger log = LogManager.getLogger(Elevator.class);
    public Elevator(int talonID) {
        elevatorTalon = new WPI_TalonSRX(talonID);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, DriveTrain.PRIMARY_PID_LOOP, DriveTrain.TIMEOUT_MS);
    }

    public enum ELEVATOR_STATE {
        LIFT(1.0), LOWER(-1.0), STOP(0.0);
        public final double SPEED;

        ELEVATOR_STATE(double SPEED) {
            this.SPEED = SPEED;
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseElevator(ELEVATOR_STATE.STOP));
    }
    public void setElevatorState(ELEVATOR_STATE elevator_state) {
        elevatorTalon.set(elevator_state.SPEED);
    }
    public void lift() {
        setElevatorState(ELEVATOR_STATE.LIFT);
    }
    public void lower() {
        setElevatorState(ELEVATOR_STATE.LOWER);
    }
    public void stop() {
        elevatorTalon.stopMotor();
        setElevatorState(ELEVATOR_STATE.STOP);
    }
    public WPI_TalonSRX getElevatorTalon(){
        return elevatorTalon;
    }


}