import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;

public class CartoonEffectApp extends JFrame {
    // other components and methods as before...

    private BufferedImage applyCartoonEffect(BufferedImage image) {
        // Convert image to grayscale
        BufferedImage grayscaleImage = convertToGrayscale(image);

        // Apply median filter to reduce noise
        BufferedImage medianFiltered = medianFilter(grayscaleImage, 3); // 3x3 median filter

        // Apply edge detection using gradient-based method
        BufferedImage edgeDetected = detectEdges(medianFiltered);

        return edgeDetected;
    }

    private BufferedImage convertToGrayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }

    private BufferedImage medianFilter(BufferedImage image, int size) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        int radius = size / 2;
        int[] pixels = new int[size * size];

        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                image.getRGB(x - radius, y - radius, size, size, pixels, 0, size);
                Arrays.sort(pixels);
                int median = pixels[pixels.length / 2];
                result.setRGB(x, y, median);
            }
        }

        return result;
    }

    private BufferedImage detectEdges(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[][] gradient = new int[width][height];
        int maxGradient = Integer.MIN_VALUE;

        // Compute gradients
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int gx = Math.abs(image.getRGB(x + 1, y) - image.getRGB(x, y));
                int gy = Math.abs(image.getRGB(x, y + 1) - image.getRGB(x, y));
                int grad = gx + gy;
                gradient[x][y] = grad;
                if (grad > maxGradient) {
                    maxGradient = grad;
                }
            }
        }

        // Normalize gradients and set pixel values
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int color = (int)(((double)gradient[x][y] / maxGradient) * 255);
                // Ensure color value is within the expected range [0, 255]
                color = Math.min(Math.max(color, 0), 255);
                result.setRGB(x, y, new Color(color, color, color).getRGB());
            }
        }

        return result;
    }
}
