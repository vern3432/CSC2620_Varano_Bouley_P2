package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/*
 * The GrayscaleConverter class is a subclass of JFrame. This class applies a grayscale effect on an iamge.
 */
public class GrayscaleConverter extends JFrame {

  private BufferedImage originalImage;
  private BufferedImage grayscaleImage;
  private JLabel imageLabel;
  private JButton applyButton;

  public GrayscaleConverter() {
    setTitle("Grayscale Converter App");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    imageLabel = new JLabel();
    mainPanel.add(imageLabel, BorderLayout.CENTER);

    applyButton = new JButton("Apply Grayscale");
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
        grayscaleImage = convertToGrayscale(originalImage);
        ImageIcon icon = new ImageIcon(grayscaleImage);
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
          "gif");
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
   * @param image
   * @return BufferedImage
   */
  public BufferedImage convertToGrayscale(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage grayscaleImage = new BufferedImage(
        width,
        height,
        BufferedImage.TYPE_BYTE_GRAY);
    java.awt.Graphics g = grayscaleImage.getGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return grayscaleImage;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        new Runnable() {
          public void run() {
            new GrayscaleConverter().setVisible(true);
          }
        });
  }
}
