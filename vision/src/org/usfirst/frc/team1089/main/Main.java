package org.usfirst.frc.team1089.main;

import edu.wpi.cscore.*;
import org.usfirst.frc.team1089.config.CscoreSettings;
import org.usfirst.frc.team1089.config.OpenCVSettings;
import org.usfirst.frc.team1089.vision.BasicFilterPipeline;
import org.usfirst.frc.team1089.vision.MarkupMachine;
import org.usfirst.frc.team1089.vision.VisionTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Runtime RUNTIME;
    private static final ExecutorService THREAD_POOL;

    static {
        // Get runtime
        RUNTIME = Runtime.getRuntime();

        // Initialize configs beforehand
        CscoreSettings.initialize();
        OpenCVSettings.initialize();
        MarkupMachine.configure();

        // Initialize thread pool
        THREAD_POOL = Executors.newFixedThreadPool(5);
    }

    public static void main(String[] args) {
        // Connect NetworkTables, and get access to the publishing table
//        NetworkTableInstance nt = NetworkTableInstance.create();
//        nt.setServerTeam(1089);
//        nt.startClient();

        //Our NetworkTable
        //NetworkTable lifecamTable = nt.getTable(ROOT + "/CubeVision");

        final String ROOT = "Vision";

        // Set resolution and fps of all output
        int[] resolution = CscoreSettings.getResolution();
        final int RES_X = resolution[0];
        final int RES_Y = resolution[1];
        final int FPS = CscoreSettings.getFPS();

        // This is the network port you want to stream the raw received image to
        // By rules, this has to be between 1180 and 1190
        // To access the stream via GRIP: IP Camera >> http://10.10.89.20:<port>/stream.mjpg
        int[]
            piCamPorts = CscoreSettings.getPiCamPorts(),
            lifecamPorts = CscoreSettings.getLifeCamPorts();

        final int
//            PORT_PICAM_RAW = piCamPorts[0],
//            PORT_PICAM_MARKUP = piCamPorts[1],
            PORT_LIFECAM_RAW = lifecamPorts[0],
            PORT_LIFECAM_MARKUP = lifecamPorts[1];


        // This stores our reference to our mjpeg server for streaming the input image
        // NOTE: Lifecam 3000 is Microsoft's large camera
        // NOTE: Pi Camera Module is the small camera connected by a ribbon cable
        // NOTE: Output streams are needed to restream marked up images
        MjpegServer
            lifecamRawServer = new MjpegServer("LIFECAM_RAW", PORT_LIFECAM_RAW),
            lifecamMarkupServer = new MjpegServer("LIFECAM_MARKUP", PORT_LIFECAM_MARKUP);
//            picamRawServer = new MjpegServer("PICAM_RAW", PORT_PICAM_RAW),
//            picamMarkupServer = new MjpegServer("PICAM_MARKUP", PORT_PICAM_MARKUP);


        // Our CvSources
        // These just act as recievers to mats
        CvSource lifecamMarkupSource = new CvSource("LIFECAM_MARKUP", VideoMode.PixelFormat.kMJPEG, RES_X, RES_Y, FPS);

        // Our UsbCameras
        // Defined just as they are.
        // UsbCamera picam = new UsbCamera("PiCam", 0);
        UsbCamera lifecam = new UsbCamera("Lifecam_3000", CscoreSettings.getLifecamID());

        // Our CvSink
        CvSink lifecamSink = new CvSink("CvSink_Lifecam");

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

        // Add threads to thread pool
        THREAD_POOL.execute(new VisionTask(lifecamMarkupSource, lifecamSink, new BasicFilterPipeline()));

        // Shutdown hook is set up to shutdown
        // the thread pool.
        // Much cleaner exit this way.
        RUNTIME.addShutdownHook(new Thread(() -> {
            System.out.print("Killing threads...");

            THREAD_POOL.shutdownNow();
            while (!THREAD_POOL.isTerminated());

            System.out.println("\n Goodbye.");
        }));
    }
}