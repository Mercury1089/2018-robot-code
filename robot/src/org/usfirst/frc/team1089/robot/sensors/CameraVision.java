package org.usfirst.frc.team1089.robot.sensors;

import edu.wpi.first.networktables.NetworkTableInstance;

public class CameraVision {
   static NetworkTableInstance nt = NetworkTableInstance.getDefault();
   //TO DO 
    public static double centerX = nt.getEntry("Center X").getDouble(-2);
    public static double centerY = nt.getEntry("Center Y").getDouble(-2);

    public Double getAngleFromCube(){
        return 30 - centerX / 12;
    }
}
