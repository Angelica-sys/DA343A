package Ludvig.Client.Model;

import javax.swing.*;
import java.io.Serializable;

/**
 * Class representing a user in the system
 */
public class User implements Serializable {
    private String name;
    private ImageIcon icon;

    /**
     * Constructing the user object
     * @param name name of the user
     * @param icon avatar for the user
     */
    public User(String name, ImageIcon icon){
        this.name = name;
        this.icon = icon;
    }

    /**
     * Get name of user
     * @return username
     */
    public String getName() {
        return name;
    }

    public ImageIcon getIcon(){
        return icon;
    }
}
