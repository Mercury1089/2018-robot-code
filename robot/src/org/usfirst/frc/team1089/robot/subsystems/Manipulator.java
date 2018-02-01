package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1089.robot.commands.TestLIDAR;
import org.usfirst.frc.team1089.util.LIDAR;

/**
 * Subsystem encapsulating manipulator actuators and sensors.
 */
public class Manipulator extends Subsystem {
    private LIDAR lidar;
    private WPI_VictorSPX victorLeft;
    private WPI_VictorSPX victorRight;

    public Manipulator(int canifierID, int pwmChannel, int victorLeftId, int victorRightId) {
        victorLeft = new WPI_VictorSPX(victorLeftId);
        victorRight = new WPI_VictorSPX(victorRightId);

        //TODO Read config for lidarNum rather than than hard code
        lidar = new LIDAR(canifierID, CANifier.PWMChannel.valueOf(pwmChannel), LIDAR.PWMOffset.EQUATION_B);
    }

    @Override
    public void periodic() {
        lidar.updatePWMInput();
    }

    public LIDAR getLidar() {
        return lidar;
    }

    public WPI_VictorSPX getVictorLeft()
    {
        return victorLeft;
    }

    public WPI_VictorSPX getVictorRight()
    {
        return victorRight;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new TestLIDAR());
    }
}
