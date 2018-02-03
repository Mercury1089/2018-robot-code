package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.UseClaw;


public class Claw extends Subsystem {

    private WPI_TalonSRX
            clawMotor_M,
            clawMotor_S;
    private Logger log = LogManager.getLogger(Claw.class);

    public enum ClawState {
        GRAB(1.0), EJECT(-1.0), STOP(0.0);
        public final double speed;
        ClawState(double speed) {
            this.speed = speed;
        }
    }

    public Claw(int leader, int follower){
        clawMotor_S = new WPI_TalonSRX(follower);
        clawMotor_M = new WPI_TalonSRX(leader);
        setName("Claw");
        log.info("Initalized claw");
        clawMotor_M.setInverted(false);
        clawMotor_S.setInverted(true);
        clawMotor_S.follow(clawMotor_M);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseClaw(ClawState.STOP));
    }

    public void set(ClawState state) {
        clawMotor_M.set(state.speed);
    }

    public void grab(){
        set(ClawState.GRAB);
    }

    public void eject(){
        set(ClawState.EJECT);
    }

    public void stop(){
        clawMotor_M.stopMotor();
        set(ClawState.STOP);
    }

}
