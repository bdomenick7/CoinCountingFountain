/*
Used to test if OpenCV is implemented correctly

code and instructions from: https://medium.com/@aadimator/how-to-set-up-opencv-in-intellij-idea-6eb103c1d45c
 */



import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;

public class TestCV {
    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(1));
        Mat mc5 = m.col(5);
        mc5.setTo(new Scalar(5));
        System.out.println("OpenCV Mat data:\n" + m.dump());
    }
}