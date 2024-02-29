package com.photoeditor;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;


/**
 * The GaussianBlur class adds a blur effect to an image.
 */
public class GaussianBlur {

/** 
 * @param image
 * @param radius
 * @param threshold
 * @return BufferedImage
 */
//source for implimentation https://stackoverflow.com/questions/39684820/java-implementation-of-gaussian-blur

public static BufferedImage apply(BufferedImage image, int radius, int threshold) {
    BufferedImage blurredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    blurredImage.setData(image.getData()); // Copying the original image
    return applyGaussianBlur(blurredImage, radius);
}


/** 
 * @param image
 * @param radius
 * @return BufferedImage
 */
private static BufferedImage applyGaussianBlur(BufferedImage image, int radius) {
    int size = radius * 2 + 1;
    float[] matrix = new float[size * size];
    float sigma = radius / 3.0f;
    float sigma22 = 2 * sigma * sigma;
    float sigmaPi2 = (float) (2 * Math.PI * sigma);
    float sqrtSigmaPi2 = (float) Math.sqrt(sigmaPi2);
    int index = 0;
    float total = 0;
    for (int y = -radius; y <= radius; y++) {
        for (int x = -radius; x <= radius; x++) {
            float distance = x * x + y * y;
            matrix[index] = (float) Math.exp(-distance / sigma22) / sqrtSigmaPi2;
            total += matrix[index];
            index++;
        }
    }
    for (int i = 0; i < size * size; i++) {
        matrix[i] /= total;
    }
    Kernel kernel = new Kernel(size, size, matrix);
    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    return op.filter(image, null);
}
}