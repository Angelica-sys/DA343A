package Ludvig.Server.model;

import javax.swing.*;
import java.io.Serializable;

/**
 * User is a serialized object that represents a user in a chat
 * @version 1.0
 */
public class User implements Serializable {
    private String username;
    private ImageIcon image;

    public User(String username, ImageIcon image){
        this.username=username;
        this.image=image;
    }

    public User(){
        super();
    }

    public String getUsername(){
        return username;
    }

    public ImageIcon getImage(){
        return image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    /**
     * Overrides the hashCode method in Object to make sure that only the hashCode of the variable username is
     * used in comparison between User-objects in a HashMap or HashSet
     * @return int
     */
    @Override
    public int hashCode(){
        return username.hashCode();
    }

    /**
     * Overrides the equals method in Object to make sure that only the variable username is used in comparison between
     * objects
     * @param o An object
     * @return boolean
     */
    @Override
    public boolean equals(Object o){
        if(o != null && o instanceof User){
            return username.equals(((User) o).getUsername());
        }
        return false;
    }
}
