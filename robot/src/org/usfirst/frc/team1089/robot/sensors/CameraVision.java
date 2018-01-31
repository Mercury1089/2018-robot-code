package org.usfirst.frc.team1089.robot.sensors;

import edu.wpi.first.networktables.*;

import java.util.Date;

public class CameraVision {
    private double centerX;
    private long lastUpdate;
    private final long LATENCY_MS = 100L;

    public CameraVision() {
        NetworkTableInstance.getDefault().getTable("CubeVision").getEntry("centerX").addListener(
            (EntryNotification note) -> {
                double val = note.value.getDouble();
                centerX = val != -1 ? val : 0;

                lastUpdate = System.currentTimeMillis();
            }, EntryListenerFlags.kUpdate
        );
    }

    public double getAngleFromCube() {
        double ratio = centerX / 320.0 - 0.5 ;

        double angle = ratio * 48.0;

        return angle;
    }

    public boolean isRecent () {
        return Math.abs(System.currentTimeMillis() - lastUpdate) <= LATENCY_MS;
    }

}
