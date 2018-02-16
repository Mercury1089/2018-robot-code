package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
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
    public final double MIN_INCHES = 8.0;

    private WPI_VictorSPX
            clawMotor_M,
            clawMotor_S;

    private LIDAR lidar;
    private CANifier canifier;
    private boolean hasCube;
    private boolean ejecting;

    public enum ClawState {
        GRAB(1.0), EJECT(-1.0), STOP(0.0);
        public final double speed;
        ClawState(double speed) {
            this.speed = speed;
        }
    }

    public Claw(int canID, int pwm, int leader, int follower) {
        canifier = new CANifier(canID);

        clawMotor_S = new WPI_VictorSPX(follower);
        clawMotor_M = new WPI_VictorSPX(leader);

        // Clamp pwm id between 0 and 3
        pwm = (int) MercMath.clamp(pwm, 0, 3);
        LIDAR.PWMOffset offset = SensorsSettings.getLidarEquation();

        hasCube = false;

        lidar = new LIDAR(canifier, CANifier.PWMChannel.valueOf(pwm), offset);

        setName("Claw");
        log.info("Initalized claw");
        clawMotor_M.setInverted(false);
        clawMotor_S.setInverted(false);
        clawMotor_S.follow(clawMotor_M);
    }

    @Override
    public void periodic() {
        lidar.updatePWMInput();

        boolean rumble = false;

        if (Robot.vision.getPixyCam().inRange()) { //Cube is in range to auto pickup
            //set green (R: 28.2%, G: 91%, B: 14.9%)
            canifier.setLEDOutput(.282, CANifier.LEDChannel.LEDChannelA);
            canifier.setLEDOutput(.91, CANifier.LEDChannel.LEDChannelB);
            canifier.setLEDOutput(.149, CANifier.LEDChannel.LEDChannelC);

            rumble = true;
        } else if (Robot.claw.getLidar().getDistance() <= MIN_INCHES) { //Have cube?
            //set orange (R: 100%, G: 74.5%, B: 3.9%)
            canifier.setLEDOutput(1, CANifier.LEDChannel.LEDChannelA);
            canifier.setLEDOutput(.745, CANifier.LEDChannel.LEDChannelB);
            canifier.setLEDOutput(.39, CANifier.LEDChannel.LEDChannelC);
        } else if (Robot.claw.getEjecting()) { //Robo is ejecting cube
            //set red (R: 100%, G: 3.9%, B: 3.9%)
            canifier.setLEDOutput(1, CANifier.LEDChannel.LEDChannelA);
            canifier.setLEDOutput(.39, CANifier.LEDChannel.LEDChannelB);
            canifier.setLEDOutput(.39, CANifier.LEDChannel.LEDChannelC);
        } else { //default case
            // set white (All values max power, maximum)
            canifier.setLEDOutput(1, CANifier.LEDChannel.LEDChannelA);
            canifier.setLEDOutput(1, CANifier.LEDChannel.LEDChannelB);
            canifier.setLEDOutput(1, CANifier.LEDChannel.LEDChannelC);
        }

        Robot.oi.rumbleController(rumble);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(null);
    }

    public void set(ClawState state) {
        clawMotor_M.set(state.speed);
    }

    public CANifier getCanifier() {
        return canifier;
    }

    public LIDAR getLidar() {
        return lidar;
    }

    public boolean getHasCube() {
        return hasCube;
    }

    public void setHasCube(boolean b){
        hasCube = b;
    }

    public boolean getEjecting() {
        return ejecting;
    }

    public void setEjecting(boolean b){
        ejecting = b;
    }
}
