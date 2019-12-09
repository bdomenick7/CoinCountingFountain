//Code taken from https://www.tutorialspoint.com/javaexamples/snapshot_from_system_camera.htm
//Thanks for the legit code my dude!

import goPro.BacPacStatus;
import goPro.CamFields;
import goPro.GoProApi;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.io.*;

import org.apache.http.client.methods.HttpGet;

public class CameraSnapshotJavaFX extends Application
    {
        Mat matrix = null;

        @Override
        public void start(Stage stage) throws FileNotFoundException, IOException {
            // Capturing the snapshot from the camera
            CameraSnapshotJavaFX obj = new CameraSnapshotJavaFX();
            WritableImage writableImage = obj.captureSnapShot();

            // Saving the image
            obj.saveImage();

            // Setting the image view
            ImageView imageView = new ImageView(writableImage);

            // setting the fit height and width of the image view
            imageView.setFitHeight(4000);
            imageView.setFitWidth(3000);

            // Setting the preserve ratio of the image view
            imageView.setPreserveRatio(true);

            // Creating a Group object
            Group root = new Group(imageView);

            // Creating a scene object
            Scene scene = new Scene(root, 4000, 3000);

            // Setting title to the Stage
            stage.setTitle("Capturing an image");

            // Adding scene to the stage
            stage.setScene(scene);

            // Displaying the contents of the stage
            stage.show();
        }
        public WritableImage captureSnapShot() {
            WritableImage WritableImage = null;

            // Loading the OpenCV core library
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            // Instantiating the VideoCapture class (camera:: 0)
            VideoCapture capture = new VideoCapture(0);

            // Reading the next video frame from the camera
            Mat matrix = new Mat();
            capture.read(matrix);

            // If camera is opened
            if( capture.isOpened()) {
                // If there is next video frame
                if (capture.read(matrix)) {
                    // Creating BuffredImage from the matrix
                    BufferedImage image = new BufferedImage(matrix.width(),
                            matrix.height(), BufferedImage.TYPE_3BYTE_BGR);

                    WritableRaster raster = image.getRaster();
                    DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                    byte[] data = dataBuffer.getData();
                    matrix.get(0, 0, data);
                    this.matrix = matrix;

                    // Creating the Writable Image
                    WritableImage = SwingFXUtils.toFXImage(image, null);
                }
            }
            return WritableImage;
        }
        public void saveImage() {
            // Saving the Image
            String file = "C:\\Users\\cryst\\Desktop\\Group Project Images\\snapshot.jpg";

            // Instantiating the imgcodecs class
            Imgcodecs imageCodecs = new Imgcodecs();

            // Saving it again
            try {
                imageCodecs.imwrite(file, matrix);
            }
            catch (NullPointerException nullPointerException)
            {
                System.out.println("The camera wasn't found!");
            }
            catch (Exception exception)
            {
                System.out.print("Exception thrown:");
                System.out.println(exception);
            }
        }

        /*private static void getGoProBatteryLevel() throws IOException
        {
            System.out.println("Getting GoPro battery level:");
            makeHttpRequest("bacpac/cv");
        }

        private static void getGoProInfo() throws IOException
        {
            System.out.println("Getting GoPro information:");
            makeHttpRequest("gp/gpControl/info");
        }

        private static void getGoProMediaList() throws IOException
        {
            System.out.println("Getting GoPro media list:");
            makeHttpRequest("gp/gpMediaList");
        }

        private static void getGoProMediaMetadata() throws IOException
        {
            System.out.println("Getting GoPro image GOPRO63.jpg:");
            makeHttpRequest("gp/gpMediaMetadata?p=/100GOPRO/GOPRO063.jpg");
        }

        private static void getGoProPresets() throws IOException
        {
            System.out.println("Getting GoPro presets:");
            makeHttpRequest("gp/gpControl/preset/get_presets");
        }

        private static void getGoProStatus() throws IOException
        {
            System.out.println("Getting GoPro status:");
            makeHttpRequest("gp/gpControl/status");
        }

        private static void resetGoProStream() throws IOException
        {
            System.out.println("Restarting GoPro stream?");
            makeHttpRequest("gp/gpControl/execute?p1=gpStream&c1=restart");
        }

        private static void setGoProTo64() throws IOException
        {
            System.out.println("Getting GoPro setting 64:");
            makeHttpRequest("gp/gpControl/setting/64/0");
        }

        // clear analytics --> /gp/gpControl/analytics/clear
        // shutter command --> /gp/gpControl/command/shutter?p=1
        // wireless pair complete --> /gp/gpControl/command/wireless/pair/complete?success=1&deviceName=SM-G920V

        private static void turnGoProCameraOn() throws IOException
        {
            System.out.println("Turning GoPro camera on:");
            makeHttpRequest("gp/gpControl/camera/PV/2");
        }

        public static void makeHttpRequest(String command) throws IOException
        {
            // URL goProURL = new URL("http://10.5.5.9/gp/gpControl/info");
            URL goProURL = new URL("http://10.5.5.9/" + command);
            URLConnection goProConnection = goProURL.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            goProConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

            in.close();
        }*/

        // bacpac commands
        // --> /bacpac/cv (checks battery level)

        // gpControl commands (/gpControl)
        // get info --> /info
        // get status --> /status
        // set up datetime --> /command/setup/date_time
        // execute stream restart --> /execute?p1=gpStream&c1=restart
        // setting 64 -->
        // --> /preset/get_presets
        // --> /camera/PV/2 (turn camera on?)

        // gpMediaList commands
        // --> /gpMediaList

        // gpMediaMetadata
        // (base) --> gpMediaMetadata?p=/100GOPRO/GOPRO063.jpg

        // ORDER OF COMMANDS FROM APP
        // gets status
        // sets the date/time
        // restarts the stream
        // sets something to 64
        // gets jpeg

        // gets jpeg
        // gets status

        public static void main(String args[])
        {
            // Loading the OpenCV core library
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            // GoProApi gopro = new GoProApi("VN5-KNP-znn");

            /*try {
                byte[] response = gopro.getHelper().sendGET("http://10.5.5.9/gp/gpControl/info");
            }
            catch (Exception exception)
            {

            }*/

            /*try {
                getGoProInfo();

                getGoProStatus();

                // sets the date/time

                // resetGoProStream();

                // sets something to 64
                // gets jpeg

                // gets jpeg
                // gets status

                getGoProBatteryLevel();

                getGoProMediaList();

                // getGoProPresets();

                turnGoProCameraOn();
            }
            catch (Exception exception)
            {
                System.out.print("Failed to get GoPro info: ");
                System.out.println(exception);
            }

            System.exit(0);

            try
            {
                BacPacStatus bacPacStatus = gopro.getHelper().getBacpacStatus();

                int cameraPowerStatus = bacPacStatus.getCameraPower();

                if (cameraPowerStatus == 1)
                    System.out.println("Camera power is on.");
                else
                    System.out.println("Camera power is off.");

                int cameraReadyStatus = bacPacStatus.getCameraReady();

                if (cameraReadyStatus == 1)
                    System.out.println("Camera is ready.");
                else
                    System.out.println("Camera is not ready.");

                int cameraModel = bacPacStatus.getCameraModel();

                System.out.print("camera model is ");
                System.out.println(cameraModel);
            }
            catch (Exception exception)
            {
                System.out.println("Failed to get BacPacStatus!");
            }

            CamFields cameraInfo = gopro.getHelper().getCameraInfo();

            System.out.print("camera info: ");
            System.out.println(cameraInfo);

            try
            {
                gopro.powerOnAndStartRecord();
                gopro.stopRecord();
            }
            catch (Exception exception)
            {
                System.out.println("Failed to power on and start recording.");
            }

            //Starting a record in Go Pro
            /*try
            {
                gopro.startRecord();
            }
            catch (Exception exception)
            {
                System.out.println("Failed to start recording!");
            }*/

            //Stopping a record in Go Pro
            /*try
            {
                gopro.stopRecord();
            }
            catch (Exception exception)
            {
                System.out.println("Failed to stop recording!");
            }*/

            // launch(args);
            new HoughCirclesRun().run(args);
            // System.exit(0);
        }
    }