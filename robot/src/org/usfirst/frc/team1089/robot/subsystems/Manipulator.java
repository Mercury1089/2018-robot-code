package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.commands.TestLIDAR;
import org.usfirst.frc.team1089.util.LIDAR;
import org.usfirst.frc.team1089.util.config.SensorsSettings;

/**
 * Subsystem encapsulating manipulator actuators and sensors.
 */
public class Manipulator extends Subsystem {
    private static Logger log = LogManager.getLogger(Manipulator.class);
    private LIDAR lidar;
    private WPI_VictorSPX victorLeft;
    private WPI_VictorSPX victorRight;

    public Manipulator(int canifierID, int pwmChannel, int victorLeftId, int victorRightId) {
        victorLeft = new WPI_VictorSPX(victorLeftId);
        victorRight = new WPI_VictorSPX(victorRightId);

        lidar = new LIDAR(canifierID, CANifier.PWMChannel.valueOf(pwmChannel), SensorsSettings.getLidarEquation());
    }

    public LIDAR getLidar() {
        return lidar;
    }

    public WPI_VictorSPX getVictorLeft() {
        return victorLeft;
    }

    public WPI_VictorSPX getVictorRight()
    {
        return victorRight;
    }

    @Override
    public void periodic() {
        lidar.updatePWMInput();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new TestLIDAR());
    }
}
