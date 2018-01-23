package org.usfirst.frc.team1089.main;

import edu.wpi.cscore.*;
import edu.wpi.first.networktables.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Main {
    public static void main(String[] args) {
        // Connect NetworkTables, and get access to the publishing table
//        NetworkTableInstance nt = NetworkTableInstance.create();
//        nt.setServerTeam(1089);
//        nt.startClient();

        final String ROOT = "Vision";
        final Runtime RUNTIME = Runtime.getRuntime();

        final int Res_X = 640;
        final int Res_Y = 480;
        final int FPS = 20;

        // This is the network port you want to stream the raw received image to
        // By rules, this has to be between 1180 and 1190, so 1185 is a good choice
        final int STREAMPORT = 1186;

        // This stores our reference to our mjpeg server for streaming the input image
        // MjpegServer lifecamOutputStream = new MjpegServer("Lifecam_Output_Stream", STREAMPORT);

        //Our CvSource
        CvSource lifecamSource = new CvSource("lifecamSource", VideoMode.PixelFormat.kMJPEG, Res_X, Res_Y, FPS);

        //Our UsbCamera
        UsbCamera lifecam = new UsbCamera("Lifecam_3000", 0);

        //Our CvSink
        CvSink lifecamSink = new CvSink("CvSink_Lifecam");

        //Our NetworkTable
        //NetworkTable lifecamTable = nt.getTable(ROOT + "/CubeVision");

        //Making the Settings for the lifecam
        lifecam.setResolution(Res_X, Res_Y);
        lifecam.setFPS(FPS);
        lifecam.setBrightness(30);
        lifecam.setExposureManual(0);

        //Sets all of the various sources
        // lifecamOutputStream.setSource(lifecamSource);
        lifecamSink.setSource(lifecam);

        //Shutdown procedure

        RUNTIME.addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            // lifecamOutputStream.free();
            lifecamSink.free();
            lifecamSource.free();
            lifecam.free();
        }));

        Mat img = new Mat();
        while (!Thread.interrupted()) {

            // Grab a frame. If it has a frame time of 0, the request timed out.
            // So we just skip the frame and continue instead.
            if (lifecamSink.grabFrame(img) == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + lifecamSink.getError());
                continue;
            }

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