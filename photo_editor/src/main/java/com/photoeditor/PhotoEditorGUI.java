package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PhotoEditorGUI extends JFrame {

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

    // Initialize components
    saveButton = createButton("resources/icons/save_icon.png", "Save");
    loadButton = createButton("resources/icons/load_icon.png", "Load");
    undoButton = createButton("resources/icons/undo_icon.png", "Undo");
    paintButton = createButton("resources/icons/paint_icon.png", "Paint");
    fillButton = createButton("resources/icons/fill_icon.png", "Fill");
    textButton = createButton("resources/icons/text_icon.png", "Text");
    filterButton = createButton("resources/icons/filter_icon.png", "Filter");
    selectToolButton =
      createButton("resources/icons/select_tool_icon.png", "Select Tool");

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
    sidebarPanel.setPreferredSize(new Dimension(200, 600));

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
    ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    System.out.println(imageIcon.toString());
    Image image = imageIcon.getImage(); // transform it
    Image newimg = image.getScaledInstance(
      120,
      120,
      java.awt.Image.SCALE_SMOOTH
    ); // scale it the smooth way
    imageIcon = new ImageIcon(newimg);

    JButton button = new JButton();
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
