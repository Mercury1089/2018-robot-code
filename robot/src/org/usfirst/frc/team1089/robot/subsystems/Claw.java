package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.UseClaw;


public class Claw extends Subsystem {

    private WPI_TalonSRX clawMotor;
    private Logger log = LogManager.getLogger(Claw.class);

    public enum ClawState {
        GRAB(0.5), EJECT(-0.5), STOP(0.0);
        public final double speed;
        ClawState(double speed) {
            this.speed = speed;
        }
    }

    public Claw(int talon){
        clawMotor = new WPI_TalonSRX(talon);
        setName("Claw");
        log.info("Initalized claw");
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseClaw(ClawState.STOP));
    }

    public void set(ClawState state) {
        clawMotor.set(state.speed);
    }

    public void grab(){
        set(ClawState.GRAB);
    }

    public void eject(){
        set(ClawState.EJECT);
    }

    public void stop(){
        clawMotor.stopMotor();
        set(ClawState.STOP);
    }

}
