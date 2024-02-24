package com.photoeditor;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;

public class FilterButton extends JButton {
    public BufferedImage image;

    public FilterButton(String iconPath, String toolTipText, BufferedImage image) {
        BufferedImage image2 = loadImage(iconPath);
        System.out.println(iconPath);
    
        Image newimg = image2.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
    
        ImageIcon imageIcon = new ImageIcon(newimg);
    
        JButton button = new JButton(imageIcon);
        button.setToolTipText(toolTipText);
    
        this.image = image;

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem grayscaleItem = new JMenuItem("Grayscale");
                JMenuItem invertColorsItem = new JMenuItem("Inverted Colors");
                JMenuItem blurItem = new JMenuItem("Blur with Gaussian Noise");
                JMenuItem staticItem = new JMenuItem("Static with Perlin Noise");
                JMenuItem cartoonifyItem = new JMenuItem("Cartoonify");

                grayscaleItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("View the image in greyscale");
                    }
                });

                invertColorsItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("View the image with inverted colors");
                    }
                });

                blurItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Adding blur with Gaussian Noise");
                    }
                });

                staticItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Adding static with Perlin Noise");
                    }
                });

                cartoonifyItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Cartoonify");
                    }
                });

                JMenuItem saveAsTextMenuItem = new JMenuItem("Save as Text");
                saveAsTextMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("printing"+image);
                        saveAsciiArt(getImage());
                    }
                });
            saveAsTextMenuItem.setAccelerator(
                  KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));

                popupMenu.add(grayscaleItem);
                popupMenu.add(invertColorsItem);
                popupMenu.add(blurItem);
                popupMenu.add(staticItem);
                popupMenu.add(cartoonifyItem);
                popupMenu.add(saveAsTextMenuItem);

                popupMenu.show(FilterButton.this, getWidth() / 2, getHeight() / 2);
            }
        });
    }

    public void saveAsciiArt(BufferedImage image) {
        String resolutionInput = JOptionPane.showInputDialog(null, "Enter resolution percentage (1-100):");
        if (resolutionInput != null) {
            try {
                int resolutionPercentage = Integer.parseInt(resolutionInput);
                if (resolutionPercentage >= 1 && resolutionPercentage <= 100) {
                    int scaledWidth = (int) (image.getWidth() * (resolutionPercentage / 100.0));
                    int scaledHeight = (int) (image.getHeight() * (resolutionPercentage / 100.0));
                    BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = scaledImage.createGraphics();
                    g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
                    g.dispose();

                    StringBuilder asciiBuilder = new StringBuilder();
                    for (int y = 0; y < scaledImage.getHeight(); y++) {
                        for (int x = 0; x < scaledImage.getWidth(); x++) {
                            int pixel = scaledImage.getRGB(x, y);
                            int brightness = getBrightness(pixel);
                            int index = (int) (brightness / 255.0 * (ASCIICHARS.length() - 1));
                            asciiBuilder.append(ASCIICHARS.charAt(index));
                        }
                        asciiBuilder.append("\n");
                    }

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedDirectory = fileChooser.getSelectedFile();
                        File outputFile = new File(selectedDirectory, "ascii_art.txt");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                            writer.write(asciiBuilder.toString());
                            JOptionPane.showMessageDialog(null, "ASCII art saved successfully to " + outputFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error occurred while saving ASCII art");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a resolution percentage between 1 and 100");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }
    }

    private int getBrightness(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = pixel & 0xff;
        return (red + green + blue) / 3;
    }
    public BufferedImage loadImage(String filename) {
    BufferedImage iconImage = null;
    try {
        InputStream inputStream = ImageLoader.class.getResourceAsStream("/icon/" + filename);
        if (inputStream != null) {
            iconImage = ImageIO.read(inputStream);
            inputStream.close();
        } else {
            System.err.println("Icon image not found: " + filename);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return iconImage;
}

    private static final String ASCIICHARS = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        FilterButton button = new FilterButton("folder.png", "ToolTipText", image);
    }

    public void setImage(BufferedImage image) {
        if(this.image==null){
            this.image = image;
            System.out.println("Added Image");

        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
