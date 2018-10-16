package org.usfirst.frc.team1089.robot.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * Navigational sensor with both a gyro and accelerometer
 */
public class NavX extends AHRS implements Gyro {
    public NavX(SerialPort.Port serial_port_id) {
        super(serial_port_id);
    }

    /**
     * The NavX does not have a method to calibrate. It calibrates automatically.
     */
    @Deprecated
    @Override
    public void calibrate() {
        return;
    }

    //TODO See if the NavX and Gyro's definition of a positive and negative angle match. If they do not match, then have getAngle() return -super.getAngle();

    public double getAngle() {
        return super.getAngle();
    }

    public double getRate() {
        return super.getRate();
    }

    public void reset() {
        super.reset();
}
}
