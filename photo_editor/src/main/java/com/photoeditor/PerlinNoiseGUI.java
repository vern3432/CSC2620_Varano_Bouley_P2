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


public class PerlinNoiseGUI extends JFrame {
    private JLabel originalImageLabel;
    private JLabel noiseImageLabel;
    private JSlider thresholdSlider;

    public PerlinNoiseGUI() {
        setTitle("Perlin Noise Filter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        originalImageLabel = new JLabel();
        noiseImageLabel = new JLabel();
        thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);

        JButton loadButton = new JButton("Load Image");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage originalImage = ImageIO.read(selectedFile);
                        originalImageLabel.setIcon(new ImageIcon(originalImage));

                        int threshold = thresholdSlider.getValue();
                        System.out.println(threshold);
                        PerlinNoiseFilter filter = new PerlinNoiseFilter(threshold);
                        BufferedImage noiseImage = filter.applyFilter(originalImage);
                        noiseImageLabel.setIcon(new ImageIcon(noiseImage));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(new JLabel("Threshold:"));
        buttonPanel.add(thresholdSlider);

        JPanel imagePanel = new JPanel(new GridLayout(1, 2));
        imagePanel.add(originalImageLabel);
        imagePanel.add(noiseImageLabel);

        add(buttonPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PerlinNoiseGUI();
            }
        });
    }
}
