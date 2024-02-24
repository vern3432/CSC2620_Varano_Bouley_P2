package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CartoonEffect extends JFrame {

  private BufferedImage originalImage;
  private BufferedImage cartoonImage;
  private JLabel imageLabel;
  private JButton applyButton;
  private JSlider thresholdSlider;

  public CartoonEffect() {
    setTitle("Cartoon Effect App");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    imageLabel = new JLabel();
    mainPanel.add(imageLabel, BorderLayout.CENTER);

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    applyButton = new JButton("Apply Cartoon Effect");
    applyButton.addActionListener(new ApplyButtonListener());
    controlPanel.add(applyButton);

    thresholdSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
    thresholdSlider.setMajorTickSpacing(10);
    thresholdSlider.setMinorTickSpacing(1);
    thresholdSlider.setPaintTicks(true);
    thresholdSlider.setPaintLabels(true);
    controlPanel.add(new JLabel("Threshold:"));
    controlPanel.add(thresholdSlider);
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

  private class ApplyButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (originalImage != null) {
        int threshold = thresholdSlider.getValue();
        cartoonImage = applyCartoonEffect(originalImage, threshold);
        ImageIcon icon = new ImageIcon(cartoonImage);
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

  public BufferedImage applyCartoonEffect(BufferedImage image, int threshold) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage cartoonImage = new BufferedImage(
      width,
      height,
      BufferedImage.TYPE_INT_RGB
    );
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        int gray = (red + green + blue) / 3;
        if (gray < threshold) {
          cartoonImage.setRGB(x, y, Color.BLACK.getRGB());
        } else {
          cartoonImage.setRGB(x, y, rgb);
        }
      }
    }
    return cartoonImage;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
      new Runnable() {
        public void run() {
          new CartoonEffect().setVisible(true);
        }
      }
    );
  }
}
