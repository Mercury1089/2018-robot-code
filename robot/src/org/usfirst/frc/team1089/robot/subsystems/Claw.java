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
    public final double MIN_INCHES = 10.0;

    private WPI_VictorSPX
            clawMotor_M,
            clawMotor_S;

    private LIDAR lidar;
    private CANifier canifier;
    private boolean hasCube;
    private boolean ejecting;

    public enum ClawState {
        GRAB(1.0), EJECT(-1.0), STOP(0.0);
        public final double SPEED;

        ClawState(double speed) {
            SPEED = speed;
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
        updateState();
    }

    private void updateState() {
        boolean rumble = false;

        if (Robot.vision.getPixyCam().inRange()) { // Cube is in range to auto pickup
            // White
            colorLED(255, 255, 255);
            rumble = true;
        } else if (Robot.claw.getLidar().getDistance() <= MIN_INCHES) { // Have cube?
            // Orange
            colorLED(255, 161, 0);
        } else {
            // None
            colorLED(0, 0, 0);
        }

        Robot.oi.rumbleController(rumble);
    }

    /**
     * Sets the color of the LED based on RBG int values
     *
     * @param r red value [0 - 255]
     * @param g green value [0 - 255]
     * @param b blue value [0 - 255]
     */
    private void colorLED(int r, int g, int b) {
        canifier.setLEDOutput(r / 255.0, CANifier.LEDChannel.LEDChannelA);
        canifier.setLEDOutput(g / 255.0, CANifier.LEDChannel.LEDChannelB);
        canifier.setLEDOutput(b / 255.0, CANifier.LEDChannel.LEDChannelC);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(null);
    }

    public void set(ClawState state) {
        clawMotor_M.set(state.SPEED);
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

    public void setHasCube(boolean b) {
        hasCube = b;
    }

    public boolean getEjecting() {
        return ejecting;
    }

    public void setEjecting(boolean b) {
        ejecting = b;
    }
}
