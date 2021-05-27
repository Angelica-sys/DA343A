package Ludvig.SharedResources;

import javax.swing.*;
import java.io.Serializable;

/**
 * Class representing a user in the system
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
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
        if(o != null && o instanceof Ludvig.Server.model.User){
            return username.equals(((Ludvig.Server.model.User) o).getUsername());
        }
        return false;
    }
}





/*
public class User implements Serializable {
    private String name;
    private ImageIcon icon;

    */
/**
     * Constructing the user object
     * @param name name of the user
     * @param icon avatar for the user
     *//*

    public User(String name, ImageIcon icon){
        this.name = name;
        this.icon = icon;
    }

    */
/**
     * Get name of user
     * @return username
     *//*

    public String getName() {
        return name;
    }

    public ImageIcon getIcon(){
        return icon;
    }
}
*/
