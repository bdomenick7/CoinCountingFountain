//Code taken from https://www.tutorialspoint.com/javaexamples/snapshot_from_system_camera.htm

//Imports for javafx packages
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

//imports for opencv pacakges
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

//imports for java awt packages
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

//imports for java io packages
import java.io.FileNotFoundException;
import java.io.IOException;

//original class for capturing image with webcam/system cam
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
        //Saving image to desktop folder
        public void saveImage() {
            // Saving the Image
            String file = "C:\\Users\\cryst\\Desktop\\Group Project Images\\snapshot.jpg";

            // Instantiating the imgcodecs class
            Imgcodecs imageCodecs = new Imgcodecs();

            // Saving it again and handling exceptions
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

        //Main
        public static void main(String args[])
        {

            //Original method call to launch webcam image capture, now commented out and not in use
            // launch(args);

            //Method call to run Hough Circle transformation
            new HoughCirclesRun().run(args);
        }
    }