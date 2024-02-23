package com.photoeditor;


private JLabel imageLabel;
private BufferedImage inputImage;

public CartoonifyImage() {
    setTitle("Cartoonify Image");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    JPanel controlPanel = new JPanel();
    JButton openButton = new JButton("Open Image");
    JButton cartoonifyButton = new JButton("Cartoonify");
    JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);

    openButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    inputImage = ImageIO.read(selectedFile);
                    displayImage(inputImage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });

    cartoonifyButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (inputImage != null) {
                int threshold = thresholdSlider.getValue();
                BufferedImage outputImage = detectEdges(inputImage, threshold);
                displayImage(outputImage);
            } else {
                JOptionPane.showMessageDialog(null, "Please open an image first", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    thresholdSlider.setMajorTickSpacing(10);
    thresholdSlider.setMinorTickSpacing(5);
    thresholdSlider.setPaintTicks(true);
    thresholdSlider.setPaintLabels(true);

    controlPanel.add(openButton);
    controlPanel.add(cartoonifyButton);
    controlPanel.add(new JLabel("Threshold:"));
    controlPanel.add(thresholdSlider);

    imageLabel = new JLabel();
    JScrollPane scrollPane = new JScrollPane(imageLabel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(controlPanel, BorderLayout.NORTH);
    getContentPane().add(scrollPane, BorderLayout.CENTER);
}

private BufferedImage detectEdges(BufferedImage image, int threshold) {
    // Implement edge detection logic here
    // For simplicity, let's just return the original image
    return image;
}

private void displayImage(BufferedImage image) {
    ImageIcon icon = new ImageIcon(image);
    imageLabel.setIcon(icon);
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            CartoonifyImage cartoonifyImage = new CartoonifyImage();
            cartoonifyImage.setVisible(true);
        }
    });
}
}