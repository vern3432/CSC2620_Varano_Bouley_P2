package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes.Name;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.STRING;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import java.util.regex.*;

/*
 * The PhotoEditorGUI class is a subclass of JFrame. Provides a mean of initilizing the editing space
 */
public class PhotoEditorGUI extends JFrame {

  private static boolean trace = true; // Turn tracing on and off

  private JLabel toolStatusLabel; // JLabel to display currently selected tool
  private JLabel imageStatusLabel; // Status of the image
  private String sidebarStatus = "Paint"; // Initialized sidebarStatus
  private String saveDirectory = "";
  private String loadedImageDirectory = "";
  private boolean Undo = false;
  private Color selectedColor = Color.BLACK; // Start colot
  private JButton colorPickerButton;
  private String Filename = "";

  // primary image
  public BufferedImage image;
  public HashMap<String, CardObject> GeneratedImages = new HashMap<>();
  public String[] keysImage;
  public String SelectedImage = "";


  /**
   * Combine images
   */
  public void MergerImage() {
    BufferedImage combinedImage = new BufferedImage(
        image.getWidth(),
        image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = combinedImage.createGraphics();
    g2d.drawImage(image, 0, 0, null);

    // Draw the lines on the combined image
    g2d.setColor(selectedColor);
    for (Line line : lines) {
      g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
    }
    image = combinedImage;
    g2d.dispose();
    lines.clear();

  }

  /**
   * Set the current image
   * @param inputimage
   */
  public void setImage(BufferedImage inputimage) {
    this.image = inputimage;

  }

  /**
   * Get the current image
   * @return
   */
  public BufferedImage getImage() {
    return this.image;

  }

  /**
   * Get the combo box for the cards
   * @param name
   */
  public void selectCombobox(String name) {
    GeneratedImages.get(SelectedImage).setAssociatedImag(image);

    setImage(GeneratedImages.get(name).getAssociatedImag());
    drawingPanel.repaint();
    updateImageinfo(this.image);


  }

  JComboBox comboBox = new JComboBox(GeneratedImages.keySet().toArray());


  
  /** 
   * @param input
   * @return String
   */
  public static String incrementTrailingInteger(String input) {
    StringBuilder base = new StringBuilder();
    StringBuilder number = new StringBuilder();

    // Find the base string and the trailing integer
    boolean foundNumber = false;
    for (int i = input.length() - 1; i >= 0; i--) {
        char ch = input.charAt(i);
        if (Character.isDigit(ch)) {
            number.insert(0, ch); // prepend digit to number
            foundNumber = true;
        } else if (foundNumber) {
            base.insert(0, ch); // prepend non-digit character to base
        } else {
            base.insert(0, ch); // prepend non-digit character to base
        }
    }

    // If no integer found, add 2 to the end
    if (number.length() == 0) {
        return input + "2";
    }

    // Increment the integer value
    int intValue = Integer.parseInt(number.toString()) + 1;

    return base.toString() + intValue;
}

  /**
   * Add a new card with an associated image
   * @param Name
   * @param image                        
   * 
   */
  public void addCardImageToState(String Name, BufferedImage image) {

    while(GeneratedImages.containsKey(Name)) {
      Name=incrementTrailingInteger(Name);

    }

      GeneratedImages.put(Name, new CardObject(image, Name));
      UpdatedCombobox();
      selectCombobox(Name);
      SelectedImage = Name;
  }

  /**
   * Update the combo box with a new component
   */
  public void UpdatedCombobox() {

    JComboBox comboBoxtemp = null;
    Component[] components2 = topPanel.getComponents();

    // System.out.println("Current
    // Value"+GeneratedImages.keySet().toArray().toString());
    String[] strings = GeneratedImages.keySet().toArray(new String[GeneratedImages.size()]);
    comboBox = new JComboBox(strings);
    keysImage = strings;

    for (Component component : components2) {
      if (component instanceof JComboBox) {
        comboBoxtemp = (JComboBox) component;
        break;
      }
    }

    if (comboBoxtemp != null) {
      topPanel.remove(comboBoxtemp);
      topPanel.repaint();

    }

    // If combo box clicked
    comboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MergerImage();

        String selectedValue = (String) comboBox.getSelectedItem();
        System.out.println("selecting:" + selectedValue);
        selectCombobox(selectedValue);
        System.out.println("previously selected: " + SelectedImage);
        SelectedImage = selectedValue;

      }

    });

    // If bombo box item selected
    comboBox.addKeyListener(new KeyListener() {
      int selectedIndex = 0;

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T) {
          // Cycle to the next option
          selectedIndex = (selectedIndex + 1) % comboBox.getItemCount();
          comboBox.setSelectedIndex(selectedIndex);
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }
    });

    topPanel.add(comboBox);
    topPanel.revalidate();
    topPanel.repaint();

    System.out.println("Combo box done");
    drawingPanel.repaint();
  }

  /**
   * Loads a new image from a file
   * @param filename
   * @return
   */
  public BufferedImage loadImage(String filename) {
    BufferedImage iconImage = null;
    try {
      // Load the icon image using ClassLoader
      InputStream inputStream = ImageLoader.class.getResourceAsStream("/icon/" + filename);
      if (inputStream != null) {
        iconImage = ImageIO.read(inputStream);
        inputStream.close();
      } else {
        System.err.println("Icon image not found: " + filename);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return iconImage;
  }

  /*
   * Round numbers
   */
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

  private void updateImageinfo(BufferedImage updated) {
    int width = updated.getWidth();
    int height = updated.getHeight();
    int totalPixels = width * height;
    int sumRed = 0;
    int sumGreen = 0;
    int sumBlue = 0;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        sumRed += red;
        sumGreen += green;
        sumBlue += blue;
      }
    }

    // calc average pixel values for each channel
    double avgRed = (double) sumRed / totalPixels;
    double avgGreen = (double) sumGreen / totalPixels;
    double avgBlue = (double) sumBlue / totalPixels;

    imageStatusLabel.setText(
        "File:" +
            Filename +
            " " +
            "avgRed:" +
            round(avgRed, 2) +
            " " +
            "avgGreen:" +
            round(avgGreen, 2) +
            " " +
            "avgBlue:" +
            round(avgBlue, 2) +
            " " +
            "Resolution=" +
            width +
            "*" +
            height +
            "=" +
            totalPixels +
            "px");
  }

  private JButton saveButton;
  private JButton loadButton;
  private JButton undoButton;
  private JButton paintButton;
  private JButton fillButton;
  private JButton textButton;
  private JButton clearButton;

  // private JButton filterButton;
  private JButton selectToolButton;
  private JButton RefelectButton;
  private JButton FlipButton;

  private JButton straightLineMenuItem;
  private JSlider toleranceSlider; // New tolerance slider
  private FilterButton Filter;

  // drawing components
  private JPanel drawingPanel;
  private Point startPoint;
  private Point endPoint;
  private ArrayList<Line> lines = new ArrayList<>();
  private boolean isDrawing = false;
  private boolean fillBucketMode = false;
  private boolean drawStraightLineMode = false;
  private int fillTolerance = 10; // changes this for sensitivity of bucket fill
  private JPanel sidebarPanel;
  private JPanel topPanel;

  // Begin the demo
  public PhotoEditorGUI() {
    // set up the JFrame
    setTitle("Photo Editor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(600, 400));

    // create components
    undoButton = createButton2("undo_topbar.png", "Undo");
    clearButton = createButton2("broom.png", "Clear All");

    paintButton = createPaintButton("paintbrush.png", "Paint");

    paintButton
        .getInputMap()
        .put(
            KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK),
            "buttonAction");

    paintButton
        .getActionMap()
        .put(
            "buttonAction",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                // Perform the same action as clicking the button
                paintButton.doClick();
              }
            });
            Action paintAction = new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                paintButton.doClick();

                sidebarStatus = "Paint"; // Set sidebarStatus when button is clicked
                toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
                fillBucketMode = false;
                drawStraightLineMode = false;
              }
          };

          InputMap inputMap = paintButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
          ActionMap actionMap2 = paintButton.getActionMap();

          inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK), "buttonAction");
          actionMap2.put("buttonAction", paintAction);
  






    fillButton = createBucketButton("paintbucketsidebar.png", "Fill");
    fillButton
        .getActionMap()
        .put(
            "fillAction",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                // Perform the same action as clicking the button
                paintButton.doClick();
              }
            });
            


    fillButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            // Your mouseClicked action here
            BufferedImage combinedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);

            // Draw the lines on the combined image
            g2d.setColor(selectedColor);
            for (Line line : lines) {
              g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
            image = combinedImage;
            g2d.dispose();

            // sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
            // toolStatusLabel.setText("Selected Tool: " + toolTipText); // Update
            // toolStatusLabel
            fillBucketMode = true;
            drawStraightLineMode = false;
          }
        });


        // Define an action
  
            fillButton
            .getInputMap()
            .put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK),
                "buttonAction2");
        
        fillButton
            .getActionMap()
            .put(
                "buttonAction2",
                new AbstractAction() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                    // Perform the same action as clicking the button
                    fillButton.doClick();
                  }
                });
                Action fillAction = new AbstractAction() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                    fillButton.doClick();
                    BufferedImage combinedImage = new BufferedImage(
                      image.getWidth(),
                      image.getHeight(),
                      BufferedImage.TYPE_INT_ARGB);
                  Graphics2D g2d = combinedImage.createGraphics();
                  g2d.drawImage(image, 0, 0, null);
      
                  // Draw the lines on the combined image
                  g2d.setColor(selectedColor);
                  for (Line line : lines) {
                    g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
                  }
                  image = combinedImage;
                  g2d.dispose();
      
                  // sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
                  // toolStatusLabel.setText("Selected Tool: " + toolTipText); // Update
                  // toolStatusLabel
                  fillBucketMode = true;
                  drawStraightLineMode = false;
                  }
              };
        
              InputMap inputMap4 = fillButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
              ActionMap actionMap = fillButton.getActionMap();
        
              inputMap4.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "buttonAction2");
              actionMap.put("buttonAction2", fillAction);
        
        
        
        
        
        


















    textButton = createButton("text_feild.png", "Text"); // new text button
    RefelectButton = createButton("reflect.png", "Reflect Horizontially"); // new reflect button
    FlipButton = createButton("flipvvertical.png", "Reflect Horizontially"); // new flip button
    colorPickerButton = new JButton("Select Color"); // Initialize colorPickerButton

    // For the sliders
    toleranceSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, fillTolerance);
    toleranceSlider.setMajorTickSpacing(10);
    toleranceSlider.setMinorTickSpacing(5);
    toleranceSlider.setPaintTicks(true);
    toleranceSlider.setPaintLabels(true);
    toleranceSlider.addChangeListener(new ToleranceSliderListener());

    // Set up layout using GridBagLayout
    // Main Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    // The top panel
    topPanel = new JPanel(new CardLayout());
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    // If undo button is clicked
    undoButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int size = lines.size(); // List size
            if (lines.size() != 0) {
              lines.remove(size - 1); // remove the last drawn line
              drawingPanel.repaint(); // repaint the drawing panel to reflect the change
              if (trace) {
                System.out.println("Line removed: size " + size);
              }
            } else {
              if (trace) {
                System.out.println("Nothing to undo");
              }
            }
          }
        });
    
    // If clear button is clicked
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        if (lines.size() > 0){
          lines.clear();
          drawingPanel.repaint();
        }
        else {
          System.out.println("The panel is cleared");
        }
      }
    });

    //If reflect button is clicked
    RefelectButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            MergerImage();
            String selectedValue = (String) comboBox.getSelectedItem();
            System.out.println("selecting:" + selectedValue);
            selectCombobox(selectedValue);
            ImageFlipper flipper = new ImageFlipper();
            BufferedImage flipped = flipper.flipImage(image, "Horizontal");
            GeneratedImages.get(SelectedImage).setAssociatedImag(flipped);
            setImage(flipped);
          }
        });
        RefelectButton
        .getInputMap()
        .put(
            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK),
            "buttonAction5");
    
    RefelectButton
        .getActionMap()
        .put(
            "buttonAction",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                RefelectButton.doClick();
              }
            });
            Action refAction = new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                RefelectButton.doClick();
                sidebarStatus = "Paint"; // Set sidebarStatus when button is clicked
                toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
                fillBucketMode = false;
                drawStraightLineMode = false;
              }
          };
    
          InputMap inputMap8= RefelectButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
          ActionMap actionMap3 = RefelectButton.getActionMap();
    
          inputMap8.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK), "buttonAction5");
          actionMap3.put("buttonAction5", refAction);
    

    // If flip button is clicked
    FlipButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            MergerImage();
            String selectedValue = (String) comboBox.getSelectedItem();
            System.out.println("selecting:" + selectedValue);
            selectCombobox(selectedValue);
            ImageFlipper flipper1 = new ImageFlipper();
            BufferedImage flipped = flipper1.flipImage(image, "Vertical");
            GeneratedImages.get(SelectedImage).setAssociatedImag(flipped);
            setImage(flipped);
          }
        });
        FlipButton
        .getInputMap()
        .put(
            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK),
            "buttonAction5");
    
    FlipButton
        .getActionMap()
        .put(
            "buttonAction",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                // Perform the same action as clicking the button
                FlipButton.doClick();
              }
            });
            Action flipfAction = new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                FlipButton.doClick();
                sidebarStatus = "Paint"; // Set sidebarStatus when button is clicked
                toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
                fillBucketMode = false;
                drawStraightLineMode = false;
              }
          };
    
          InputMap inputMap9= FlipButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
          ActionMap actionMap4 = FlipButton.getActionMap();
    
          inputMap9.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK), "buttonAction5");
          actionMap4.put("buttonAction5", flipfAction);

    // Add the buttons to the top panel
    topPanel.add(undoButton);
    topPanel.add(clearButton);
    topPanel.add(comboBox);

    // Add the top panel to the main panel
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // Initialize the status bar
    toolStatusLabel = new JLabel("Selected Tool: " + sidebarStatus);
    toolStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    toolStatusLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
    imageStatusLabel = new JLabel("No selected Image");
    imageStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageStatusLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

    mainPanel.add(toolStatusLabel, BorderLayout.SOUTH); // Add label
    mainPanel.add(imageStatusLabel, BorderLayout.SOUTH); // Add label

    if (trace) {
      System.out.println("top bar picker added");
    }
    
    // add sidebar
    sidebarPanel = new JPanel();
    sidebarPanel.setLayout(new GridLayout(0, 1));
    sidebarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    sidebarPanel.setPreferredSize(new Dimension(70, 70));
    paintButton.setToolTipText("Select Paint");
    fillButton.setToolTipText("Select Paint");

    // Add components to the side bar panel
    sidebarPanel.add(paintButton);
    sidebarPanel.add(fillButton);
    sidebarPanel.add(textButton);
    sidebarPanel.add(FlipButton);
    sidebarPanel.add(RefelectButton);
    colorPickerButton.addActionListener(new ColorPickerListener()); // add ActionListener to colorPickerButton
    System.out.println("color picker added");
    colorPickerButton.setBackground(selectedColor); // Set initial background color of colorPickerButton

    // Filter button 
    Filter = new FilterButton("filter2.png", "Apply Filter", this.image, this);

    sidebarPanel.add(Filter);
    sidebarPanel.add(colorPickerButton);
    mainPanel.add(sidebarPanel, BorderLayout.EAST);

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");

    // If menu item is clicked
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
                Filter.setImage(image);
                Filename = selectedFile.toString();

                updateImageinfo(image);
                FilterButton filterButton = null;
                Component[] components = sidebarPanel.getComponents();

                GeneratedImages.clear();

                String title = "primImage";
                SelectedImage = title;

                CardObject primImage = new CardObject(image, title);
                GeneratedImages.put(title, primImage);
                UpdatedCombobox(); // Update the combo box

                for (Component component : components) {
                  if (component instanceof FilterButton) {
                    filterButton = (FilterButton) component;
                    break;
                  }
                }
                if (filterButton != null) {
                  sidebarPanel.remove(filterButton);

                  filterButton.setImage(image);

                  sidebarPanel.add(filterButton);

                  sidebarPanel.revalidate();
                  sidebarPanel.repaint();
                }
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            }
          }
        });

    JMenuItem saveMenuItem = new JMenuItem("Save As");

    // If save menu item is clicked
    saveMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          // Your save logic here
          if (image != null) {
              JFileChooser fileChooser = new JFileChooser();
              int result = fileChooser.showSaveDialog(PhotoEditorGUI.this);
  
              if (result == JFileChooser.APPROVE_OPTION) {
                  File outputFile = fileChooser.getSelectedFile();
  
                  // Check if the file already exists
                  if (outputFile.exists()) {
                      int overwriteResult = JOptionPane.showConfirmDialog(PhotoEditorGUI.this,
                              "File already exists. Do you want to overwrite it?",
                              "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                      if (overwriteResult != JOptionPane.YES_OPTION) {
                          // User chose not to overwrite, return without saving
                          return;
                      }
                  }
  
                // Create a new BufferedImage to draw the lines on
                BufferedImage combinedImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = combinedImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);

                // Draw the lines on the combined image
                g2d.setColor(selectedColor);
                for (Line line : lines) {
                  g2d.drawLine(
                      line.start.x,
                      line.start.y,
                      line.end.x,
                      line.end.y);
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
        });

    JMenuItem newImage = new JMenuItem("New");

    // New image
    newImage.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            lines.clear();

            // Create a dialog to input height and width
            JTextField heightField = new JTextField(5);
            JTextField widthField = new JTextField(5);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Height:"));
            panel.add(heightField);
            panel.add(Box.createHorizontalStrut(15)); // Add some space between components
            panel.add(new JLabel("Width:"));
            panel.add(widthField);

            int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Enter Height and Width",
                JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
              int height = Integer.parseInt(heightField.getText());
              int width = Integer.parseInt(widthField.getText());
              System.out.println(
                  "Creating new image with height: " +
                      height +
                      " and width: " +
                      width);
              BufferedImage bufferedImage = new BufferedImage(
                  width,
                  height,
                  BufferedImage.TYPE_INT_RGB);
              Graphics2D g2d = bufferedImage.createGraphics();
              g2d.setColor(Color.WHITE);
              g2d.fillRect(0, 0, width, height);
              g2d.dispose();

              image = bufferedImage;
              drawingPanel.repaint();
              GeneratedImages.clear();

              String title = "primImage";
              SelectedImage = title;

              CardObject primImage = new CardObject(image, title);
              GeneratedImages.put(title, primImage);
              UpdatedCombobox();

              FilterButton filterButton = null;
              Component[] components = sidebarPanel.getComponents();

              for (Component component : components) {
                if (component instanceof FilterButton) {
                  filterButton = (FilterButton) component;
                  break;
                }
              }
              if (filterButton != null) {
                sidebarPanel.remove(filterButton);

                filterButton.setImage(image);

                sidebarPanel.add(filterButton);

                sidebarPanel.revalidate();
                sidebarPanel.repaint();
              }
            }
          }
        });

    newImage.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

    openMenuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

    saveMenuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

    // saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control alt P"));
    fileMenu.add(newImage);
    fileMenu.add(openMenuItem);
    fileMenu.add(saveMenuItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);

    // Create the drawing panel
    drawingPanel = new JPanel() {
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
        });
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
        });
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
      this.color = color;
    }

    public void draw(Graphics g) {
      g.setColor(color);
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

    return (deltaRed <= tolerance && deltaGreen <= tolerance && deltaBlue <= tolerance);
  }

  class ColorPickerListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      Color newColor = JColorChooser.showDialog(
          PhotoEditorGUI.this,
          "Select a Color",
          selectedColor);
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
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText;
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          }
        });

    return button;
  }

  private JButton createButton2(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
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
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText;
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          }
        });

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
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
            fillBucketMode = false;
            drawStraightLineMode = false;
          }
        });

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
            button.setBackground(Color.WHITE); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }
          // public void mouseClicked(MouseEvent e) {

          // sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
          // toolStatusLabel.setText("Selected Tool: " + toolTipText); // Update
          // toolStatusLabel
          // fillBucketMode = true;
          // drawStraightLineMode = false;
          // }
        });

    return button;
  }

  /**
   * Save AsciiArt
   * @param image
   */
  public void saveAsciiArt(BufferedImage image) {
    String resolutionInput = JOptionPane.showInputDialog(
        null,
        "Enter resolution percentage (1-100):");
    if (resolutionInput != null) {
      try {
        int resolutionPercentage = Integer.parseInt(resolutionInput);
        if (resolutionPercentage >= 1 && resolutionPercentage <= 100) {
          int scaledWidth = (int) (image.getWidth() * (resolutionPercentage / 100.0));
          int scaledHeight = (int) (image.getHeight() * (resolutionPercentage / 100.0));
          BufferedImage scaledImage = new BufferedImage(
              scaledWidth,
              scaledHeight,
              BufferedImage.TYPE_INT_RGB);
          Graphics2D g = scaledImage.createGraphics();
          g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
          g.dispose();

          StringBuilder asciiBuilder = new StringBuilder();
          for (int y = 0; y < scaledImage.getHeight(); y++) {
            for (int x = 0; x < scaledImage.getWidth(); x++) {
              int pixel = scaledImage.getRGB(x, y);
              int brightness = getBrightness(pixel);
              int index = (int) (brightness / 255.0 * (ASCIICHARS.length() - 1));
              asciiBuilder.append(ASCIICHARS.charAt(index));
            }
            asciiBuilder.append("\n");
          }

          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          int result = fileChooser.showSaveDialog(null);
          if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            File outputFile = new File(selectedDirectory, "ascii_art.txt");
            try (
                BufferedWriter writer = new BufferedWriter(
                    new FileWriter(outputFile))) {
              writer.write(asciiBuilder.toString());
              JOptionPane.showMessageDialog(
                  null,
                  "ASCII art saved successfully to " +
                      outputFile.getAbsolutePath());
            } catch (IOException e) {
              e.printStackTrace();
              JOptionPane.showMessageDialog(
                  null,
                  "Error occurred while saving ASCII art");
            }
          }
        } else {
          JOptionPane.showMessageDialog(
              null,
              "Please enter a resolution percentage between 1 and 100");
        }
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter a valid number");
      }
    }
  }

  /**
   * Get Pixel brightness
   * @param pixel
   * @return
   */
  private int getBrightness(int pixel) {
    int red = (pixel >> 16) & 0xff;
    int green = (pixel >> 8) & 0xff;
    int blue = pixel & 0xff;
    return (red + green + blue) / 3;
  }

  private static final String ASCIICHARS = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";

  /**
   * Create straight button
   * @param iconPath
   * @param toolTipText
   * @return
   */
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
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
            fillBucketMode = false;
            drawStraightLineMode = true;
          }
        });

    return button;
  }

  // Number of images loaded
  int imageCount = 0;

  // Create the buttons
  private JButton createLoadButton(
      String iconPath,
      String toolTipText,
      String type) {
    // Total number of images selected so they can be stored in another card when
    // more are added

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
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                "Image files",
                ImageIO.getReaderFileSuffixes());
            System.out.println(imageFilter);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(imageFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
              File selectedFile = fileChooser.getSelectedFile();
              System.out.println(
                  "Selected File: " + selectedFile.getAbsolutePath());
              imageCount = imageCount + 1;
              System.out.println("Number of images: " + imageCount);
            } else {
              System.out.println("No file selected");
            }
          }
        });

    return button;
  }

  private JButton createSaveButton(
      String iconPath,
      String toolTipText,
      String type) {
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
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
              File selectedDirectory = fileChooser.getSelectedFile();
              System.out.println(
                  "Selected Directory: " + selectedDirectory.getAbsolutePath());
            } else {
              System.out.println("No directory selected");
            }
          }
        });

    return button;
  }

  /**
   * The ToleranceSliderListernClass is a subclass of ChangeListener. It detects when change is made to a slider object
   */
  class ToleranceSliderListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent e) {
      JSlider source = (JSlider) e.getSource();
      if (!source.getValueIsAdjusting()) {
        fillTolerance = source.getValue();
        System.out.println("Tolerance changed to: " + fillTolerance);
      }
    }
  }

  private void updateToleranceSliderVisibility() {
    toleranceSlider.setVisible(fillBucketMode);
  }

  private void updateToleranceSliderValue() {
    toleranceSlider.setValue(fillTolerance);
  }

  private void configureToleranceSlider() {
    toleranceSlider.setMinimum(0);
    toleranceSlider.setMaximum(50);
    toleranceSlider.setMajorTickSpacing(50);
    toleranceSlider.setMinorTickSpacing(10);
    toleranceSlider.setPaintTicks(true);
    toleranceSlider.setPaintLabels(true);
    toleranceSlider.setSnapToTicks(true);
    toleranceSlider.setValue(fillTolerance);
  }

  private void addToleranceSliderChangeListener() {
    toleranceSlider.addChangeListener(new ToleranceSliderListener());
  }

  private void setupToleranceSlider() {
    configureToleranceSlider();
    addToleranceSliderChangeListener();
    updateToleranceSliderVisibility();
  }

  public static void main(String[] args) {
    System.out.println("Testing Space");

    // Create and show the GUI
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            new PhotoEditorGUI();
          }
        });
  }
}