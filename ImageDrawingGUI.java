import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class ImageDrawingGUI extends JFrame {

    private BufferedImage image;
    private JPanel drawingPanel;
    private Point startPoint;
    private Point endPoint;
    private Color currentColor = Color.BLACK;
    private List<Line> lines = new ArrayList<>();
    private boolean isDrawing = false;
    private boolean fillBucketMode = false;
    private boolean drawStraightLineMode = false;
    private int fillTolerance = 10; // Adjust this for sensitivity

    public ImageDrawingGUI() {
        setTitle("Image Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JMenuBar menuBar = new JMenuBar();

        
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");


        openMenuItem.addActionListener(new ActionListener() {
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
        });
        JMenuItem saveMenuItem = new JMenuItem("Save As");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showSaveDialog(ImageDrawingGUI.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File outputFile = fileChooser.getSelectedFile();
                        try {
                            ImageIO.write(image, "png", outputFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JMenu toolMenu = new JMenu("Tools");
        JMenuItem freehandMenuItem = new JMenuItem("Freehand");
        freehandMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillBucketMode = false;
                drawStraightLineMode = false;
            }
        });
        JMenuItem fillBucketMenuItem = new JMenuItem("Fill Bucket");
        fillBucketMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillBucketMode = true;
                drawStraightLineMode = false;
            }
        });
        JMenuItem straightLineMenuItem = new JMenuItem("Straight Line");
        straightLineMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawStraightLineMode = true;
                fillBucketMode = false;
            }
        });
        toolMenu.add(freehandMenuItem);
        toolMenu.add(fillBucketMenuItem);
        toolMenu.add(straightLineMenuItem);
        menuBar.add(toolMenu);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, this);
                }
                g.setColor(currentColor);
                if (!fillBucketMode) {
                    for (Line line : lines) {
                        g.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
                    }
                }
            }
        };
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (fillBucketMode) {
                    fillBucket(e.getPoint());
                    drawingPanel.repaint();
                } else {
                    startPoint = e.getPoint();
                    isDrawing = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDrawing && !fillBucketMode) {
                    endPoint = e.getPoint();
                    if (drawStraightLineMode) {
                        lines.add(new Line(startPoint, endPoint));
                    } else {
                        lines.add(new Line(startPoint, startPoint)); // Add single point for freehand drawing
                    }
                    isDrawing = false;
                    drawingPanel.repaint();
                }
            }
        });
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing && !fillBucketMode) {
                    endPoint = e.getPoint();
                    if (!drawStraightLineMode) {
                        lines.add(new Line(startPoint, endPoint));
                    }
                    startPoint = endPoint;
                    drawingPanel.repaint();
                }
            }
        });

        add(drawingPanel);
    }

    private class Line {
        Point start;
        Point end;

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
    }

    private void fillBucket(Point point) {
        if (image != null) {
            int targetColor = image.getRGB(point.x, point.y);
            int replacementColor = currentColor.getRGB();
            if (targetColor != replacementColor) {
                floodFill(point.x, point.y, targetColor, replacementColor);
            }
        }
    }

    private void floodFill(int x, int y, int targetColor, int replacementColor) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return;
        }
        if (!isWithinTolerance(image.getRGB(x, y), targetColor, fillTolerance)) {
            return;
        }
        image.setRGB(x, y, replacementColor);
        floodFill(x + 1, y, targetColor, replacementColor);
        floodFill(x - 1, y, targetColor, replacementColor);
        floodFill(x, y + 1, targetColor, replacementColor);
        floodFill(x, y - 1, targetColor, replacementColor);
    }

    private boolean isWithinTolerance(int color1, int color2, int tolerance) {
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;
        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        int deltaRed = Math.abs(red1 - red2);
        int deltaGreen = Math.abs(green1 - green2);
        int deltaBlue = Math.abs(blue1 - blue2);

        return deltaRed <= tolerance && deltaGreen <= tolerance && deltaBlue <= tolerance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageDrawingGUI().setVisible(true);
            }
        });
    }
}
