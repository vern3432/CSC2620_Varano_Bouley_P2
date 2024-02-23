package com.photoeditor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang3.SystemUtils;

public class CartoonizerApp extends JFrame {

    private JLabel originalImageLabel;
    private JLabel cartoonImageLabel;

    public CartoonizerApp() {
        super("Image Cartoonizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        originalImageLabel = new JLabel();
        cartoonImageLabel = new JLabel();

        JButton loadImageButton = new JButton("Load Image");
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(CartoonizerApp.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    Mat originalImage = Imgcodecs.imread(filePath);
                    Mat cartoonImage = cartoonizeImage(originalImage);
                    displayImages(originalImage, cartoonImage);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadImageButton);

        JPanel imagePanel = new JPanel(new GridLayout(1, 2));
        imagePanel.add(originalImageLabel);
        imagePanel.add(cartoonImageLabel);

        add(buttonPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private Mat cartoonizeImage(Mat originalImage) {
        Mat gray = new Mat();
        Imgproc.cvtColor(originalImage, gray, Imgproc.COLOR_BGR2GRAY);

        Mat blur = new Mat();
        Imgproc.medianBlur(gray, blur, 5);

        Mat edges = new Mat();
        Imgproc.adaptiveThreshold(blur, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 9, 9);

        Mat colorImg = new Mat();
        Imgproc.bilateralFilter(edges, colorImg, 9, 250, 250);

        Mat cartoon = new Mat();
        Core.bitwise_and(colorImg, edges, cartoon);

        return cartoon;
    }

    private void displayImages(Mat originalImage, Mat cartoonImage) {
        ImageIcon originalIcon = new ImageIcon(Mat2BufferedImage(originalImage));
        ImageIcon cartoonIcon = new ImageIcon(Mat2BufferedImage(cartoonImage));

        originalImageLabel.setIcon(originalIcon);
        cartoonImageLabel.setIcon(cartoonIcon);
    }

    private BufferedImage Mat2BufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);

        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb
                byte b;
                for(int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }

        BufferedImage image = new BufferedImage(cols, rows, type);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    public static void main(String[] args) {



        String libName = "";
        if (SystemUtils.IS_OS_WINDOWS) {
            libName = "opencv_java320.dll";
        } else if (SystemUtils.IS_OS_LINUX) {
            libName = "libopencv_java320.so";
        }
        // System.load(new File("./libs/".concat(libName)).getAbsolutePath());



        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new CartoonizerApp();
            }
        });
    }
}

