package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;

public class PhotoEditorGUI extends JFrame {

  private static class RoundedBorder implements Border {

    private int radius;

    RoundedBorder(int radius) {
      this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
      return new Insets(
        this.radius + 1,
        this.radius + 1,
        this.radius + 2,
        this.radius
      );
    }

    public boolean isBorderOpaque() {
      return true;
    }

    public void paintBorder(
      Component c,
      Graphics g,
      int x,
      int y,
      int width,
      int height
    ) {
      g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
  }

  private JLabel toolStatusLabel; // Added JLabel to display currently selected tool

  private String sidebarStatus = "Paint"; // Initialized sidebarStatus
  private String saveDirectory = ""; 
  private String loadedImageDirectory = ""; 
  private boolean Undo = false;
  private Color selectedColor = Color.BLACK;
  private JButton colorPickerButton; // Added colorPickerButton instance variable


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
  private JButton filterButton;
  private JButton selectToolButton;
  private JButton straightLineMenuItem;




  //drawing components 
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


  public PhotoEditorGUI() {
    String sidebarStatus = "Paint";
    // Set up the JFrame
    setTitle("Photo Editor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(800, 600));

    // "folder.jpg"
    // "mirror_sidebar2.png"
    // "mirror_sidebar.png"
    // "paint_brush_sidebar.png"
    // "paint_bucket_sidebar.png"
    // "saveicon.png"
    // "select_tool_box.png"
    // "select_tool_lasso.png"
    // "text_feild.png"
    // "undo_topbar.png"

    // Initialize components
    //.setBounds(1, y_pos, 30, 25).setBorder(new RoundedBorder(10));;
    loadButton = createLoadButton("folder.png", "Load", "type");
    saveButton = createSaveButton("saveicon.png", "Save","type");
    undoButton = createButton("undo_topbar.png", "Undo");
    paintButton = createButton("saveicon.png", "Paint");
    fillButton = createButton("paintbucketsidebar.png", "Fill");
    textButton = createButton("text_feild.png", "Text");
    filterButton = createButton("saveicon.png", "Filter");
    selectToolButton = createButton("select_tool_box.png", "Select Tool");
    straightLineMenuItem = createButton("straightLine.png","Straigt Line Tool"); // Initialize colorPickerButton
    
    
    colorPickerButton = new JButton("Select Color"); // Initialize colorPickerButton

    // Set up layout using GridBagLayout
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(saveButton);
    topPanel.add(loadButton);
    topPanel.add(undoButton);

    mainPanel.add(topPanel, BorderLayout.NORTH);
    toolStatusLabel = new JLabel("Selected Tool: " + sidebarStatus);
    toolStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    toolStatusLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add some padding
    mainPanel.add(toolStatusLabel, BorderLayout.SOUTH);

    // Add sidebar
    JPanel sidebarPanel = new JPanel();
    sidebarPanel.setLayout(new GridLayout(0, 1));
    sidebarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    sidebarPanel.setPreferredSize(new Dimension(70, 70));

    sidebarPanel.add(paintButton);
    sidebarPanel.add(fillButton);
    sidebarPanel.add(textButton);
    sidebarPanel.add(filterButton);
    sidebarPanel.add(selectToolButton);
    sidebarPanel.add(straightLineMenuItem);
    colorPickerButton.addActionListener(new ColorPickerListener()); // Add ActionListener to colorPickerButton
    colorPickerButton.setBackground(selectedColor); // Set initial background color of colorPickerButton
    sidebarPanel.add(colorPickerButton);

    mainPanel.add(sidebarPanel, BorderLayout.EAST);

    JMenuBar menuBar = new JMenuBar();

        
    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");


          openMenuItem.addActionListener(new ActionListener() {
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
          });
          JMenuItem saveMenuItem = new JMenuItem("Save As");
          saveMenuItem.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  if (image != null) {
                      JFileChooser fileChooser = new JFileChooser();
                      int result = fileChooser.showSaveDialog(PhotoEditorGUI.this);
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


















    // Add main panel to content pane
    getContentPane().add(mainPanel);

    // Pack and set visible
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  class ColorPickerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(PhotoEditorGUI.this, "Select a Color", selectedColor);
        if (newColor != null) {
            selectedColor = newColor;
            colorPickerButton.setBackground(selectedColor); // Change background color of colorPickerButton
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

    button.setToolTipText(toolTipText);

    button.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          button.setBackground(Color.LIGHT_GRAY); // Change background color on hover
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
          button.setBackground(Color.LIGHT_GRAY); // Change background color on hover
        }

        @Override
        public void mouseExited(MouseEvent e) {
          button.setBackground(UIManager.getColor("Button.background")); // Reset background color when mouse exits
        }

        public void mouseClicked(MouseEvent e) {
          FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
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
          button.setBackground(Color.LIGHT_GRAY); // Change background color on hover
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
