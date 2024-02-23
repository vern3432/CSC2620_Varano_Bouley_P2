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

public class CartoonEffectApp extends JFrame {

    private JLabel imageLabel;
    private BufferedImage inputImage;

    public CartoonEffectApp() {
        setTitle("Cartoon Effect App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        JButton openButton = new JButton("Open Image");
        JButton cartoonifyButton = new JButton("Cartoonify");
        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);

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
                    BufferedImage outputImage = applyCartoonEffect(inputImage, threshold);
                    displayImage(outputImage);
                } else {
                    JOptionPane.showMessageDialog(null, "Please open an image first", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        thresholdSlider.setMajorTickSpacing(10);
        thresholdSlider.setMinorTickSpacing(5);
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

    private BufferedImage applyCartoonEffect(BufferedImage image, int threshold) {
        // Apply cartoon effect logic here
        // For simplicity, let's just return the original image
        return image;
    }

    private void displayImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CartoonEffectApp app = new CartoonEffectApp();
                app.setVisible(true);
            }
        });
    }
}
