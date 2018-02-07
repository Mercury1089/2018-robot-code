package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1089.robot.RobotMap;
import org.usfirst.frc.team1089.robot.commands.UseElevator;

public class Elevator extends Subsystem {
    private WPI_TalonSRX elevator_talon;
    public Elevator() {
//    elevator_talon = new WPI_TalonSRX(RobotMap.CAN.TALON_ELEVATOR);
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
        elevator_talon.set(elevator_state.SPEED);
    }
    public void lift() {
        setElevatorState(ELEVATOR_STATE.LIFT);
    }
    public void lower() {
        setElevatorState(ELEVATOR_STATE.LOWER);
    }
    public void stop() {
        elevator_talon.stopMotor();
        setElevatorState(ELEVATOR_STATE.STOP);
    }
}