package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.commands.UseClaw;
import org.usfirst.frc.team1089.robot.sensors.LIDAR;
import org.usfirst.frc.team1089.robot.sensors.PixyI2C;
import org.usfirst.frc.team1089.robot.sensors.Ultrasonic;
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

    // All sensors belong to the claw, I guess?
    private LIDAR lidar;
    private CANifier canifier;
    private PixyI2C pixyCam;
    private Ultrasonic ultrasonic;

    private boolean hasCube;
    private boolean hasCubeUltrasonic;
    private boolean ejecting;

    public enum ClawState {
        GRAB(1.0), EJECT(-1.0), STOP(0.0);
        public final double SPEED;

        ClawState(double speed) {
            SPEED = speed;
        }
    }

    /**
     * Constructs a new claw, specifying the ports for the ledaer SPX, follower SPX,
     * and all the sensors connected to it.
     *
     * @param usPort    Ultrasonic port
     * @param cfID      CANifier CAN ID
     * @param lidarPort LIDAR port
     * @param leadID    Leader (Victor SPX) CAN ID
     * @param folID     Follower (Victor SPX) CAN ID
     */
    public Claw(int usPort, int cfID, int lidarPort, int leadID, int folID) {
        canifier = new CANifier(cfID);

        clawMotor_S = new WPI_VictorSPX(folID);
        clawMotor_M = new WPI_VictorSPX(leadID);

        pixyCam = new PixyI2C();

        ultrasonic = new Ultrasonic(usPort);

        // Clamp pwm id between 0 and 3
        LIDAR.PWMOffset offset = SensorsSettings.getLidarEquation();

        hasCube = false;

        this.lidar = new LIDAR(canifier, CANifier.PWMChannel.valueOf(lidarPort), offset);

        setName("Claw");
        log.info("Initalized claw");
        clawMotor_M.setInverted(false);
        clawMotor_S.setInverted(true);
        clawMotor_S.follow(clawMotor_M);
    }

    @Override
    public void periodic() {
        lidar.updatePWMInput();
        updateState();
    }

    /**
     * Updates the states of the LEDs and the gamepad rumble
     * based on whether or not the cube is in range or is being held.
     */
    private void updateState() {
        boolean rumble = false;

        if (pixyCam.inRange()) { // Cube is in range to auto pickup
            // White
            colorLED(255, 255, 255);
            rumble = true;
        } else if (hasCube()) { // Have cube?
            // Listen from SmartDash

            // Fun colors to note:
            // Orange (In range): 255, 30, 0
            // Purple (Something about a cube): 255, 0, 255
            // Cyan (Very nice color): 0, 255, 255
            int r = (int) SmartDashboard.getNumber("LED Color (R)", 255);
            int g = (int) SmartDashboard.getNumber("LED Color (G)", 161);
            int b = (int) SmartDashboard.getNumber("LED Color (B)", 0);
            colorLED(r, g, b);
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
        canifier.setLEDOutput((double) g / 255.0, CANifier.LEDChannel.LEDChannelA);
        canifier.setLEDOutput((double) r / 255.0, CANifier.LEDChannel.LEDChannelB);
        canifier.setLEDOutput((double) b / 255.0, CANifier.LEDChannel.LEDChannelC);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(null);
    }

    public void setClawState(ClawState state) {
        clawMotor_M.set(state.SPEED);
    }

    public CANifier getCanifier() {
        return canifier;
    }

    public LIDAR getLidar() {
        return lidar;
    }

    public PixyI2C getPixyCam() {
        return pixyCam;
    }

    public Ultrasonic getUltrasonic() {
        return ultrasonic;
    }

    public boolean hasCube() {
        return ultrasonic.getRange() <= 3.6;
    }

    public boolean getEjecting() {
        return ejecting;
    }

    public void setEjecting(boolean b) {
        ejecting = b;
    }
}
