package org.usfirst.frc.team1089.vision;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team1089.config.OpenCVSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic pipeline that filters a color
 * and finds properly-sized contours to go along with it
 */
public class BasicFilterPipeline implements Pipeline<Mat, ArrayList<MatOfPoint>> {

    //Outputs
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

    /**
     * Inputs a mat into the pipeline to be processed
     *
     * @param source0 the mat to process
     */
    public void process(Mat source0) {
        // Step HSV_Threshold0:
        Mat hsvThresholdInput = source0;
        double[] hsvThresholdHue = OpenCVSettings.getHSVThresholdHue();
        double[] hsvThresholdSaturation = OpenCVSettings.getHSVThresholdSat();
        double[] hsvThresholdValue = OpenCVSettings.getHSVThresholdLum();
        hslThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

        // Step Find_Contours0:
        Mat findContoursInput = hsvThresholdOutput;
        boolean findContoursExternalOnly = false;
        findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

        // Step Filter_Contours0:
        ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
        double filterContoursMinArea = 500.0;
        double filterContoursMinPerimeter = 0.0;
        double[] filterContoursWidth = OpenCVSettings.getContoursFilterWidth();
        double[] filterContoursHeight = OpenCVSettings.getContoursFilterHeight();
        double[] filterContoursSolidity = OpenCVSettings.getContoursFilterSolidity();
        double[] filterContoursVertices = OpenCVSettings.getContoursFilterVertices();
        double[] filterContoursRatio = OpenCVSettings.getContoursFilterRatio();
        filterContours(
                filterContoursContours,
                filterContoursMinArea,
                filterContoursMinPerimeter,
                filterContoursWidth,
                filterContoursHeight,
                filterContoursSolidity,
                filterContoursVertices,
                filterContoursRatio,
                filterContoursOutput
        );
    }

    /**
     * This method is a generated getter for the output of a HSV_Threshold.
     * @return Mat output from HSV_Threshold.
     */
    public Mat hsvThresholdOutput() {
        return hsvThresholdOutput;
    }

    /**
     * This method is a generated getter for the output of a Find_Contours.
     * @return ArrayList<MatOfPoint> output from Find_Contours.
     */
    public ArrayList<MatOfPoint> findContoursOutput() {
        return findContoursOutput;
    }

    /**
     * This method is a generated getter for the output of a Filter_Contours.
     * @return ArrayList<MatOfPoint> output from Filter_Contours.
     */
    public ArrayList<MatOfPoint> filterContoursOutput() {
        return filterContoursOutput;
    }

    /**
     * Gets the filtered contours list
     *
     * @param contours array list to contain the contours list
     */
    public void output(ArrayList<MatOfPoint> contours) {
        contours.addAll(filterContoursOutput);
    }

    /**
     * Segment an image based on hue, saturation, and value ranges.
     *
     * @param input The image on which to perform the HSL threshold.
     * @param hue The min and max hue
     * @param sat The min and max saturation
     * @param lum The min and max luminance
     * @param out The image in which to store the output.
     */
    private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum, Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);

        Core.inRange(
                out,
                new Scalar(hue[0], lum[0], sat[0]),
                new Scalar(hue[1], lum[1], sat[1]),
                out
        );
    }

    /**
     * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
     *
     * @param input        The image on which to perform the Distance Transform.
     * @param externalOnly The Transform.
     * @param contours     the list of points
     */
    private void findContours(Mat input, boolean externalOnly, List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        }
        else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }


    /**
     * Filters out contours that do not meet certain criteria.
     * @param inputContours is the input list of contours
     * @param output is the the output list of contours
     * @param minArea is the minimum area of a contour that will be kept
     * @param minPerimeter is the minimum perimeter of a contour that will be kept
     * @param width width threshold
     * @param height height threshold
     * @param solidity the minimum and maximum solidity of a contour
     * @param vertexCount vertex count threshold
     * @param ratio ratio threshold
     */
    private void filterContours(List<MatOfPoint> inputContours, double minArea, double minPerimeter,
                                double[] width, double[] height, double[] solidity, double[] vertexCount,
                                double[] ratio, List<MatOfPoint> output) {
        final MatOfInt hull = new MatOfInt();
        output.clear();

        // Filter loop
        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);
            final Rect bb = Imgproc.boundingRect(contour);

            // Apply width filter
            if (bb.width < width[0] || bb.width > width[1]) continue;

            // Apply height filter
            if (bb.height < height[0] || bb.height > height[1]) continue;

            // Apply area filter
            final double area = Imgproc.contourArea(contour);

            if (area < minArea) continue;

            // Apply perimeter filter
            if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;

            // Apply solidity filter
            Imgproc.convexHull(contour, hull);
            MatOfPoint mopHull = new MatOfPoint();

            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);

            for (int j = 0; j < hull.size().height; j++) {
                int index = (int)hull.get(j, 0)[0];
                double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
                mopHull.put(j, 0, point);
            }

            final double solid = 100 * area / Imgproc.contourArea(mopHull);

            if (solid < solidity[0] || solid > solidity[1]) continue;

            // Apply vertices filter
            if (contour.rows() < vertexCount[0] || contour.rows() > vertexCount[1])	continue;

            // Apply ratio filter
            final double contourRatio = bb.width / (double)bb.height;

            if (contourRatio < ratio[0] || contourRatio > ratio[1]) continue;

            output.add(contour);
        }
    }



}

