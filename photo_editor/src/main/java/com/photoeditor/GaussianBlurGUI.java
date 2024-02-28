package com.photoeditor;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * The GaussianBlurBUI is a subclass of JFrame. This class can be used to create a seperate JFrame for this effect which blurs an image/
 */
public class GaussianBlurGUI extends JFrame {

    private BufferedImage originalImage;
    private BufferedImage blurredImage;
    private JLabel imageLabel;
    private JButton applyButton;
    private JTextField radiusTextField;
    private JTextField thresholdTextField;

    public GaussianBlurGUI() {
        setTitle("Gaussian Blur App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());

        applyButton = new JButton("Apply Gaussian Blur");
        applyButton.addActionListener(e -> applyGaussianBlur());
        controlPanel.add(applyButton);

        controlPanel.add(new JLabel("Radius:"));
        radiusTextField = new JTextField("3", 5);
        controlPanel.add(radiusTextField);

        controlPanel.add(new JLabel("Threshold:"));
        thresholdTextField = new JTextField("128", 5);
        controlPanel.add(thresholdTextField);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new OpenMenuItemListener());
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void applyGaussianBlur() {
        if (originalImage != null) {
            try {
                int radius = Integer.parseInt(radiusTextField.getText());
                int threshold = Integer.parseInt(thresholdTextField.getText());
                blurredImage = GaussianBlur.apply(originalImage, radius, threshold);
                ImageIcon icon = new ImageIcon(blurredImage);
                imageLabel.setIcon(icon);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class OpenMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif");
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
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GaussianBlurGUI().setVisible(true));
    }
}
