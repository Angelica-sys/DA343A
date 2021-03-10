import javax.swing.*;
import java.io.File;

public class testFile {
    public static void main(String[] args) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showOpenDialog(null);
        File file = jFileChooser.getSelectedFile();
        String str = file.getAbsolutePath();
        System.out.println(str);
    }
}
