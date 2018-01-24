package org.usfirst.frc.team1089.util;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.interfaces.Gyro;

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
