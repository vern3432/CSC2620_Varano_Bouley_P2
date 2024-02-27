package com.photoeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PerlinNoiseFilter {
    private int threshold;

    public PerlinNoiseFilter(int threshold) {
        this.threshold = threshold;
    }

    public BufferedImage applyFilter(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage staticedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Random random = new Random();

        double noiseIntensity = 0.2; // Adjust noise intensity

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int noise = (int) (random.nextDouble() * 255 * noiseIntensity);

                Color color = new Color(inputImage.getRGB(x, y));

                int red = applyThreshold(color.getRed(), noise);
                int green = applyThreshold(color.getGreen(), noise);
                int blue = applyThreshold(color.getBlue(), noise);

                Color newColor = new Color(red, green, blue);

                staticedImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return staticedImage;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    private int applyThreshold(int colorValue, int noise) {
        int newValue = colorValue + noise;
        if (newValue < 0) {
            newValue = 0;
        } else if (newValue > 255) {
            newValue = 255;
        }
        return Math.abs(newValue - 128) < threshold ? colorValue : newValue;
    }
}
