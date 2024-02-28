package com.photoeditor;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Random;


/*
 * The FilmBlurWithNoise class is a subclass of JFrame
 */
public class FilmBlurWithNoise extends JFrame {

    private BufferedImage originalImage;
    private BufferedImage blurredImage;
    private JLabel imageLabel;
    private JButton applyButton;

    public FilmBlurWithNoise() {
        setTitle("Film Blur with Noise App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        applyButton = new JButton("Apply Film Blur with Noise");
        applyButton.addActionListener(new ApplyButtonListener());
        mainPanel.add(applyButton, BorderLayout.SOUTH);

        add(mainPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new OpenMenuItemListener());
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private class ApplyButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (originalImage != null) {
                blurredImage = applyFilmBlurWithNoise(originalImage);
                ImageIcon icon = new ImageIcon(blurredImage);
                imageLabel.setIcon(icon);
            }
        }
    }

    private class OpenMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image Files",
                    "jpg",
                    "jpeg",
                    "png",
                    "gif"
            );
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    originalImage = ImageIO.read(selectedFile);
                    ImageIcon icon = new ImageIcon(originalImage);
                    imageLabel.setIcon(icon);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    
    /** 
     * @param image
     * @return BufferedImage
     */
    private BufferedImage applyFilmBlurWithNoise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurredImage = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB
        );

        float[][] FilmKernel = generateFilmKernel(5, 1.4f); // Example kernel size and sigma
        blurredImage = applyConvolution(image, FilmKernel);

        Random random = new Random();
        float noiseIntensity = 20.0f; // Adjust noise intensity as needed
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(blurredImage.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                red += (int) (random.nextGaussian() * noiseIntensity);
                green += (int) (random.nextGaussian() * noiseIntensity);
                blue += (int) (random.nextGaussian() * noiseIntensity);

                red = clamp(red);
                green = clamp(green);
                blue = clamp(blue);

                Color noisyColor = new Color(red, green, blue);
                blurredImage.setRGB(x, y, noisyColor.getRGB());
            }
        }

        return blurredImage;
    }

    private int clamp(int value) {
        return Math.min(Math.max(value, 0), 255);
    }

    private float[][] generateFilmKernel(int size, float sigma) {
        float[][] kernel = new float[size][size];
        float constant = 1 / (2 * (float) Math.PI * sigma * sigma);
        float sum = 0;

        int center = size / 2;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                float exponent = ((x - center) * (x - center) + (y - center) * (y - center)) / (2 * sigma * sigma);
                kernel[x][y] = constant * (float) Math.exp(-exponent);
                sum += kernel[x][y];
            }
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                kernel[x][y] /= sum;
            }
        }

        return kernel;
    }

    private BufferedImage applyConvolution(BufferedImage image, float[][] kernel) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int kernelSize = kernel.length;
        int kernelRadius = kernelSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float red = 0;
                float green = 0;
                float blue = 0;

                for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
                    for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
                        int pixelX = Math.min(Math.max(x + kx, 0), width - 1);
                        int pixelY = Math.min(Math.max(y + ky, 0), height - 1);
                        int pixelRGB = image.getRGB(pixelX, pixelY);
                        Color color = new Color(pixelRGB);
                        float kernelValue = kernel[ky + kernelRadius][kx + kernelRadius];
                        red += color.getRed() * kernelValue;
                        green += color.getGreen() * kernelValue;
                        blue += color.getBlue() * kernelValue;
                    }
                }

                red = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue = Math.min(Math.max(blue, 0), 255);

                Color resultColor = new Color((int) red, (int) green, (int) blue);
                result.setRGB(x, y, resultColor.getRGB());
            }
        }

        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        new FilmBlurWithNoise().setVisible(true);
                    }
                }
        );
    }
}
