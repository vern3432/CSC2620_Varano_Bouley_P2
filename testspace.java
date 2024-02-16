public class testspace {
    
}
public void mouseClicked(MouseEvent e) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnValue = fileChooser.showOpenDialog(null);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        System.out.println("Selected File: " + selectedFile.getAbsolutePath());
    } else {
        System.out.println("No file selected");
    }
}
});

public void mouseClicked(MouseEvent e) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int returnValue = fileChooser.showOpenDialog(null);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedDirectory = fileChooser.getSelectedFile();
        System.out.println("Selected Directory: " + selectedDirectory.getAbsolutePath());
    } else {
        System.out.println("No directory selected");
    }
}