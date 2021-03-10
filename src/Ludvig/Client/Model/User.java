package Client.Model;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private ImageIcon icon;

    public User(String name, ImageIcon icon){
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }
}
