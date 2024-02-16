import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImageDrawingApp extends JFrame {
    private ImagePanel imagePanel;

    public ImageDrawingApp() {
        setTitle("Image Drawing App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the image
        ImageIcon icon = new ImageIcon("example.jpg"); // Change "example.jpg" to your image file path
        Image image = icon.getImage();

        // Create and add ImagePanel
        imagePanel = new ImagePanel(image);
        getContentPane().add(imagePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageDrawingApp());
    }
}

class ImagePanel extends JPanel {
    private Image image;
    private Point startPoint;
    private Point endPoint;

    public ImagePanel(Image image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        setOpaque(true);
        setBackground(Color.WHITE);

        // Mouse listeners for drawing lines
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (image != null) {
            g2d.drawImage(image, 0, 0, this);
        }
        if (startPoint != null && endPoint != null) {
            g2d.setColor(Color.RED);
            g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
        g2d.dispose();
    }
}
