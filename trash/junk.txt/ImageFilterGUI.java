package com.photoeditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageFilterGUI extends JFrame {
    private JLabel originalLabel;
    private JLabel filteredLabel;
    private JButton loadButton;
    private JButton applyFilterButton;

    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    public ImageFilterGUI() {
        setTitle("Image Filter GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalLabel = new JLabel();
        filteredLabel = new JLabel();
        loadButton = new JButton("Load Image");
        applyFilterButton = new JButton("Apply Filter");

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(ImageFilterGUI.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        originalImage = ImageIO.read(file);
                        originalLabel.setIcon(new ImageIcon(originalImage));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        applyFilterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    filteredImage = applyFilter(originalImage);
                    filteredLabel.setIcon(new ImageIcon(filteredImage));
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(applyFilterButton);

        JPanel imagePanel = new JPanel(new GridLayout(1, 2));
        imagePanel.add(originalLabel);
        imagePanel.add(filteredLabel);

        add(buttonPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
    }

    private BufferedImage applyFilter(BufferedImage image) {
        // Apply your filtering logic here
        // For demonstration purposes, let's just return the original image
        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageFilterGUI().setVisible(true);
            }
        });
    }
}
