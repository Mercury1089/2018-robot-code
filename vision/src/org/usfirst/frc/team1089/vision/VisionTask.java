package org.usfirst.frc.team1089.vision;

import edu.wpi.cscore.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Creates a thread capable of processing images from a camera and getting contours.
 *
 * This class also contains a local counter for the number of threads,
 * and handles assigning the ports to the needed {@link edu.wpi.cscore.MjpegServer MjpegServer}s
 */
public class VisionTask implements Runnable {
    private final CvSource RESTREAM_SOURCE;
    private final CvSink IMG_SINK;
    private final Mat IMG;
    private final Pipeline<Mat, ArrayList<MatOfPoint>> PIPELINE;
    private final ArrayList<MatOfPoint> CONTOURS;

    /**
     * Creates a new {@code VisionTask}
     *
     * @param restreamSource  the CvSource to output marked up mats for restreaming
     * @param imgSink         the CvSink to pull mats from
     * @param contourPipeline the pipeline to use for processing
     */
    public VisionTask(CvSource restreamSource, CvSink imgSink, Pipeline<Mat, ArrayList<MatOfPoint>> contourPipeline) {
        RESTREAM_SOURCE = restreamSource;
        IMG_SINK = imgSink;
        PIPELINE = contourPipeline;

        IMG = new Mat();
        CONTOURS = new ArrayList<MatOfPoint>();
    }

    public void run() {
        while (!Thread.interrupted()) {
            // Grab a frame. If it has a frame time of 0, the request timed out.
            // So we just skip the frame and continue instead.
            if (IMG_SINK.grabFrame(IMG) == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + IMG_SINK.getError());
                continue;
            }

            // Process the image.
            PIPELINE.process(IMG);
            PIPELINE.output(CONTOURS);

            // Regardless of whether or not we see anything, we still want to see
            // what we consider the center of the screen.
            MarkupMachine.drawCrosshair(IMG);

            // Only do this part if there are contours to even look at
            if (CONTOURS.size() > 0) {
                // Sort the contours by area
                CONTOURS.sort((MatOfPoint o1, MatOfPoint o2) -> {
                    Rect
                        r1 = Imgproc.boundingRect(o1),
                        r2 = Imgproc.boundingRect(o2);
                    return (int)Math.signum(r2.area() - r1.area());
                });

                // Just get the bounding rect from the first (largest) target.
                // Don't think about it too hard.
                BoundingRect target1 = new BoundingRect(Imgproc.boundingRect(CONTOURS.get(0)));

                // Draw the contours
                MarkupMachine.drawContours(IMG, target1);
            }

            // Restream the marked up image
            // then release the resources within the mat.
            // Might also be a good idea to clear the contours map
            RESTREAM_SOURCE.putFrame(IMG);
            IMG.release();
            CONTOURS.clear();
        }

        // Once you leave the loop, execution is done. Release the resources.
        RESTREAM_SOURCE.free();
        IMG_SINK.free();
    }
}
