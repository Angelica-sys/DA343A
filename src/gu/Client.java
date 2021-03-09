package gu;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Is used to create a Client
 * 
 * @author
 *
 */
public class Client  {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;		
	private Socket socket;
	private ClientUI clientUI;
	private String server;
	private User user;
	private int port;
	private ArrayList<User> users = new ArrayList<>();

	/**
	 * Constructs a Client-object
	 * 
	 * @param server the server IP-address
	 * @param port the server port
	 * @param user the user of the client
	 * @param clientUI the clientUI
	 */
	public Client(String server, int port, User user, ClientUI clientUI) {
		this.server = server;
		this.port = port;
		this.user = user;
		this.clientUI = clientUI;
	}

	/**
	 * Starts the client
	 * 
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public boolean start(){
		try {
			socket = new Socket(server, port);
		} 
		catch(Exception e) {
			clientUI.append("Error connectiong to server:" + e);
			return false;
		}
		try{
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e) {
			clientUI.append("Exception creating new Input/output Streams: " + e);
			return false;
		}
		new ServerHandler().start();
		try{
			oos.writeObject(user);
		}
		catch (IOException e) {
			clientUI.append("Exception doing login : " + e);
			disconnect();
			return false;
		}
		return true;
	}

	/**
	 * Sends the message
	 * 
	 * @param msg the message
	 */

	public synchronized void sendMessage(Message msg) {
		try {
			oos.writeObject(msg);
		}
		catch(IOException e) {
			clientUI.append("Exception writing to server: " + e);
		}
	}

	/**
	 * Disconnects from the server
	 */

	private void disconnect() {
		try { 
			if(ois != null) ois.close();
			if(oos != null) oos.close();
			if(socket != null) socket.close();
		}
		catch(Exception e) {}	
	}
	
	/**
	 * @return the User
	 */

	public User getUser() {
		return user;
	}
	
	/**
	 * Logs out
	 */

	public void logout() {
		sendMessage(new Message(Message.LOGOUT));
		disconnect();
	}
	
	public void clearUserlist() {
		users.clear();
	}
	
	private class ServerHandler extends Thread {
		public synchronized void run() {
			while(true) {
				try {
					Object obj = ois.readObject();
					if(obj instanceof User) {
						users.add((User)obj);
						ClientUI.getContacts().displayUsers(users);
					}
					//Skickar meddelande
					else if(obj instanceof Message) {
						clientUI.append((Message)obj);
					}
					else if(obj instanceof String) {
						if(((String)obj).equals("Clear users")) users.clear();
						else {
							clientUI.append((String)obj);						
							if(((String)obj).contains("is taken")) clientUI.connect(false);
							if(((String)obj).contains("You connected to the server")) clientUI.getContacts().readContacts();
						}
					}
				}
				catch(IOException e) {
					break;
				}
				catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new ClientUI().main(args);
	}
}
