package org.usfirst.frc.team1089.main;

import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team1089.config.CscoreSettings;
import org.usfirst.frc.team1089.config.PipelineSettings;
import org.usfirst.frc.team1089.vision.BasicFilterPipeline;
import org.usfirst.frc.team1089.vision.BoundingRect;
import org.usfirst.frc.team1089.vision.Pipeline;

import java.util.ArrayList;

public class Main {
    private static final int LINE_THICKNESS = 2;
    public static double[] centerTotal = {-1, -1};
    public static double[] center = {-1, -1};
    public static double[] centerTarget2 = {-1, -1};
    public static double[] boundsTotal = {-1, -1};
    public static double[] bounds = {-1, 1};
    public static double[] boundsTarget2 = {-1, -1};

    public static final Scalar TARGET_COLOR = new Scalar(0, 0, 255);
    public static final Scalar IMG_MARKUP_COLOR = new Scalar(255, 255, 255);

    static {
        // Initialize configs beforehand
        CscoreSettings.initialize();
        PipelineSettings.initialize();
    }
    public static void main(String[] args) {
        // Connect NetworkTables, and get access to the publishing table
//        NetworkTableInstance nt = NetworkTableInstance.create();
//        nt.setServerTeam(1089);
//        nt.startClient();

        final String ROOT = "Vision";
        final Runtime RUNTIME = Runtime.getRuntime();

        // Set resolution and fps of all output
        int[] resolution = CscoreSettings.getResolution();
        final int RES_X = resolution[0];
        final int RES_Y = resolution[1];
        final int FPS = CscoreSettings.getFPS();

        // This is the network port you want to stream the raw received image to
        // By rules, this has to be between 1180 and 1190
        // To access the stream via GRIP: IP Camera >> http://10.10.89.20:<port>/stream.mjpg
        int[] piCamPorts = CscoreSettings.getPiCamPorts();
        int[] lifeCamPorts = CscoreSettings.getLifeCamPorts();

        final int PORT_PICAM_RAW = piCamPorts[0];
        final int PORT_LIFECAM_RAW = lifeCamPorts[0];
        final int PORT_LIFECAM_MARKUP = lifeCamPorts[1];

        // This stores our reference to our mjpeg server for streaming the input image
        // NOTE: Lifecam 3000 is Microsoft's large camera
        // NOTE: Pi Camera Module is the small camera connected by a ribbon cable
        // NOTE: Output streams are needed to restream marked up images
        MjpegServer lifecamRawServer = new MjpegServer("LIFECAM_RAW", PORT_LIFECAM_RAW);
        MjpegServer lifecamMarkupServer = new MjpegServer("LIFECAM_MARKUP", PORT_LIFECAM_MARKUP);
        //MjpegServer picamOutputStream = new MjpegServer("PiCam_Out", PICAM_PORT);


        // Our CvSources
        // These just act as recievers to mats
        CvSource lifecamMarkupSource = new CvSource("LIFECAM_MARKUP", VideoMode.PixelFormat.kMJPEG, RES_X, RES_Y, FPS);

        // Our UsbCameras
        // Defined just as they are.
        // UsbCamera picam = new UsbCamera("PiCam", 0);
        UsbCamera lifecam = new UsbCamera("Lifecam_3000", 1);

        // Our CvSink
        CvSink lifecamSink = new CvSink("CvSink_Lifecam");

        // Our Pipeline
        // It's made to be somewhat generic
        Pipeline grip = new BasicFilterPipeline();

        //Our NetworkTable
        //NetworkTable lifecamTable = nt.getTable(ROOT + "/CubeVision");

        // Configuring lifecam settings
        lifecam.setResolution(RES_X, RES_Y);
        lifecam.setFPS(FPS);
        lifecam.setBrightness(50);
        lifecam.setExposureManual(50);


        // Set sources to proper inputs.
        // Raw servers get direct input from camera,
        // Markup servers get input from CvSources feeding them marked up mats
        lifecamRawServer.setSource(lifecam);
        lifecamSink.setSource(lifecam);

        lifecamMarkupServer.setSource(lifecamMarkupSource);

        // Shutdown hook to release all the occupied resources.
        // Keeps the pi from exploding.
        RUNTIME.addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            lifecamRawServer.free();
            // picamOutputStream.free();
            lifecamMarkupServer.free();

            lifecamSink.free();
            lifecamMarkupSource.free();
            lifecam.free();
        }));

        Mat img = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        while (!Thread.interrupted()) {

            // Grab a frame. If it has a frame time of 0, the request timed out.
            // So we just skip the frame and continue instead.
            if (lifecamSink.grabFrame(img) == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + lifecamSink.getError());
                continue;
            }

            // Process the image.
            grip.process(img);
            grip.output(contours);

            // Regardless of whether or not we see anything, we still want to see
            // what we consider the center of the screen.
            Imgproc.line(
                    img,
                    new Point(RES_X / 2.0, 80),
                    new Point(RES_X / 2.0, RES_Y - 80),
                    IMG_MARKUP_COLOR,
                    LINE_THICKNESS
            );

            Imgproc.line(
                    img,
                    new Point(80, RES_Y / 2.0),
                    new Point(RES_X - 80, RES_Y / 2.0),
                    IMG_MARKUP_COLOR,
                    LINE_THICKNESS
            );

            // Only do this part if there are contours to even look at
            if (contours.size() > 0) {
                // Sort the contours by area
                contours.sort((MatOfPoint o1, MatOfPoint o2) -> {
                    Rect
                            r1 = Imgproc.boundingRect(o1),
                            r2 = Imgproc.boundingRect(o2);
                    return (int)Math.signum(r2.area() - r1.area());
                });

                // Just get the bounding rect from the first (largest) target.
                // Don't think about it too hard.
                BoundingRect target1 = new BoundingRect(Imgproc.boundingRect(contours.get(0)));

                // Draw the contours
                drawContours(img, target1);
            }

            // Restream the marked up image
            // then release the resources within the mat
            lifecamMarkupSource.putFrame(img);
            img.release();
        }
    }

    /**
     * Draws the bounding boxes of all the contours in the image.
     * @param img        the mat to draw on
     * @param rectangles the rectangles to mark up onto the mat
     */
    private static void drawContours(Mat img, BoundingRect... rectangles) {
        for (BoundingRect rect : rectangles) {
            Imgproc.rectangle(
                    img,
                    rect.tl(),
                    rect.br(),
                    TARGET_COLOR,
                    LINE_THICKNESS
            );

            Imgproc.line(
                    img,
                    new Point(rect.getCenterX(), rect.getCenterY() - 10),
                    new Point(rect.getCenterX(), rect.getCenterY() + 10),
                    TARGET_COLOR,
                    LINE_THICKNESS
            );

            Imgproc.line(
                    img,
                    new Point(rect.getCenterX() - 10, rect.getCenterY()),
                    new Point(rect.getCenterX() + 10, rect.getCenterY()),
                    TARGET_COLOR,
                    LINE_THICKNESS
            );
        }
    }
}