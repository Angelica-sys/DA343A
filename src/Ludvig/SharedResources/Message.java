package Ludvig.SharedResources;

import javax.swing.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * This Message class keeps track of where a massage comes 
 * from and where it is delivered, it transforms the data received from a sender to a message displayed in the chat window.
 * @version 1.0
 * @author Angelica Asplund, Ludvig Wedin Pettersson
 */
public class Message implements Serializable {
    private User sender;
    private String text;
    private ArrayList<User> receivers;
    private ImageIcon image; 
    private String timeMessageReceivedByServer;
    private String timeMessageReceivedClient;
    private int logout = 0;

    /**
     * This constructor Constructs a message without an image.
     * @param text text in message
     * @param sender sender of message
     * @param receivers receiver or receivers of message
     */
    public Message (String text, User sender, ArrayList<User> receivers)
    {
        this.text = text;
        this.receivers = receivers;
        this.sender = sender;
    }

    /**
     * Constructor used when sending a message with both text and image.
     * @param text of the message.
     * @param sender of the message.
     * @param receivers of the message.
     * @param image to send with the message.
     */
    public Message (String text, User sender, ArrayList<User> receivers, ImageIcon image)
    {
        this(text, sender, receivers);
        this.image =image;
    }

    /**
     * Constructor used when a client is logging out.
     * @param logout value used to tell the server that the client wants to log out.
     * @param sender of the message.
     */
    public Message(int logout, User sender){
        // Får bara in en 1a när disconnect körs, får hanteras på server sidan för att stänga koppling / tråd
        this.logout = logout;
        this.sender = sender;
    }

    public ImageIcon getImage() {
        return this.image;
    }

    public void setTimeReceivedByServer() {
        timeMessageReceivedByServer = LocalDateTime.now().toString();
    }

    public String timeDeliveredToClient() {
        return (new SimpleDateFormat("HH:mm")).format(new Date());
    }

    public String getText() {
        return this.text;
    }

    public User getSender() {
        return this.sender;
    }

    public String getTimeMessageReceivedClient(){
        return timeMessageReceivedClient;
    }

    public void setTimeMessageReceivedClient(String time){
        timeMessageReceivedClient = time;
    }

    public ArrayList<User> getReceivers() {
        return receivers;
    }

    public int getLogout(){
        return logout;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", text='" + text + '\'' +
                ", receivers=" + receivers +
                ", image=" + image +
                ", timeMessageReceivedByServer='" + timeMessageReceivedByServer + '\'' +
                ", timeMessageReceivedClient='" + timeMessageReceivedClient + '\'' +
                ", logout=" + logout +
                '}';
    }
}
