package org.usfirst.frc.team1089.config;

import java.util.Properties;

/**
 * Settings pertaining specifically to the vision pipeline.
 */
public class PipelineSettings extends Config {
    private static Properties instance;
    private static String ARRAY_DELIM = ",";

    public static void initialize() {
        initialize("pipeline.properties");
        instance = getInstance();
    }

    /**
     * Parses the {@code hsv.hue} property in the pipeline config
     * and gets the threshold for the hue
     *
     * @return array containing min and max of hue, from 0 to 180
     */
    public static double[] getHSVThresholdHue() {
        String[] rangeString = parseArrayValue("hsv.hue", ARRAY_DELIM);
        double[] range = {20, 40};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code hsv.saturation} property in the pipeline config
     * and gets the threshold for the saturation
     *
     * @return array containing min and max of saturation, from 0 to 100
     */
    public static double[] getHSVThresholdSat() {
        String[] rangeString = parseArrayValue("hsv.saturation", ARRAY_DELIM);

        double[] range = {200, 255};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code hsv.value} property in the pipeline config
     * and gets the threshold for the value
     *
     * @return array containing min and max of value, between 0 and 255
     */
    public static double[] getHSVThresholdVal() {
        String[] rangeString = parseArrayValue("hsv.value", ARRAY_DELIM);
        double[] range = {67, 150};
        if (rangeString.length == 2) {
            range[0] = Double.parseDouble(rangeString[0].trim());
            range[1] = Double.parseDouble(rangeString[1].trim());
        }

        return range;
    }

    /**
     * Parses the {@code contours.area} property in the pipeline config
     * and gets the minimum contour area.
     *
     * @return minimum area size threshold
     */
    public static double getContoursFilterArea() {
        return (Double)instance.getOrDefault("contours.area", 500);
    }

    /**
     * Parses the {@code contours.area} property in the pipeline config
     * and gets the minimum contour perimeter.
     *
     * @return minimum perimeter length threshold
     */
    public static double getContoursFilterPerimeter() {
        return (Double)instance.getOrDefault("contours.perimeter", 0);
    }

    /**
     * Parses the {@code contours.width} property in the pipeline config
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
     * Parses the {@code contours.height} property in the pipeline config
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
     * Parses the {@code contours.solidity} property in the pipeline config
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
     * Parses the {@code contours.ratio} property in the pipeline config
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
     * Parses the {@code contours.vertices} property in the pipeline config
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
}
