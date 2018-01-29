package org.usfirst.frc.team1089.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team1089.config.OpenCVSettings;

/**
 * Helper class to markup mats based on rectangles.
 */
public class MarkupMachine {
    private static int strokeWidth;
    private static Scalar boundsColor, crosshairColor;

    public static void configure() {
        strokeWidth = OpenCVSettings.getStrokeWidth();
        boundsColor = OpenCVSettings.getBoundsColor();
        crosshairColor = OpenCVSettings.getCrosshairColor();
    }

    /**
     * Draws a crosshair on the center of the image.
     *
     * @param img the mat to draw on
     */
    public static void drawCrosshair(Mat img) {
        int resX = img.width(), resY = img.height();
        Imgproc.line(
                img,
                new Point(resX / 2.0, 80),
                new Point(resX / 2.0, resY - 80),
                crosshairColor,
                strokeWidth
        );

        Imgproc.line(
                img,
                new Point(80, resY / 2.0),
                new Point(resX - 80, resY / 2.0),
                crosshairColor,
                strokeWidth
        );
    }

    /**
     * Draws the bounding boxes of all the contours in the image.
     *
     * @param img        the mat to draw on
     * @param rectangles the rectangles to mark up onto the mat
     */
    public static void drawContours(Mat img, BoundingRect... rectangles) {
        for (BoundingRect rect : rectangles) {
            Imgproc.rectangle(
                    img,
                    rect.tl(),
                    rect.br(),
                    boundsColor,
                    strokeWidth
            );

            Imgproc.line(
                    img,
                    new Point(rect.getCenterX(), rect.getCenterY() - 10),
                    new Point(rect.getCenterX(), rect.getCenterY() + 10),
                    boundsColor,
                    strokeWidth
            );

            Imgproc.line(
                    img,
                    new Point(rect.getCenterX() - 10, rect.getCenterY()),
                    new Point(rect.getCenterX() + 10, rect.getCenterY()),
                    boundsColor,
                    strokeWidth
            );
        }
    }
}
