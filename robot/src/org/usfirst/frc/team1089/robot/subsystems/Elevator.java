package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.commands.UseElevator;

public class Elevator extends Subsystem {
    private WPI_TalonSRX elevatorTalon;
    private static Logger log = LogManager.getLogger(Elevator.class);
    public Elevator(int talonID) {
        elevatorTalon = new WPI_TalonSRX(talonID);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        //ToDo get proper values when elevator is made.
        Robot.elevator.getElevatorTalon().config_kP(DriveTrain.PRIMARY_PID_LOOP, .1, 10);
        Robot.elevator.getElevatorTalon().config_kI(DriveTrain.PRIMARY_PID_LOOP,0, 10);
        Robot.elevator.getElevatorTalon().config_kD(DriveTrain.PRIMARY_PID_LOOP,0, 10);
        Robot.elevator.getElevatorTalon().setSensorPhase(true);

        Robot.elevator.getElevatorTalon().configNominalOutputForward(.05, 10);
        Robot.elevator.getElevatorTalon().configNominalOutputReverse(-.05, 10);
        Robot.elevator.getElevatorTalon().configPeakOutputForward(.3, 10);
        Robot.elevator.getElevatorTalon().configPeakOutputReverse(-.3, 10);
        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, DriveTrain.PRIMARY_PID_LOOP, DriveTrain.TIMEOUT_MS);

    }

    public enum ELEVATOR_STATE {
        SWITCH(150.0), SCALE_LOW(300.0), SCALE_HIGH(450.0), STOP(0.0); //Temporary Values
        public final double encPos;

        ELEVATOR_STATE(double encPos) {
            this.encPos = encPos;
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseElevator(ELEVATOR_STATE.STOP));
    }

    public WPI_TalonSRX getElevatorTalon(){
        return elevatorTalon;
    }


}