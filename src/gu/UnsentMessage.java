package gu;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Stores the unsent messages
 * 
 * @author
 *
 */
public class UnsentMessage extends Message {
	private HashMap<String, ArrayList<Message>> messages = new HashMap<String, ArrayList<Message>>();
	
	public UnsentMessage(int type) {
		super(type);
	}
	/**
	 * Puts an unsent message to the HashMap
	 * 
	 * @param username the username of the receiver
	 * @param message the unsent message
	 */
	public synchronized void put(String username, Message message) {
		if (get(username) == null) {
			messages.put(username, new ArrayList<Message>());
		}
		get(username).add(message);
	}
	/**
	 * Gets a list of unsent message to a certain user
	 * 
	 * @param username the username of the receiver
	 * @return An ArrayList of unsent messages
	 */
	public synchronized ArrayList<Message> get(String username){
		return messages.get(username);
	}
	/**
	 * Clears the HashMap
	 */
	public synchronized void clear() {
		messages.clear();
	}
	/**
	 * Removes all unsent message to a certain user
	 * 
	 * @param username the username of the user
	 */
	public synchronized void remove(String username) {
		messages.remove(username);
	}
}
