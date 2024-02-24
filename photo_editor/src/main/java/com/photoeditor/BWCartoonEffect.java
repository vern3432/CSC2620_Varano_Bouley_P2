package com.photoeditor;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BWCartoonEffect extends JFrame {

    private JLabel imageLabel;
    private BufferedImage inputImage;

    public BWCartoonEffect() {
        setTitle("Cartoon Effect App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        JButton openButton = new JButton("Open Image");
        JButton cartoonifyButton = new JButton("Cartoonify");
        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 20);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        inputImage = ImageIO.read(selectedFile);
                        displayImage(inputImage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cartoonifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputImage != null) {
                    int threshold = thresholdSlider.getValue();
                    BufferedImage outputImage = applyBWCartoonEffect(inputImage, threshold);
                    displayImage(outputImage);
                } else {
                    JOptionPane.showMessageDialog(null, "Please open an image first", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        thresholdSlider.setMajorTickSpacing(5);
        thresholdSlider.setMinorTickSpacing(1);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);

        controlPanel.add(openButton);
        controlPanel.add(cartoonifyButton);
        controlPanel.add(new JLabel("Threshold:"));
        controlPanel.add(thresholdSlider);

        imageLabel = new JLabel();
        JScrollPane scrollPane = new JScrollPane(imageLabel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public BufferedImage applyBWCartoonEffect(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = gray.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        BufferedImage thresholdImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = gray.getRGB(x, y);
                int grayValue = (rgb >> 16) & 0xFF;
                int alpha = (rgb >> 24) & 0xFF;
                int newRGB = alpha << 24 | grayValue << 16 | grayValue << 8 | grayValue;
                thresholdImage.setRGB(x, y, newRGB);
            }
        }
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int thresholdRGB = thresholdImage.getRGB(x, y);
                int originalRGB = image.getRGB(x, y);
                int thresholdGrayValue = (thresholdRGB >> 16) & 0xFF;
                int originalRed = (originalRGB >> 16) & 0xFF;
                int originalGreen = (originalRGB >> 8) & 0xFF;
                int originalBlue = originalRGB & 0xFF;
                if (thresholdGrayValue > threshold) {
                    outputImage.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    outputImage.setRGB(x, y, originalRGB);
                }
            }
        }
        return outputImage;
    }

    private void displayImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BWCartoonEffect app = new BWCartoonEffect();
                app.setVisible(true);
            }
        });
    }
}
