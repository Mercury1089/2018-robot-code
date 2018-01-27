package org.usfirst.frc.team1089.main;

import edu.wpi.cscore.*;
import edu.wpi.first.networktables.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class Main {
    public static final int RES_X = 640;
    public static final int RES_Y = 480;
    public static final int FPS = 20;
    private static final int LINE_THICKNESS = 2;
    public static double[] centerTotal = {-1, -1};
    public static double[] centerTarget1 = {-1, -1};
    public static double[] centerTarget2 = {-1, -1};
    public static double[] boundsTotal = {-1, -1};
    public static double[] boundsTarget1 = {-1, 1};
    public static double[] boundsTarget2 = {-1, -1};
    public static final Scalar RED = new Scalar(255, 0, 0);
    public static final Scalar WHITE = new Scalar(255, 255, 255);
    public static final Scalar BLUE = new Scalar(0, 0, 255);
    public static void main(String[] args) {
        // Connect NetworkTables, and get access to the publishing table
//        NetworkTableInstance nt = NetworkTableInstance.create();
//        nt.setServerTeam(1089);
//        nt.startClient();

        final String ROOT = "Vision";
        final Runtime RUNTIME = Runtime.getRuntime();

        // This is the network port you want to stream the raw received image to
        // By rules, this has to be between 1180 and 1190
        // To access the stream via GRIP: IP Camera >> http://10.10.89.20:<port>/stream.mjpg
        final int PICAM_PORT = 1186;
        final int LIFECAM_PORT = 1187;
        final int CONTOUR_PORT = 1188;
        MjpegServer contourOutputStream = new MjpegServer("Contour_Out", CONTOUR_PORT); //output from the GRIP code contour finder

        Mat img = new Mat();

        // This stores our reference to our mjpeg server for streaming the input image
        MjpegServer lifecamOutputStream = new MjpegServer("Lifecam_Output_Stream", LIFECAM_PORT); //Large camera from microsoft
        //MjpegServer picamOutputStream = new MjpegServer("PiCam_Out", PICAM_PORT);  //camera on Raspberry Pi

        //Our CvSource
        CvSource lifecamSource = new CvSource("lifecamSource", VideoMode.PixelFormat.kMJPEG, RES_X, RES_Y, FPS);
        CvSource outputFeed = new CvSource("outputfeed", VideoMode.PixelFormat.kMJPEG, RES_X, RES_Y, FPS);
        //Our UsbCamera
        //UsbCamera picam = new UsbCamera("PiCam", 0);
        UsbCamera lifecam = new UsbCamera("Lifecam_3000", 1);

        //Our CvSink
        CvSink lifecamSink = new CvSink("CvSink_Lifecam");

        //Our Grip Pipeline
        GripPipeline grip = new GripPipeline();

        //Our NetworkTable
        //NetworkTable lifecamTable = nt.getTable(ROOT + "/CubeVision");

        //Making the Settings for the lifecam
        lifecam.setResolution(RES_X, RES_Y);
        lifecam.setFPS(FPS);
        lifecam.setBrightness(50);
        lifecam.setExposureManual(50);


        //Sets all of the various sources
        // picamOutputStream.setSource(picam);

        lifecamOutputStream.setSource(lifecam);
        lifecamSink.setSource(lifecam);

        contourOutputStream.setSource(outputFeed);

        //Shutdown procedure

        RUNTIME.addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            lifecamOutputStream.free();
            // picamOutputStream.free();
            contourOutputStream.free();

            lifecamSink.free();
            lifecamSource.free();
            lifecam.free();
        }));
        while (!Thread.interrupted()) {

            // Grab a frame. If it has a frame time of 0, the request timed out.
            // So we just skip the frame and continue instead.
            if (lifecamSink.grabFrame(img) == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + lifecamSink.getError());
                continue;
            }

            grip.process(img);
            ArrayList<MatOfPoint> contours = grip.filterContoursOutput();

            if (contours.size() > 0) {
                contours.sort((MatOfPoint o1, MatOfPoint o2) -> {
                    Rect
                            r1 = Imgproc.boundingRect(o1),
                            r2 = Imgproc.boundingRect(o2);
                    return (int)Math.signum(r2.area() - r1.area());
                });

                Rect target1 = Imgproc.boundingRect(contours.get(0));

                Point topLeft = new Point(
                        target1.x - target1.width,
                        target1.y
                );

                Point bottomRight = new Point(
                        target1.x - target1.width,
                        target1.y - target1. height
                );

                boundsTotal[0] = bottomRight.x - topLeft.x;
                boundsTotal[1] = bottomRight.y - topLeft.y;
                boundsTarget1[0] = target1.br().x - target1.tl().x;
                boundsTarget1[1] = target1.br().y - target1.tl().y;
                centerTotal[0] = topLeft.x + boundsTotal[0] / 2;
                centerTotal[1] = topLeft.y + boundsTotal[1] / 2;
                centerTarget1[0] = target1.tl().x + target1.width / 2.0;
                centerTarget1[1] = target1.tl().y + target1.height / 2.0;

                Imgproc.rectangle(
                        img,
                        target1.br(),
                        target1.tl(),
                        BLUE,
                        LINE_THICKNESS
                );

                Imgproc.rectangle(
                        img,
                        topLeft,
                        bottomRight,
                        RED,
                        LINE_THICKNESS
                );

                Imgproc.line(
                        img,
                        new Point(centerTotal[0], centerTotal[1] - 5),
                        new Point(centerTotal[0], centerTotal[1] + 5),
                        RED,
                        LINE_THICKNESS
                );

                Imgproc.line(
                        img,
                        new Point(centerTotal[0] - 5, centerTotal[1]),
                        new Point(centerTotal[0] + 5, centerTotal[1]),
                        RED,
                        LINE_THICKNESS
                );

                Imgproc.line(
                        img,
                        new Point(Main.RES_X / 2.0, 50),
                        new Point(Main.RES_X / 2.0, Main.RES_Y - 50),
                        WHITE,
                        LINE_THICKNESS
                );

                Imgproc.line(
                        img,
                        new Point(50, Main.RES_Y / 2.0),
                        new Point(Main.RES_X - 50, Main.RES_Y / 2.0),
                        WHITE,
                        LINE_THICKNESS
                );
            }

            outputFeed.putFrame(img);
            img.release();
        }
    }



    public static UsbCamera setUsbCamera(int cameraId, MjpegServer server) {
        // This gets the image from a USB camera
        // Usually this will be on device 0, but there are other overloads
        // that can be used
        UsbCamera camera = new UsbCamera("CoprocessorCamera", cameraId);
        server.setSource(camera);
        return camera;
    }
}
