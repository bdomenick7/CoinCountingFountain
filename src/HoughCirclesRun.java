//Original code from https://docs.opencv.org/3.4/d4/d70/tutorial_hough_circle.html

//imports for Apache Commons IO for iteration through folder
import org.apache.commons.io.FileUtils;

//imports for opencv image processing
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.*;

//imports for java io
import java.io.File;
import java.util.Iterator;


//Class that runs the Hough Circle Transformation on the supplied image
public class HoughCirclesRun
{
    //double variable to hold total amount in fountain
    public double fountainTotal = 0.0;

    public void run(String[] args) {
        String folderPath = "C:/GoProImages";

        // Create an iterator to loop through all of the files in the C:/GoProImages folder
        Iterator iterator = FileUtils.iterateFiles(new File(folderPath), null, false);

        // Loop through all of the files and rebuild filepath
        while(iterator.hasNext()){
            String filename = ((File) iterator.next()).getName();

            System.out.println("filename: " + filename);

            String filepath = folderPath + "/" + filename;

            // Load an image
            Mat src = Imgcodecs.imread(filepath, Imgcodecs.IMREAD_COLOR);

            // Check if image is loaded properly
            if( src.empty() ) {
                System.out.println("Error opening image: " + filename);
                System.exit(-1);
            }

            // Resize the image
            Mat resizedImage = new Mat();
            Size scaleSize = new Size(600,450);

            resize(src, resizedImage, scaleSize , 0, 0, INTER_AREA);

            //Use a grey mat to change image from RGB to grayscale
            Mat gray = new Mat();
            Imgproc.cvtColor(resizedImage, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.medianBlur(gray, gray, 5);
            Mat circles = new Mat();
            double minimumDistanceBetweenCircles = gray.rows() / 16.0;
            System.out.println("minimum distance between circles: " + minimumDistanceBetweenCircles);
            Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    minimumDistanceBetweenCircles, // change this value to detect circles with different distances to each other
                    150.0, 20.0, 15, 50); // change the last two parameters
            // (min_radius & max_radius) to detect larger circles
            int numberOfCirclesDetected = circles.cols();
            //Output number of circles detected
            System.out.println("detected " + numberOfCirclesDetected + " circles");
            for (int x = 0; x < numberOfCirclesDetected; x++) {
                double[] circle = circles.get(0, x);
                Point center = new Point(Math.round(circle[0]), Math.round(circle[1]));
                // circle center
                Imgproc.circle(resizedImage, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(circle[2]);
                Imgproc.circle(resizedImage, center, radius, new Scalar(255,0,255), 3, 8, 0 );
                System.out.println(radius);

                //Measurements for coins at height of mounted camera are as follows
                // 16-21 for pennies and dimes
                // 20-22 for nickels
                // 23-25 for quarters
                // 32 for half-dollars
                // 36-38 for dollars

                //Logic to determine coin type based on size and increment total in correct amount
                if (radius >= 17 && radius <= 19)
                {
                    System.out.println("penny!");
                    fountainTotal += 0.01;
                }
                else if (radius >= 20 && radius <= 23)
                {
                    System.out.println("nickel!");
                    fountainTotal += 0.05;
                }
                else if (radius >= 24 && radius <= 27)
                {
                    System.out.println("quarter!");
                    fountainTotal += 0.25;
                }
                else if (radius == 32)
                {
                    System.out.println("half-dollar!");
                    fountainTotal += 0.50;
                }
                else if (radius >= 36 && radius <= 38)
                {
                    System.out.println("dollar!");
                    fountainTotal += 1.00;
                }
            }
            //Output fountain total to screen
            System.out.print("The total donation is $" + fountainTotal);
            if (numberOfCirclesDetected >= 0)
            {
                String windowName = "detected circles";

                // Show the image with the detected circles
                HighGui.imshow(windowName, resizedImage);

                // Wait for the user to press a key/close the window
                HighGui.waitKey();

                // Destroy the window so that another window can be shown
                HighGui.destroyWindow(windowName);
            }
        }
        System.exit(0);
    }
}