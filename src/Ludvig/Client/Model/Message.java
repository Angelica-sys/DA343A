package Client.Model;
import Ludvig.Client.Model.User;

import javax.swing.*;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * This Message class keeps track of where a massage comes 
 * from and where it is delivered, it transforms the data reciveved from a sender to a message desplayed in the chat window.
 * @version 1.0
 * @author Angelica Asplund
 */
public class Message implements Serializable {
    private String text;
    private String sender;
    private Object[] recievers;
    private ArrayList<User> receivers;
    private ImageIcon image; 
    private String timeMessageRecievedByServer;
    private String timeMessageReceivedClient;
    private Boolean isOffLineMessage; 
    private int receiversCount;
    private ImageIcon alias;
    private int logout = 0;

    /**
     * This constructer Constructs a message without an image.
     * @param text text in message
     * @param sender sender of message
     * @param receivers receiver or receivers of message
     */
    
    public Message (String text, String sender, ArrayList<User> receivers)
    {
        this.isOffLineMessage = false; 
        this.text = text;
        this.receivers = receivers;
        this.sender = sender; 
        this.receiversCount = receivers.size();
        this.alias = new ImageIcon("images/alias.png"); 
    }
    
    public Message (String text, String sender, ArrayList<User> receivers, ImageIcon image)
    {
        this(text, sender, receivers);
        this.image =image; 
        this.alias = new ImageIcon("images/alias.png");
    }
    /*public Message(String text, String sender, ImageIcon alias, Object[] recipients, ImageIcon image) {
        this(text, sender, alias, recipients);
        this.image = image;
    }*/

    public Message(int logout, String sender){
        // Får bara in en 1a när disconnect körs, får hanteras på server sidan för att stänga koppling / tråd
        this.logout = logout;
        this.sender = sender;
    }

    public ImageIcon getImage() {
        return this.image;
    }

    public ImageIcon getAlias() {
        return this.alias;
    }

    public int getRecCount() {
        return this.receiversCount;
    }

    public void lowerRecCount() {
        --this.receiversCount;
    }

    public void setTimeRecievedByServer() {
        this.timeMessageRecievedByServer = (new SimpleDateFormat("HH:mm")).format(new Date());
    }

    public String timeDeliveredtoClient() {
        return (new SimpleDateFormat("HH:mm")).format(new Date());
    }

    public String getText() {
        return this.text;
    }

    public String getSender() {
        return this.sender;
    }

    public Object[] getRecipients() {
        return this.recievers;
    }

    public String getTimeMessageReceivedClient(){
        return timeMessageReceivedClient;
    }

    public void setTimeMessageReceivedClient(String time){
        timeMessageReceivedClient = time;
    }


    public String getRecipientsToString() {
        String res = this.recievers[0].toString();
        if (this.recievers.length > 1) {
            for(int i = 1; i < this.recievers.length; ++i) {
                res = res + ", " + this.recievers[i];
            }
        }
        return res;
    }

    public void setIsOfflineMessage(Boolean isOfflineMessage) {
        this.isOffLineMessage = isOfflineMessage;
    }

    public boolean getIsOfflineMessage() {
        return this.isOffLineMessage;
    }

    public String toString() {
        String var10000;
        if (!this.isOffLineMessage) {
            var10000 = this.timeDeliveredtoClient();
            return var10000 + " - " + this.sender + " says: " + this.text;
        } else {
            var10000 = this.timeDeliveredtoClient();
            return var10000 + " - " + this.sender + " said (" + timeMessageRecievedByServer + "): " + this.text;
        }
    }
}