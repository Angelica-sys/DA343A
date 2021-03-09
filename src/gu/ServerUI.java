package gu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * Is used to create a UI for the server
 * 
 * @author
 *
 */
public class ServerUI extends JFrame implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton stopStart;
	private Server server;
	private JButton btnMessages;
	private JPanel center = new JPanel(new GridLayout(3,1));
	private int port = 1500;
	private JList userList, messageList, eventList;
	private DefaultListModel<String> userListModel, eventListModel;
	private DefaultListModel<Object> messageListModel;
	private boolean serverStarted = false;
	

	/**
	 * Constructs a ServerUI-object
	 * 
	 * @param port the port of the server
	 */
	public ServerUI(int port) {
		super("Chat Server");
		server = null;
		JPanel south = new JPanel();
		btnMessages =  new JButton("Show messages between specific dates");
		btnMessages.addActionListener(this);
		btnMessages.setEnabled(false);
		stopStart = new JButton("Run server");
		stopStart.addActionListener(this);

		south.setLayout(new GridLayout(2, 1, 0, 0));
		south.add(stopStart);
		south.add(btnMessages);
		getContentPane().add(south, BorderLayout.SOUTH);
		
		messageList();
		userList();
		eventList();

		messageList.setBackground(Color.darkGray);
		eventList.setBackground(Color.darkGray);
		userList.setBackground(Color.darkGray);
		stopStart.setBackground(Color.darkGray);
		btnMessages.setBackground(Color.darkGray);

		messageList.setForeground(Color.white);
		eventList.setForeground(Color.white);
		userList.setForeground(Color.white);
		stopStart.setForeground(Color.white);
		btnMessages.setForeground(Color.white);

		//Order they are added in decides window positions.
		center.add(new JScrollPane(userList));
		center.add(new JScrollPane(messageList));
		center.add(new JScrollPane(eventList));
		
		add(center);
		addWindowListener(this);
		setSize(900, 600);
		setVisible(true);
		setLocationRelativeTo(null);
	}		

	/**
	 * Make the messageList and messageListModel
	 */
	public void messageList() {
		messageListModel = new DefaultListModel<Object>();
		messageListModel.addElement("Chat room.");
		messageList = new JList(messageListModel);
		messageList.setSelectionModel(new DisabledItemSelectionModel());
	}
	/**
	 * Make the eventList and eventListModel
	 */
	public void eventList() {
		eventListModel = new DefaultListModel<String>();
		eventListModel.addElement("Server log.");
		eventList = new JList(eventListModel);
		eventList.setSelectionModel(new DisabledItemSelectionModel());
	}
	/**
	 * Make the userList and userListModel
	 */
	public void userList() {
		userListModel = new DefaultListModel<String>();
		userListModel.addElement("Users currently online.");
		userList = new JList(userListModel);
		userList.setSelectionModel(new DisabledItemSelectionModel());
	}
	
	/**
	 * Appends a user to the list of users
	 * 
	 * @param str the username
	 */
	public void appendUsers(String str) {
		userListModel.addElement(str);
	}

	public void removeUsers(String str) {
		for(int i=0;i<userListModel.size();i++) {
			if(userListModel.get(i).equals(str)) {
				userListModel.removeElementAt(i);
			}
		}
	}

	/**
	 * Append a message to the list of messages
	 * 
	 * @param obj the message
	 */

	public void appendRoom(Object obj) {
		if(obj instanceof Message) {
			Message m = (Message)obj;
			messageListModel.addElement(m.getIcon());
			messageListModel.addElement(m.getText()+ " sent from "+ m.getSender().getUsername()+ ". Sent " + m.getTimeSent());
		}
	}

	/**
	 * Appends a string to the list of events
	 * 
	 * @param str the event
	 */

	public void appendEvent(String str) {
		eventListModel.addElement(str);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==stopStart) {
			if(server != null) {
				server.stop();
				server = null;
				stopStart.setText("Start server");
				btnMessages.setEnabled(false);
				serverStarted=false;
				return;
			}	
			server = new Server(port, this);
			serverStarted=true;
			btnMessages.setEnabled(true);
			new ServerRunning().start();
			stopStart.setText("Stop server");
		}

		if(e.getSource()==btnMessages) {
			try {
				if(serverStarted)
					server.showMessages();
			} catch (IOException | ParseException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] arg) {
		new ServerUI(1500);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */

	public void windowClosing(WindowEvent e) {
		if(server != null) {
			try {server.stop();
			}catch(Exception e1) {
				e1.printStackTrace();
			}
			server = null;
		}
		dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	private class ServerRunning extends Thread {
		public void run() {
			server.start();     
			stopStart.setText("Start");
			appendEvent("Server stopped\n");
			server = null;
		}
	}

	private class DisabledItemSelectionModel extends DefaultListSelectionModel {
		@Override
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1,-1);
		}
	}
}

