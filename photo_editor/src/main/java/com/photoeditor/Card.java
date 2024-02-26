package com.photoeditor;

import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import com.photoeditor.PhotoEditorGUI;

public class Card extends JPanel implements ItemListener {

    private JPanel cards;
    String image1 = "Image 1";
    String image2 = "Image 2";
    String image3 = "Image 3";
    String comboBoxItems[] = {image1, image2, image3};

    public Card() {
        
        JComboBox comboBox = new JComboBox(comboBoxItems);
        comboBox.setEditable(false);

        JPanel view1 = new JPanel();
        JPanel view2 = new JPanel();
        JPanel view3 = new JPanel();

        cards = new JPanel(new CardLayout());

        
        this.add(view1, image1);
        this.add(view2, image2);
        this.add(view3, image3);

    }

    @Override
    public void itemStateChanged(ItemEvent arg0) {
        
        throw new UnsupportedOperationException("Unimplemented method 'itemStateChanged'");
    }

}
