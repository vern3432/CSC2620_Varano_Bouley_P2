package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PhotoEditorGUI extends JFrame {

  private JLabel toolStatusLabel; //  JLabel to display currently selected tool

  private String sidebarStatus = "Paint"; // Initialized sidebarStatus
  private String saveDirectory = "";
  private String loadedImageDirectory = "";
  private boolean Undo = false;
  private Color selectedColor = Color.BLACK;
  private JButton colorPickerButton;

  public BufferedImage loadImage(String filename) {
    BufferedImage image = null;
    try {
      image =
        ImageIO.read(
          ImageLoader.class.getResourceAsStream("/icon/" + filename)
        );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return image;
  }

  // Components
  private JButton saveButton;
  private JButton loadButton;
  private JButton undoButton;
  private JButton paintButton;
  private JButton fillButton;
  private JButton textButton;
  // private JButton filterButton;
  private JButton selectToolButton;
  private JButton straightLineMenuItem;

  //drawing components
  private BufferedImage image;
  private JPanel drawingPanel;
  private Point startPoint;
  private Point endPoint;
  private List<Line> lines = new ArrayList<>();
  private boolean isDrawing = false;
  private boolean fillBucketMode = false;
  private boolean drawStraightLineMode = false;
  private int fillTolerance = 10; // changes this for sensitivity of bucket fill

  public PhotoEditorGUI() {
    // set up the JFrame
    setTitle("Photo Editor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(800, 600));

    // create components
    loadButton = createLoadButton("folder.png", "Load", "type");
    saveButton = createSaveButton("saveicon.png", "Save", "type");
    undoButton = createButton("undo_topbar.png", "Undo");
    paintButton = createPaintButton("paintbrush.png", "Paint");
    fillButton = createBucketButton("paintbucketsidebar.png", "Fill");
    textButton = createButton("text_feild.png", "Text");
    // filterButton = createButton("saveicon.png", "Filter");
    selectToolButton = createButton("select_tool_box.png", "Select Tool");
    straightLineMenuItem =
      createStraightButton("straightLine.png", "Straigt Line Tool"); // Initialize colorPickerButton

    colorPickerButton = new JButton("Select Color"); // Initialize colorPickerButton

    // Set up layout using GridBagLayout
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(saveButton);
    topPanel.add(loadButton);
    undoButton.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (!lines.isEmpty()) {
            lines.remove(lines.size() - 1); // remove the last drawn line
            drawingPanel.repaint(); // repaint the drawing panel to reflect the change
          }
        }
      }
    );
    topPanel.add(undoButton);

    mainPanel.add(topPanel, BorderLayout.NORTH);
    toolStatusLabel = new JLabel("Selected Tool: " + sidebarStatus);
    toolStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    toolStatusLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); //  some padding to the toolar
    mainPanel.add(toolStatusLabel, BorderLayout.SOUTH);

    // add sidebar
    JPanel sidebarPanel = new JPanel();
    sidebarPanel.setLayout(new GridLayout(0, 1));
    sidebarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    sidebarPanel.setPreferredSize(new Dimension(70, 70));

    sidebarPanel.add(paintButton);
    sidebarPanel.add(fillButton);
    sidebarPanel.add(textButton);
    // sidebarPanel.add(filterButton);
    sidebarPanel.add(selectToolButton);
    sidebarPanel.add(straightLineMenuItem);
    colorPickerButton.addActionListener(new ColorPickerListener()); // add ActionListener to colorPickerButton
    colorPickerButton.setBackground(selectedColor); // Set initial background color of colorPickerButton

    JButton Filter = createFilterButton("filter2.png", "Apply Filter");

    sidebarPanel.add(Filter);
    sidebarPanel.add(colorPickerButton);

    mainPanel.add(sidebarPanel, BorderLayout.EAST);

    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");

    openMenuItem.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JFileChooser fileChooser = new JFileChooser();
          int result = fileChooser.showOpenDialog(PhotoEditorGUI.this);
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
    );

    JMenuItem saveMenuItem = new JMenuItem("Save As");
    saveMenuItem.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (image != null) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(PhotoEditorGUI.this);

            if (result == JFileChooser.APPROVE_OPTION) {
              File outputFile = fileChooser.getSelectedFile();

              // Create a new BufferedImage to draw the lines on
              BufferedImage combinedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
              );
              Graphics2D g2d = combinedImage.createGraphics();
              g2d.drawImage(image, 0, 0, null);

              // Draw the lines on the combined image
              g2d.setColor(selectedColor);
              for (Line line : lines) {
                g2d.drawLine(
                  line.start.x,
                  line.start.y,
                  line.end.x,
                  line.end.y
                );
              }
              g2d.dispose(); // Dispose the Graphics2D object
              try {
                ImageIO.write(combinedImage, "png", outputFile);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            }
          }
        }
      }
    );
    fileMenu.add(openMenuItem);
    fileMenu.add(saveMenuItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);

    JMenu toolMenu = new JMenu("Tools");

    JMenuItem freehandMenuItem = new JMenuItem("Freehand");
    freehandMenuItem.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          fillBucketMode = false;
          drawStraightLineMode = false;
        }
      }
    );
    JMenuItem fillBucketMenuItem = new JMenuItem("Fill Bucket");
    fillBucketMenuItem.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          fillBucketMode = true;
          drawStraightLineMode = false;
        }
      }
    );
    JMenuItem straightLineMenuItem = new JMenuItem("Straight Line");
    straightLineMenuItem.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          drawStraightLineMode = true;
          fillBucketMode = false;
        }
      }
    );
    toolMenu.add(freehandMenuItem);
    toolMenu.add(fillBucketMenuItem);
    toolMenu.add(straightLineMenuItem);
    menuBar.add(toolMenu);

    drawingPanel =
      new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          if (image != null) {
            g.drawImage(image, 0, 0, this);
          }
          if (!fillBucketMode) {
            for (Line line : lines) {
              line.draw(g);
            }
          }
        }
      };
    drawingPanel.addMouseListener(
      new MouseAdapter() {
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
              lines.add(new Line(startPoint, endPoint, selectedColor));
            } else {
              lines.add(new Line(startPoint, startPoint, selectedColor)); // add single point for freehand drawing
            }
            isDrawing = false;
            drawingPanel.repaint();
          }
        }
      }
    );
    drawingPanel.addMouseMotionListener(
      new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
          if (isDrawing && !fillBucketMode) {
            endPoint = e.getPoint();
            if (!drawStraightLineMode) {
              lines.add(new Line(startPoint, endPoint, selectedColor));
            }
            startPoint = endPoint;
            drawingPanel.repaint();
          }
        }
      }
    );

    mainPanel.add(drawingPanel);

    // add main panel to content pane
    getContentPane().add(mainPanel);

    // Pack and set visible
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private class Line {

    Point start;
    Point end;
    Color color;

    public Line(Point start, Point end, Color color) {
      this.start = start;
      this.end = end;
      this.color = color; // Store the color of the line
    }

    public void draw(Graphics g) {
      g.setColor(color); // Set the color before drawing the line
      g.drawLine(start.x, start.y, end.x, end.y);
    }
  }

  private void fillBucket(Point point) {
    if (image != null) {
      int targetColor = image.getRGB(point.x, point.y);
      int replacementColor = selectedColor.getRGB();
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

    return (
      deltaRed <= tolerance && deltaGreen <= tolerance && deltaBlue <= tolerance
    );
  }

  class ColorPickerListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      Color newColor = JColorChooser.showDialog(
        PhotoEditorGUI.this,
        "Select a Color",
        selectedColor
      );
      if (newColor != null) {
        selectedColor = newColor;
        colorPickerButton.setBackground(selectedColor); // change background color of colorPickerButton
      }
    }
  }

  private JButton createButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
          toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
        }
      }
    );

    return button;
  }

  private JButton createPaintButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
          toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          fillBucketMode = false;
          drawStraightLineMode = false;
        }
      }
    );

    return button;
  }

  private JButton createBucketButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
          toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          fillBucketMode = true;
          drawStraightLineMode = false;
        }
      }
    );

    return button;
  }

  public JButton createFilterButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JPopupMenu popupMenu = new JPopupMenu();
          JMenuItem grayscaleItem = new JMenuItem("Grayscale");
          JMenuItem invertColorsItem = new JMenuItem("Inverted Colors");
          JMenuItem blurItem = new JMenuItem("Blur with Gaussian Noise");
          JMenuItem staticItem = new JMenuItem("Static with Perlin Noise");
          JMenuItem cartoonifyItem = new JMenuItem("Cartoonify");

          grayscaleItem.addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                System.out.println("View the image in greyscale");
              }
            }
          );

          invertColorsItem.addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                System.out.println("View the image with inverted colors");
              }
            }
          );

          blurItem.addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                System.out.println("Adding blur with Gaussian Noise");
              }
            }
          );

          staticItem.addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                System.out.println("Adding static with Perlin Noise");
              }
            }
          );

          cartoonifyItem.addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                System.out.println("Cartoonify");
              }
            }
          );

          popupMenu.add(grayscaleItem);
          popupMenu.add(invertColorsItem);
          popupMenu.add(blurItem);
          popupMenu.add(staticItem);
          popupMenu.add(cartoonifyItem);

          popupMenu.show(button, button.getWidth() / 2, button.getHeight() / 2);
        }
      }
    );
    return button;
  }

  private JButton createStraightButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
          toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          fillBucketMode = false;
          drawStraightLineMode = true;
        }
      }
    );

    return button;
  }

  private JButton createLoadButton(
    String iconPath,
    String toolTipText,
    String type
  ) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image files",
            ImageIO.getReaderFileSuffixes()
          );
          System.out.println(imageFilter);
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.addChoosableFileFilter(imageFilter);
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

          int returnValue = fileChooser.showOpenDialog(null);

          if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println(
              "Selected File: " + selectedFile.getAbsolutePath()
            );
          } else {
            System.out.println("No file selected");
          }
        }
      }
    );

    return button;
  }

  private JButton createSaveButton(
    String iconPath,
    String toolTipText,
    String type
  ) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          int returnValue = fileChooser.showOpenDialog(null);

          if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            System.out.println(
              "Selected Directory: " + selectedDirectory.getAbsolutePath()
            );
          } else {
            System.out.println("No directory selected");
          }
        }
      }
    );

    return button;
  }

  public static void main(String[] args) {
    // Create and show the GUI
    SwingUtilities.invokeLater(
      new Runnable() {
        @Override
        public void run() {
          new PhotoEditorGUI();
        }
      }
    );
  }
}
