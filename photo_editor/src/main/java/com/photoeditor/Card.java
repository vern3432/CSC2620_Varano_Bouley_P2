package com.photoeditor;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import com.photoeditor.PhotoEditorGUI;

public class Card extends JPanel {

    private JPanel cards;

    public Card() {

        JPanel comboBoxPannel = new JPanel();
        String comboBoxItems[] = {};
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);

    }

}
