package org.usfirst.frc.team1089.config;

import org.opencv.core.Scalar;

import java.util.Properties;

/**
 * Settings pertaining specifically to OpenCV processing.
 */
public class OpenCVSettings extends Config {
    private static Properties instance;
    private static String ARRAY_DELIM = ",";

    public static void initialize() {
        instance = getInstance();
        loadProperties("opencv.properties");
    }

    /**
     * Parses the {@code hsv.hue} property in the opencv config
     * and gets the threshold for the hue
     *
     * @return array containing min and max of hue, from 0 to 180
     */
    public static double[] getHSVThresholdHue() {
        String[] rangeString = parseArrayValue("hsv.hue", ARRAY_DELIM);
        double[] range = {15, 45};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code hsv.saturation} property in the opencv config
     * and gets the threshold for the saturation
     *
     * @return array containing min and max of saturation, from 0 to 100
     */
    public static double[] getHSVThresholdSat() {
        String[] rangeString = parseArrayValue("hsv.saturation", ARRAY_DELIM);

        double[] range = {195, 255};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code hsv.luminance} property in the opencv config
     * and gets the threshold for the luminance
     *
     * @return array containing min and max of luminance, between 0 and 255
     */
    public static double[] getHSVThresholdLum() {
        String[] rangeString = parseArrayValue("hsv.luminance", ARRAY_DELIM);
        double[] range = {155, 255};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code contours.area} property in the opencv config
     * and gets the minimum contour area.
     *
     * @return minimum area size threshold
     */
    public static double getContoursFilterArea() {
        return (Double)instance.getOrDefault("contours.area", 500);
    }

    /**
     * Parses the {@code contours.area} property in the opencv config
     * and gets the minimum contour perimeter.
     *
     * @return minimum perimeter length threshold
     */
    public static double getContoursFilterPerimeter() {
        return (Double)instance.getOrDefault("contours.perimeter", 0);
    }

    /**
     * Parses the {@code contours.width} property in the opencv config
     * and gets the threshold for the contour widths
     *
     * @return array containing min and max of contour widths
     */
    public static double[] getContoursFilterWidth() {
        String[] rangeString = parseArrayValue("contours.width", ARRAY_DELIM);
        double[] range = {0, 1000};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code contours.height} property in the opencv config
     * and gets the threshold for the contour heights
     *
     * @return array containing min and max of contour heights
     */
    public static double[] getContoursFilterHeight() {
        String[] rangeString = parseArrayValue("contours.height", ARRAY_DELIM);
        double[] range = {0, 1000};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code contours.solidity} property in the opencv config
     * and gets the threshold for the contour solidity
     *
     * @return array containing min and max of contour solidity, from 0 to 100
     */
    public static double[] getContoursFilterSolidity() {
        String[] rangeString = parseArrayValue("contours.solidity", ARRAY_DELIM);
        double[] range = {0, 100};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code contours.ratio} property in the opencv config
     * and gets the threshold for the contour ratio (height to width)
     *
     * @return array containing min and max of contour ratio
     */
    public static double[] getContoursFilterRatio() {
        String[] rangeString = parseArrayValue("contours.ratio", ARRAY_DELIM);
        double[] range = {0, 1000};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code contours.vertices} property in the opencv config
     * and gets the threshold for the contour vertices count
     *
     * @return array containing min and max of contour vertices
     */
    public static double[] getContoursFilterVertices() {
        String[] rangeString = parseArrayValue("contours.vertices", ARRAY_DELIM);
        double[] range = {0, 1000000};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code markup.strokeWidth} property in the opencv config
     * and gets the pixel width for the line stroke
     *
     * @return int value for stroke width
     */
    public static int getStrokeWidth() {
        String value = instance.getProperty("markup.strokeWidth", "1");

        return Integer.parseInt(value);
    }
    /**
     * Parses the {@code markup.boundsColor} property in the opencv config
     * and gets the BGR values for the bounds color
     *
     * @return scalar representing BGR values for bounds color
     */
    public static Scalar getBoundsColor() {
        String[] rangeString = parseArrayValue("markup.boundsColor", ARRAY_DELIM);
        double[] color = {0, 0, 255};
        if (rangeString.length == 3) {
            color[0] = Double.parseDouble(rangeString[0].trim());
            color[1] = Double.parseDouble(rangeString[1].trim());
            color[2] = Double.parseDouble(rangeString[2].trim());
        }

        return new Scalar(color);
    }

    /**
     * Parses the {@code markup.crosshairColor} property in the opencv config
     * and gets the BGR values for the crosshair color
     *
     * @return scalar representing BGR values for crosshair color
     */
    public static Scalar getCrosshairColor() {
        String[] rangeString = parseArrayValue("markup.crosshairColor", ARRAY_DELIM);
        double[] color = {255, 255, 255};
        if (rangeString.length == 3) {
            color[0] = Double.parseDouble(rangeString[0].trim());
            color[1] = Double.parseDouble(rangeString[1].trim());
            color[2] = Double.parseDouble(rangeString[2].trim());
        }

        return new Scalar(color);
    }
}
