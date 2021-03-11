package gu;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.ImageIcon;

/**
 * This class represents a single message.
 * A Message contains an image, text, User, time recived and time sent
 * 
 * @author
 *
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MESSAGE = 1, LOGOUT = 2;
	private int type;
	private String text;
	private ImageIcon image;
	private User sender;
	private ArrayList<User> reciverList;
	private String timeRecived;
	private String timeSent;

	/**
	 * Constructs a Message-object
	 * 
	 * @param type the type of the message
	 * @param text the text of the message
	 * @param image the image of the message
	 * @param sender the user who sent the message
	 * @param reciverList the receivers of the message
	 */
	public Message(int type, String text, ImageIcon image, User sender, ArrayList<User> reciverList) {
		this.text=text;
		this.image=image;
		this.sender=sender;
		this.reciverList=reciverList;
		this.type = type;
	}
	/**
	 * Constructs a Message-object
	 * 
	 * @param text the text of the message
	 * @param image the image of the message
	 */
	public Message(String text, ImageIcon image) {
		this.text=text;
		this.image=image;
	}
	/**
	 * Constructs a Message-object
	 * 
	 * @param type
	 */
	public Message(int type) {
		this.type=type;
	}
	/**
	 * Constructs a Message-object
	 * 
	 * @param msg a Message-object
	 */
	public Message(Message msg) {
		this.text=msg.getText();
		this.image=msg.getIcon();
		this.sender=msg.getSender();
		this.reciverList=msg.getReciverList();
		this.type=msg.getType();
		this.timeRecived=msg.getTimeRecived();
	}
	/**
	 * @return the text of the message
	 */
	public String getText() {
		return text;
	}
	/**
	 * @return the sender of the message.
	 */
	public User getSender() {
		return sender;
	}
	/**
	 * @return the list of receivers of this message.
	 */
	public ArrayList<User> getReciverList(){
		return reciverList;
	}
	/**
	 * @return the image of the message.
	 */
	public ImageIcon getIcon() {
		return image;
	}
	/**
	 * @return the time the message was received.
	 */
	public String getTimeRecived() {
		return timeRecived;
	}
	/**
	 * @return the time the message was sent.
	 */
	public String getTimeSent() {
		return timeSent;
	}
	/**
	 * @return the type of this message.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param time the time the message was received.
	 */
	public void setTimeRecived(String time) {
		timeRecived=time;
	}
	/**
	 * @param time the time the message was sent.
	 */
	public void setTimeSent(String time) {
		timeSent=time;
	}
	/**
	 * @return whether or not an image exists in the message.
	 */
	public boolean imageExists() {	
		if(image==null) return false;
		return true;		
	}
	/**
	 * @param user set a receiver to the list of receivers
	 *
	 */
	public void addReciver(User user) {
		reciverList.add(user);
	}
	public void setReciverList(ArrayList<User> reciverList) {
		this.reciverList=reciverList;
	}
}

