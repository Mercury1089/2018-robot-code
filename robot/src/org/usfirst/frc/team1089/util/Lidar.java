package org.usfirst.frc.team1089.util;
import com.ctre.phoenix.CANifier;

public class Lidar {
    private CANifier canifier;
    private CANifier.PWMChannel pwmChannel;

    public Lidar(int deviceID, CANifier.PWMChannel channel){
        canifier = new CANifier(deviceID);
        pwmChannel = channel;
    }

    //returns the distance (cm)
    public double getDistance(){
        double[] dutyCycle = new double[0];

        //returns [PWM Value, Period]
        canifier.getPWMInput(pwmChannel, dutyCycle);

        //10 us (microseconds) to 1 cm to 1 inch
        return (dutyCycle[0]/10.0)/2.54;
    }

}
