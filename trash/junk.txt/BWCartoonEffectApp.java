package com.photoeditor;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class BWCartoonEffectApp extends JFrame {
    private BufferedImage originalImage;
    private BufferedImage displayedImage;
    private JLabel imageLabel;
    private JSlider thresholdSlider;
    private JButton applyFilterButton;

    public BWCartoonEffectApp() {
        setTitle("Cartoon Effect App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Load the original image using a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose an image file");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                originalImage = ImageIO.read(fileChooser.getSelectedFile());
                displayedImage = originalImage;
                displayImage(displayedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected. Exiting application.");
            System.exit(0);
        }

        // Threshold slider
        thresholdSlider = new JSlider(0, 50, 5);
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);

        // Apply filter button
        applyFilterButton = new JButton("Apply Filter");
        applyFilterButton.addActionListener(e -> applyFilter());

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Threshold:"));
        controlPanel.add(thresholdSlider);
        controlPanel.add(applyFilterButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void displayImage(BufferedImage image) {
        if (imageLabel != null) {
            remove(imageLabel);
        }
        ImageIcon icon = new ImageIcon(image);
        imageLabel = new JLabel(icon);
        add(imageLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void applyFilter() {
        // Apply cartoon effect and display the result
        int threshold = thresholdSlider.getValue();
        BufferedImage cartoonImage = applyCartoonEffect(originalImage, threshold);
        displayImage(cartoonImage);
    }

    private BufferedImage applyCartoonEffect(BufferedImage image, int threshold) {
        // Convert image to grayscale
        BufferedImage grayscaleImage = convertToGrayscale(image);

        // Apply median filter to reduce noise
        BufferedImage medianFiltered = medianFilter(grayscaleImage, 3); // 3x3 median filter

        // Apply edge detection using gradient-based method
        BufferedImage edgeDetected = detectEdges(medianFiltered, threshold);

        return edgeDetected;
    }

    private BufferedImage convertToGrayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }

    private BufferedImage medianFilter(BufferedImage image, int size) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        int radius = size / 2;
        int[] pixels = new int[size * size];

        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                image.getRGB(x - radius, y - radius, size, size, pixels, 0, size);
                Arrays.sort(pixels);
                int median = pixels[pixels.length / 2];
                result.setRGB(x, y, median);
            }
        }

        return result;
    }

    private BufferedImage detectEdges(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[][] gradient = new int[width][height];
        int maxGradient = Integer.MIN_VALUE;

        // Compute gradients
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int gx = Math.abs(image.getRGB(x + 1, y) - image.getRGB(x, y));
                int gy = Math.abs(image.getRGB(x, y + 1) - image.getRGB(x, y));
                int grad = gx + gy;
                gradient[x][y] = grad;
                if (grad > maxGradient) {
                    maxGradient = grad;
                }
            }
        }

        // Normalize gradients and set pixel values
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int color = (int) (((double) gradient[x][y] / maxGradient) * 255);
                // Apply threshold
                color = (color < threshold) ? 0 : 255;
                result.setRGB(x, y, new Color(color, color, color).getRGB());
            }
        }

        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BWCartoonEffectApp().setVisible(true);
            }
        });
    }
}