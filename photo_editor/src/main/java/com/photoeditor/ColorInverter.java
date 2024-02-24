package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ColorInverter extends JFrame {

  private BufferedImage originalImage;
  private BufferedImage invertedImage;
  private JLabel imageLabel;
  private JButton applyButton;

  public ColorInverter() {
    setTitle("Color Inverter App");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    imageLabel = new JLabel();
    mainPanel.add(imageLabel, BorderLayout.CENTER);

    applyButton = new JButton("Invert Colors");
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
        invertedImage = invertColors(originalImage);
        ImageIcon icon = new ImageIcon(invertedImage);
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

  public BufferedImage invertColors(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage invertedImage = new BufferedImage(
      width,
      height,
      BufferedImage.TYPE_INT_RGB
    );
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        int invertedRGB = ~rgb;
        invertedImage.setRGB(x, y, invertedRGB);
      }
    }
    return invertedImage;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
      new Runnable() {
        public void run() {
          new ColorInverter().setVisible(true);
        }
      }
    );
  }
}
