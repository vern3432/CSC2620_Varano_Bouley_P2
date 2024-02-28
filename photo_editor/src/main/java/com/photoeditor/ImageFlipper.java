package com.photoeditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

/*
 * The Image Flipper class is a subclass of JFrame. This class provides a means of flipping an image horizontal or vertical
 */
public class ImageFlipper extends JFrame {
    private BufferedImage originalImage;
    private JLabel imageLabel;
    private JComboBox<String> flipTypeComboBox;

    public ImageFlipper() {
        setTitle("Image Flipper");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        contentPane.add(imageLabel, BorderLayout.CENTER);

        JButton openButton = new JButton("Open Image");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ImageFlipper.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        originalImage = ImageIO.read(selectedFile);
                        imageLabel.setIcon(new ImageIcon(originalImage));
                        flipTypeComboBox.setEnabled(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        contentPane.add(openButton, BorderLayout.NORTH);

        flipTypeComboBox = new JComboBox<>(new String[]{"Horizontal", "Vertical"});
        flipTypeComboBox.setEnabled(false);
        flipTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    String flipType = (String) flipTypeComboBox.getSelectedItem();
                    BufferedImage flippedImage = flipImage(originalImage, flipType);
                    imageLabel.setIcon(new ImageIcon(flippedImage));
                }
            }
        });
        contentPane.add(flipTypeComboBox, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    
    /** 
     * @param image
     * @param flipType
     * @return BufferedImage
     */
    public BufferedImage flipImage(BufferedImage image, String flipType) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = flippedImage.createGraphics();
        if (flipType.equals("Horizontal")) {
            g.drawImage(image, width, 0, -width, height, null);
        } else if (flipType.equals("Vertical")) {
            g.drawImage(image, 0, height, width, -height, null);
        }
        g.dispose();
        return flippedImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageFlipper();
            }
        });
    }
}
