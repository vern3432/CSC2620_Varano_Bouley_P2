package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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

  public PhotoEditorGUI() {
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
    loadButton = createButton("folder.jpg", "Load");
    saveButton = createButton("saveicon.png", "Save");
    undoButton = createButton("undo_topbar.png", "Undo");
    paintButton = createButton("saveicon.png", "Paint");
    fillButton = createButton("paintbucketsidebar.png", "Fill");
    textButton = createButton("text_feild.png", "Text");
    filterButton = createButton("saveicon.png", "Filter");
    selectToolButton = createButton("select_tool_box.png", "Select Tool");

    // Set up layout using GridBagLayout
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(saveButton);
    topPanel.add(loadButton);
    topPanel.add(undoButton);

    mainPanel.add(topPanel, BorderLayout.NORTH);

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

    mainPanel.add(sidebarPanel, BorderLayout.EAST);

    // Add main panel to content pane
    getContentPane().add(mainPanel);

    // Pack and set visible
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
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
//    Add a UIManger container this enture thing that can have the following: UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
