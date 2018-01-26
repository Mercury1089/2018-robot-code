package org.usfirst.frc.team1089.robot.subsystems;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1089.robot.commands.TestLIDAR;
import org.usfirst.frc.team1089.util.LIDAR;

/**
 * Subsystem encapsulating manipulator actuators and sensors.
 */
public class Manipulator extends Subsystem {
    private LIDAR lidar;

    public Manipulator(int canifierID, int pwmChannel) {
        lidar = new LIDAR(canifierID, CANifier.PWMChannel.valueOf(pwmChannel));

    }

    public LIDAR getLidar() {
        return lidar;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new TestLIDAR());
    }


}
