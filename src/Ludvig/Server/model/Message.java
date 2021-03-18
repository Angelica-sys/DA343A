package Ludvig.Server.model;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message is a serialized object that represents a message to be sent between two or more users in a chat
 * @version 1.0
 */
public class Message implements Serializable {
    private ArrayList<User> toUser;
    private User fromUser;
    private String text;
    private String timeReceived;
    private String timeSent;
    private ImageIcon image;

    public Message(ArrayList<User> toUser, User fromUser, String text, ImageIcon image){
        this.toUser=toUser;
        this.fromUser=fromUser;
        this.text=text;
        this.image=image;
    }

    public String getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(String timeReceived) {
        this.timeReceived = timeReceived;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public void setToUser(ArrayList<User> toUser) {
        this.toUser = toUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public ArrayList<User> getToUser() {
        return toUser;
    }

    public User getFromUser(){
        return fromUser;
    }

    public String getText(){
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageIcon getImage() {
        return image;
    }

    public Message(){
        super();
    }
}
