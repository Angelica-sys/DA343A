package entity;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 34098759873465L;
    private String userName;
    private ImageIcon userImage;


    public User(String userName, ImageIcon userImage){
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public ImageIcon getUserImage() {
        return userImage;
    }
}
