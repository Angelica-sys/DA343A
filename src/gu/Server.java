package gu;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

/**
 * Is used to the create a server
 * 
 * @author
 *
 */

public class Server {
	private ArrayList<ClientHandler> al;
	private ArrayList<User> users;
	private ArrayList<User> userList;
	private ServerUI serverUI;
	private SimpleDateFormat sdf;
	private int port;
	private boolean running;
	private HashMap<String, ClientHandler> clients;
	private UnsentMessage unsentMessages = new UnsentMessage(Message.MESSAGE);
	private static int uniqueId;

	/**
	 * Constructs a Server-object
	 * 
	 * @param port the port of the server
	 * @param serverUI the serverUI
	 */

	public Server(int port, ServerUI serverUI){
		this.serverUI = serverUI;
		this.port = port;
		sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
		al = new ArrayList<ClientHandler>();
		users = new ArrayList<User>();
		clients = new HashMap<>();
		userList = new ArrayList<User>();
	}
	
	public static void main(String[] args) {
		new ServerUI(1500);
	}

	/**
	 * Starts the server
	 */

	public void start() {
		running = true;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			deleteFile("files/messages.dat");
			display("Server started. Port: " + port + ".");
			while(running) {
				Socket socket = serverSocket.accept(); 
				if(!running) break;
				ClientHandler t = new ClientHandler(socket);
				t.start();
			}
			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientHandler tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					}
					catch(IOException e) {}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
			display(sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n");
		}
	}

	private void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	/**
	 * Stops the server
	 */

	public void stop() {
		running = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
		}
	}

	//Method to show exceptions on the ServerUI with the use of the appendEvent method in that class.
	private void display(Object obj) {
		String time = sdf.format(new Date()) + " " + obj;
		serverUI.appendEvent(time + "\n");
	}

	private synchronized void broadcastToServer(Message message) throws InterruptedException, IOException {
		String time = sdf.format(new Date());
		//Sätter time
		message.setTimeSent(time);
		//Skickar meddelandet till serverUI
		serverUI.appendRoom(message);
		for(User user:message.getReciverList()) {
			if(clients.containsKey(user.getUsername()) && !user.getUsername().isEmpty()) {
				ClientHandler cHandler = clients.get(user.getUsername());
				if(!cHandler.socket.isClosed()) {
					//If send message fails send message to UnsetMessage instead and send it next time user logs on.
					cHandler.sendMessage(message);
				}
				else {
					//Adds message to unsentMessages if it fails to send.
					unsentMessages.put(user.getUsername(), message);
				}
			}
		}
	}

	private synchronized void broadcastUserList() throws IOException {
		for(int i=0;i<al.size();i++) {
			ClientHandler cHandler = al.get(i);
			cHandler.sendMessageToClient("Clear users");
			cHandler.sendUserList(users);
		}
	}
	
	public synchronized void remove(int id) {
		for(int i = 0; i < al.size(); ++i) {
			ClientHandler cHandler = al.get(i);
			if(cHandler.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	private void writeMessageToFile(Message message, String filename) throws IOException {
		ArrayList<Message> messages = new ArrayList<>();
		messages.add(message);
		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
			oos.writeInt(messages.size());
			for(Message m:messages) {
				oos.writeObject(m);
				oos.flush();
			}
		}
	}

	/**
	 * Shows messages between two dates
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public void showMessages() throws FileNotFoundException, IOException, ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");  
		try {
			Date dateFrom = formatter.parse(JOptionPane.showInputDialog(null, "Date from: yyyy.MM.dd"));
			Date dateTo = formatter.parse(JOptionPane.showInputDialog("Date to: yyyy.MM.dd"));
			Message message = null;
			try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("files/messages.dat")))) {
				int n = ois.readInt();
				String msg = "";
				for(int i=0; i<n; i++) {
					message = (Message) ois.readObject();
					String str = message.getTimeRecived().substring(0,10);
					Date messageDate = formatter.parse(str);
					if(dateFrom.compareTo(messageDate) < 0) {
						if(dateTo.compareTo(messageDate) > 0) {
							msg+=message.getText()+" sent from "+message.getSender().getUsername()+". Recived "+message.getTimeRecived()+"\n";
						}
					}
				}
				JOptionPane.showMessageDialog(null, msg);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private class ClientHandler extends Thread {
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		Message message;
		User user;
		int id;
		ClientHandler(Socket socket) {
			this.socket = socket;
			//Assigns a uniqueId to the user that increases by 1 for each user that connects to the server.
			id = ++ uniqueId;
			al.add(this);	
			try {
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				sInput  = new ObjectInputStream(socket.getInputStream());
				user = (User) sInput.readObject();
				connectUser();
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			boolean running = true;
			while(running) {
				try {
					String time = sdf.format(new Date());
					Object obj = sInput.readObject();
					if(obj instanceof Message) {
						message=(Message)obj;
						message.setTimeRecived(time);
						writeMessageToFile(message,"files/messages.dat");
					}
				}
				//Catches exceptions and shows them on the server log in the UI.
				catch (IOException e) {
					display(user.getUsername()+ " Exception: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}

				if(message!=null) {
					switch(message.getType()) {
					case Message.MESSAGE:
						try {
							broadcastToServer(message);
						} catch (InterruptedException | IOException e) {
							e.printStackTrace();
						}
						break;
					case Message.LOGOUT:
						display(user.getUsername()+" has disconnected from the server.");
						serverUI.removeUsers(user.getUsername());
						users.remove(user);
						remove(id);
						try {
							broadcastUserList();
						} catch (IOException e) {
							e.printStackTrace();
						}
						running = false;
						break;
					}
				}
			}
			remove(id);
			close();
		}
		private void close() {
			try {
				sInput.close();
				sOutput.close();
				socket.close();
			}
			catch(Exception e) {}
		}

		private synchronized void sendUserList(ArrayList<User> list) throws IOException {
			for(int i=0;i<list.size();i++) {
				sOutput.writeObject(list.get(i));
			}
		}

		//Sends text messages to the client and adds them to the list on the UI.
		private synchronized void sendMessageToClient(String str) throws IOException {
			sOutput.writeObject(str);
		}

		//Sends Direct messages.
		private synchronized void sendMessage(Message message) throws InterruptedException {
			try {
				sOutput.writeObject(message);
			}
			catch(IOException e) {
				display("Error sending message to " + user.getUsername()+ "\n" +e.toString());
			}
		}

		private synchronized void sendUnsentMessages() throws IOException, InterruptedException {
			ArrayList<Message> unsent = unsentMessages.get(user.getUsername());
			if (unsent != null && unsent.size() != 0) {
				for (Message message : unsent) {
					ArrayList<User> list = new ArrayList<User>();
					list.add(user);
					message.setReciverList(list);
					sendMessage(message);
				}
				unsentMessages.remove(user.getUsername());
			}
		}
		
		private synchronized void connectUser() throws IOException, InterruptedException {
			if(userExists(user)) {
				sendMessageToClient(user.getUsername()+ " is taken");
				sOutput.close();
			}
			else {
				clients.put(user.getUsername(), this);
				users.add(user);
				broadcastUserList();
				//Sends message to Client UI.
				sendMessageToClient("You connected to the server");
				//Displays this message in the Server Log on the Server UI.
				display(user.getUsername()+" just connected.");
				serverUI.appendUsers(user.getUsername());
				//Sends the unsent messages after user reconnects.
				sendUnsentMessages();
			}
		}
		
		private boolean userExists(User user) {
			for(User u: users)
				if(u.equals(user))
					return true;
			return false;
		}
	}
}


