package com.photoeditor;

public class test {
    fillButton
    .getInputMap()
    .put(
        KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK),
        "buttonAction2");

fillButton
    .getActionMap()
    .put(
        "buttonAction",
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
            sidebarStatus = "Paint"; // Set sidebarStatus when button is clicked
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
            fillBucketMode = false;
            drawStraightLineMode = false;
          }
      };

      InputMap inputMap4 = fillButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      ActionMap actionMap = fillButton.getActionMap();

      inputMap4.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK), "buttonAction2");
      actionMap.put("buttonAction", fillAction);




}
