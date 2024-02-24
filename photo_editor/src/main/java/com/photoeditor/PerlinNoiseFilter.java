package com.photoeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PerlinNoiseFilter {
    private int threshold;

    public PerlinNoiseFilter(int threshold) {
        this.threshold = threshold;
    }

    public BufferedImage applyFilter(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage noiseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double scale = 0.1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double noiseValue = perlinNoise(x * scale, y * scale);
                int noise = (int) (128.0 + 128.0 * noiseValue);

                Color color = new Color(inputImage.getRGB(x, y));

                int red = applyThreshold(color.getRed(), noise);
                int green = applyThreshold(color.getGreen(), noise);
                int blue = applyThreshold(color.getBlue(), noise);

                Color newColor = new Color(red, green, blue);

                noiseImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return noiseImage;
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

    private double perlinNoise(double x, double y) {
        return Math.random() * 2 - 1;
    }
}
