package com.photoeditor;

import javax.swing.*;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class FilterButton extends JButton {
    public BufferedImage image;
    PhotoEditorGUI mainGui;

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

    public FilterButton(String iconPath, String toolTipText, BufferedImage image, PhotoEditorGUI mainGui) {
        BufferedImage image2 = loadImage(iconPath);
        System.out.println(iconPath);
        this.mainGui = mainGui;
        Image newimg = image2.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);

        ImageIcon imageIcon = new ImageIcon(newimg);
        this.setToolTipText("Select Filter");
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
                JMenuItem cartoonifyItemBW = new JMenuItem("Cartoonify(B&W)");
                JMenuItem FilmGrane = new JMenuItem("FilmGrane");

                grayscaleItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("View the image in greyscale");
                        BufferedImage gray = convertToGrayscale(mainGui.getImage());
                        mainGui.addCardImageToState("GrayScale", gray);
                        System.out.println("image returned");

                    }
                });

                invertColorsItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("View the image with inverted colors");
                        BufferedImage inverted = convertToGrayscale(mainGui.getImage());
                        ColorInverter colorInverter = new ColorInverter();
                        BufferedImage invertedImage = colorInverter.invertColors(mainGui.getImage());
                        mainGui.addCardImageToState("Inverted Image", invertedImage);
                        System.out.println("image returned");
                        colorInverter.dispose();
                    }
                });

                blurItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Threshold Popup");
                        String input = JOptionPane.showInputDialog(frame, "Enter threshold value(128 Recomended):");
                        try {
                            int threshold = Integer.parseInt(input);
                            System.out.println("Adding blur with Gaussian Noise");
                            GaussianBlur blurObject = new GaussianBlur();
                            BufferedImage blur = blurObject.apply(mainGui.getImage(), 2, threshold);
                            mainGui.addCardImageToState("Blured Image", blur);
                            System.out.println("image returned");
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid input! Please enter an integer.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    }
                });

                staticItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        PerlinNoiseFilter perLinobj = new PerlinNoiseFilter(10);
                        System.out.println("Adding static with Perlin Noise");
                        BufferedImage perlin = perLinobj.applyFilter(mainGui.getImage());
                        mainGui.addCardImageToState("Perlin", perlin);
                        System.out.println("image returned");
                    }
                });

                cartoonifyItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Cartoonify");
                        CartoonEffect cartoon1 = new CartoonEffect();
                        BufferedImage cartoon = cartoon1.applyCartoonEffect(mainGui.getImage(), 124);
                        mainGui.addCardImageToState("Cartoon Image", cartoon);
                        System.out.println("image returned");
                    }
                });
                cartoonifyItemBW.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Cartoonify");
                        BWCartoonEffect cartoon1 = new BWCartoonEffect();
                        BufferedImage cartoon = cartoon1.applyBWCartoonEffect(mainGui.getImage(), 124);
                        mainGui.addCardImageToState("Cartoon Image", cartoon);
                        System.out.println("image returned");
                    }
                });

                JMenuItem saveAsTextMenuItem = new JMenuItem("Save as Text");
                saveAsTextMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("printing" + image);
                        saveAsciiArt(getImage());
                    }
                });
                grayscaleItem.setAccelerator(
                        KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));

                invertColorsItem.setAccelerator(
                        KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));

                blurItem.setAccelerator(
                        KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

                staticItem.setAccelerator(
                        KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));

                cartoonifyItem.setAccelerator(
                        KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK));

                saveAsTextMenuItem.setAccelerator(
                        KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));

                popupMenu.add(grayscaleItem);
                popupMenu.add(invertColorsItem);
                popupMenu.add(blurItem);
                popupMenu.add(staticItem);
                popupMenu.add(cartoonifyItem);
                popupMenu.add(saveAsTextMenuItem);
                popupMenu.add(cartoonifyItemBW);

                popupMenu.show(FilterButton.this, getWidth() / 2, getHeight() / 2);
            }
        });
        // set up key binding for CTRL+F
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK),
                "filterAction");
        getActionMap().put("filterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");
                doClick();
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
                    BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight,
                            BufferedImage.TYPE_INT_RGB);
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
                            JOptionPane.showMessageDialog(null,
                                    "ASCII art saved successfully to " + outputFile.getAbsolutePath());
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
        // FilterButton button = new FilterButton("folder.png", "ToolTipText", image);
    }

    public void setImage(BufferedImage image) {
        if (this.image == null) {
            this.image = image;
            System.out.println("Added Image");

        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
