package com.photoeditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class CartoonifyApp extends JFrame {
    private JLabel originalLabel, cartoonLabel;
    private JButton loadButton, cartoonifyButton;
    private BufferedImage originalImage, cartoonImage;

    public CartoonifyApp() {
        setTitle("Cartoonify App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new GridLayout(1, 2));

        originalLabel = new JLabel();
        cartoonLabel = new JLabel();
        loadButton = new JButton("Load Image");
        cartoonifyButton = new JButton("Cartoonify");

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(loadButton, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(originalLabel), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(cartoonifyButton, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(cartoonLabel), BorderLayout.CENTER);

        add(leftPanel);
        add(rightPanel);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(CartoonifyApp.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        originalImage = ImageIO.read(selectedFile);
                        originalLabel.setIcon(new ImageIcon(originalImage));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        cartoonifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    cartoonImage = cartoonify(originalImage);
                    cartoonLabel.setIcon(new ImageIcon(cartoonImage));
                } else {
                    JOptionPane.showMessageDialog(CartoonifyApp.this, "Please load an image first.");
                }
            }
        });
    }

    // Custom median filtering algorithm to "cartoonify" the image
    private BufferedImage cartoonify(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Apply median filtering to each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] redValues = new int[9];
                int[] greenValues = new int[9];
                int[] blueValues = new int[9];
                int index = 0;

                // Extract RGB values from the surrounding 3x3 neighborhood
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int px = Math.min(Math.max(x + dx, 0), width - 1);
                        int py = Math.min(Math.max(y + dy, 0), height - 1);
                        Color pixel = new Color(img.getRGB(px, py));
                        redValues[index] = pixel.getRed();
                        greenValues[index] = pixel.getGreen();
                        blueValues[index] = pixel.getBlue();
                        index++;
                    }
                }

                // Sort the RGB values to find the median
                Arrays.sort(redValues);
                Arrays.sort(greenValues);
                Arrays.sort(blueValues);

                // Set the median RGB values as the pixel value in the result image
                Color medianColor = new Color(redValues[4], greenValues[4], blueValues[4]);
                result.setRGB(x, y, medianColor.getRGB());
            }
        }

        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CartoonifyApp app = new CartoonifyApp();
                app.setVisible(true);
            }
        });
    }
}

