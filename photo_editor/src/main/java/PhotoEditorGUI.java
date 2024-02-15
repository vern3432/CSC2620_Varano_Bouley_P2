
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        undoButton = new JButton("Undo");
        paintButton = new JButton("Paint");
        fillButton = new JButton("Fill");
        textButton = new JButton("Text");
        filterButton = new JButton("Filter");
        selectToolButton = new JButton("Select Tool");

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

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement save functionality
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement load functionality
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement undo functionality
            }
        });

        // Add action listeners for sidebar buttons
        paintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement paint functionality
            }
        });

        fillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement fill functionality
            }
        });

        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement text functionality
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement filter functionality
            }
        });

        selectToolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement select tool functionality
            }
        });

        // Add keyboard shortcuts
        saveButton.setMnemonic(KeyEvent.VK_S);
        loadButton.setMnemonic(KeyEvent.VK_O);
        undoButton.setMnemonic(KeyEvent.VK_Z);
    }

    public static void main(String[] args) {
        // Create and show the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PhotoEditorGUI();
            }
        });
    }
}
