package com.photoeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

/**
 * The ImageLoader class allows for loading an image from a file
 */
public class ImageLoader {

    
    /** 
     * @param target
     * @return boolean
     */
    public static boolean allNull(Object target) {
        return Arrays.stream(target.getClass()
                .getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .map(f -> getFieldValue(f, target))
                .allMatch(Objects::isNull);
    }

    public static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        ArrayList<String> imageNames = new ArrayList<>(Arrays.asList(
                "folder.png",
                "mirrorsidebar2.png",
                "mirrorsidebar.png",
                "paintbrushsidebar.png",
                "paintbucketsidebar.png",
                "saveicon.png",
                "select_tool_box.png",
                "select_tool_lasso.png",
                "text_feild.png",
                "undo_topbar.png"));

        // Load an image
        BufferedImage image = new BufferedImage(1, 1, 1);
        for (int i = 0; i < imageNames.size(); i++) {
            System.out.println(imageNames.get(i));
            image = loadImage(imageNames.get(i));
            if (image == null) {
                System.out.println(imageNames.get(i) + ":null");

            }
            System.out.println(image);

        }

        // displayImage(image);

        // Display the image in a window
    }

    public static BufferedImage loadImage(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ImageLoader.class.getResourceAsStream("/icon/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
