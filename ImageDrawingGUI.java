import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageDrawingGUI extends JFrame {
    private BufferedImage image;
    private JPanel drawingPanel;
    private Color selectedColor = Color.BLACK;

    public ImageDrawingGUI() {
        setTitle("Image Drawing GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create components
        drawingPanel = new DrawingPanel();
        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(new ChooseImageListener());
        JButton colorPickerButton = new JButton("Select Color");
        colorPickerButton.addActionListener(new ColorPickerListener());

        // Layout components
        JPanel controlPanel = new JPanel();
        controlPanel.add(chooseImageButton);
        controlPanel.add(colorPickerButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(drawingPanel, BorderLayout.CENTER);
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Draw the image
            if (image != null) {
                g2d.drawImage(image, 0, 0, this);
            }

            // Draw pixels
            g2d.setColor(selectedColor);
            // Draw some example pixels
            g2d.fillRect(100, 100, 20, 20);
            g2d.fillRect(200, 200, 20, 20);

            g2d.dispose();
        }
    }

    private class ChooseImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ImageDrawingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    image = ImageIO.read(selectedFile);
                    drawingPanel.repaint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ColorPickerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedColor = JColorChooser.showDialog(ImageDrawingGUI.this, "Select a Color", selectedColor);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageDrawingGUI gui = new ImageDrawingGUI();
            gui.setVisible(true);
        });
    }
}
