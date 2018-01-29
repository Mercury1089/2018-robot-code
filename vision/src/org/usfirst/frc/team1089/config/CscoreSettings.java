package org.usfirst.frc.team1089.config;

import java.util.Properties;

/**
 * Settings pertaining to general imaging and publishing
 */
public class CscoreSettings extends Config {
    private static Properties instance;

    public static void initialize() {
        initialize("cscore.properties");
        instance = getInstance();
    }

    /**
     * Gets the resolution of the video to publish
     *
     * @return int array containing width and height of resolution
     */
    public static int[] getResolution() {
        String[] resString = parseArrayValue("video.resolution", ",");
        int[] resolution = {320, 240};

        if (resString.length == 2) {
            resolution[0] = Integer.parseInt(resString[0].trim());
            resolution[1] = Integer.parseInt(resString[1].trim());
        }

        return resolution;
    }

    /**
     * Gets the frames per second of the video to publish
     *
     * @return integer of frames per second
     */
    public static int getFPS() {
        String fpsString = instance.getProperty("video.fps", "20");

        return Integer.parseInt(fpsString.trim());
    }

    /**
     * Gets the ports for the MJPEG servers streaming the PiCam's
     * "raw" and "marked up" feeds, respectively.
     *
     * @return int array containing ports for raw stream and marked up stream
     */
    public static int[] getPiCamPorts() {
        String[] portStrings = parseArrayValue("picam.ports", ",");
        int[] ports = {1186, 1188};

        if (portStrings.length == 2) {
            ports[0] = Integer.parseInt(portStrings[0].trim());
            ports[1] = Integer.parseInt(portStrings[1].trim());
        }

        return ports;
    }

    /**
     * Gets the ports for the MJPEG servers streaming the LifeCam's
     * "raw" and "marked up" feeds, respectively.
     *
     * @return int array containing ports for raw stream and marked up stream
     */
    public static int[] getLifeCamPorts() {
        String[] portStrings = parseArrayValue("lifecam.ports", ",");
        int[] ports = {1186, 1188};

        if (portStrings.length == 2) {
            ports[0] = Integer.parseInt(portStrings[0].trim());
            ports[1] = Integer.parseInt(portStrings[1].trim());
        }

        return ports;
    }

    /**
     * Gets the device ID for the Pi Camera
     *
     * @return int value of device id
     */
    public static int getPiCameraID() {
        return Integer.parseInt(
                instance.getProperty("picam.id", "1")
        );
    }

    /**
     * Gets the device ID for the Lifecam 3000
     *
     * @return int value of device id
     */
    public static int getLifecamID() {
        return Integer.parseInt(
            instance.getProperty("lifecam.id", "0")
        );
    }
}
