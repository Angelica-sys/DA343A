package entity;

import javax.swing.*;
import java.io.Serializable;
import java.util.LinkedList;

public class Message implements Serializable {
    private static final long serialVersionUID = 85432237656543L;
    private String text;
    private ImageIcon icon;
    private String timeAndDateReceivedByServer;
    private String timeAndDateSentToUser;
    private User sender;
    private LinkedList<User> receivers = new LinkedList<>();

    public Message(String text, ImageIcon icon, User sender){
        this.text = text;
        this.icon = icon;
        this.sender = sender;
    }

    public void setTimeAndDateReceivedByServer(String timeAndDateReceivedByServer) {
        this.timeAndDateReceivedByServer = timeAndDateReceivedByServer;
    }

    public void setTimeAndDateSentToUser(String timeAndDateSentToUser) {
        this.timeAndDateSentToUser = timeAndDateSentToUser;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getSender() {
        return sender;
    }

    public void addReceiver(User receiver){
        receivers.add(receiver);
    }

    public LinkedList<User> getReceivers() {
        return receivers;
    }
}
