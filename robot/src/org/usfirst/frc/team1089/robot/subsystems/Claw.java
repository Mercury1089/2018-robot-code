package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.UseClaw;
import org.usfirst.frc.team1089.robot.sensors.LIDAR;
import org.usfirst.frc.team1089.util.MercMath;
import org.usfirst.frc.team1089.util.config.SensorsSettings;

/**
 * Subsystem that encapsulates actuators and sensors for
 * claw mechanism
 */
public class Claw extends Subsystem {
    private static Logger log = LogManager.getLogger(Claw.class);

    private WPI_TalonSRX
            clawMotor_M,
            clawMotor_S;

    private LIDAR lidar;

    public enum ClawState {
        GRAB(1.0), EJECT(-1.0), STOP(0.0);
        public final double speed;
        ClawState(double speed) {
            this.speed = speed;
        }
    }

    public Claw(int canifier, int pwm, int leader, int follower){
        clawMotor_S = new WPI_TalonSRX(follower);
        clawMotor_M = new WPI_TalonSRX(leader);

        // Clamp pwm id between 0 and 3
        pwm = (int) MercMath.clamp(pwm, 0, 3);
        LIDAR.PWMOffset offset = SensorsSettings.getLidarEquation();

        lidar = new LIDAR(canifier, CANifier.PWMChannel.valueOf(pwm), offset);

        setName("Claw");
        log.info("Initalized claw");
        clawMotor_M.setInverted(false);
        clawMotor_S.setInverted(true);
        clawMotor_S.follow(clawMotor_M);
    }

    @Override
    public void periodic() {
        lidar.updatePWMInput();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(null);
    }

    public void set(ClawState state) {
        clawMotor_M.set(state.speed);
    }

    public LIDAR getLidar() {
        return lidar;
    }
}
