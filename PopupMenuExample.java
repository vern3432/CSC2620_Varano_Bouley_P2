import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupMenuExample extends JFrame {
    public PopupMenuExample() {
      
        setTitle("Popup Menu Example");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
      
        JButton button = new JButton("Options");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem grayscaleItem = new JMenuItem("Grayscale");
                JMenuItem invertColorsItem = new JMenuItem("Inverted Colors");
                JMenuItem blurItem = new JMenuItem("Blur with Gaussian Noise");
                JMenuItem staticItem = new JMenuItem("Static with Perlin Noise");
                JMenuItem cartoonifyItem = new JMenuItem("Cartoonify");

                grayscaleItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("View the image in greyscale");
                    }
                });

                invertColorsItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("View the image with inverted colors");
                    }
                });

                blurItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Adding blur with Gaussian Noise");
                    }
                });

                staticItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Adding static with Perlin Noise");
                    }
                });

                cartoonifyItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Cartoonify");
                    }
                });

                popupMenu.add(grayscaleItem);
                popupMenu.add(invertColorsItem);
                popupMenu.add(blurItem);
                popupMenu.add(staticItem);
                popupMenu.add(cartoonifyItem);

                popupMenu.show(button, button.getWidth() / 2, button.getHeight() / 2);
            }
        });

        JPanel panel = new JPanel();
        panel.add(button);
        add(panel, BorderLayout.CENTER);

        setSize(300, 200);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PopupMenuExample();
            }
        });
    }
}
