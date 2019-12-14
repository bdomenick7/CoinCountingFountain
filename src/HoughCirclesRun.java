//Original code from https://docs.opencv.org/3.4/d4/d70/tutorial_hough_circle.html

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class HoughCirclesRun
{
    // The radius measurement is dependant on distance from camera. Radii will be filled in after mounted camera tests.
    // private static final int pennyRadius = 29;
    // private static final int nickleRadius = 30;
    // private static final int dimeRadius = 22;
    // private static final int quarterRadius = 45;
    // private static final int dollarRadius = 53;

    // public double pennyTotal = 0.0;
    // public double nickleTotal = 0.0;
    // public double dimeTotal = 0.0;
    // public double quarterTotal = 0.0;
    // public double dollarTotal = 0.0;
    // public double fountainTotal = 0.0;

    public void run(String[] args) {
        String default_file = "C:\\Users\\cryst\\Desktop\\Group Project Images\\sanpshot.jpg";
        String filename = ((args.length > 0) ? args[0] : default_file);
        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 1, 30); // change the last two parameters
        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            System.out.println(radius);
        }
        HighGui.imshow("detected circles", src);
        HighGui.waitKey();
        System.exit(0);
    }
}
/**
public class HoughCircles {
    public static void main(String[] args)
 {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughCirclesRun().run(args);
    }
}
 */