package org.usfirst.frc.team1089.robot.sensors;

import edu.wpi.first.networktables.*;
import org.usfirst.frc.team1089.util.config.SensorsSettings;

import java.awt.*;
import java.util.Date;

public class CameraVision {
    private double centerX;
    private long lastUpdate;

    private final long LATENCY_MS;
    private final Dimension RESOLUTION;
    private final double[] FOV;

    public CameraVision() {
        LATENCY_MS = SensorsSettings.getCameraServerLatency();
        RESOLUTION = SensorsSettings.getCameraResolution();
        FOV = SensorsSettings.getCameraFOV();

        NetworkTableInstance.getDefault().getTable("CubeVision").getEntry("centerX").addListener(
            (EntryNotification note) -> {
                double val = note.value.getDouble();
                centerX = val != -1 ? val : 0;

                lastUpdate = System.currentTimeMillis();
            }, EntryListenerFlags.kUpdate
        );
    }

    public double getAngleFromCube() {
        // Start by defining what we need
        double ratio, angle, halfHRes = RESOLUTION.width / 2.0, hfov = FOV[0];

        // Get ratio of distance from the center
        // to the full half-width of the resolution
        ratio = centerX / halfHRes - 0.5;

        // Multiply ratio by horizontal FOV
        angle = ratio * hfov;

        return angle;
    }

    public boolean isRecent () {
        return Math.abs(System.currentTimeMillis() - lastUpdate) <= LATENCY_MS;
    }

}
