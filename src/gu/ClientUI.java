package gu;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class creates the Client UI.
 * 
 * @author
 *
 */
public class ClientUI extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private final int port = 1500;
	private final String ipAdress = "127.0.0.1";
	private Client client;
	private boolean connect, imageChoosed=false;
	private User user;
	private ImageIcon profileImage, img;
	private ArrayList<User> reciverList = new ArrayList<User>();
	private static Contacts contacts;

	private JPanel panelNorth = new JPanel();
	private JPanel panelSouthCenter = new JPanel();
	private JPanel panelSouthSouth = new JPanel();
	private JPanel panelCenter = new JPanel();
	private JPanel panelSouth = new JPanel();

	private JButton btnChoose = new JButton("Picture");
	private JButton btnConnect = new JButton("Connect to server");
	private JButton btnSend = new JButton("Send Message");
	private JButton btnImage = new JButton("Attach a picture");
	private JButton btnDisconnect = new JButton("Disconnect");
	private JButton btnContacts = new JButton("Open contact list");

	private JTextPane image = new JTextPane();

	private JTextField tfUsername = new JTextField();
	private JList<Object> textPaneViewer;
	private JTextPane textPaneMessage = new JTextPane();

	private DefaultListModel<Object> messageListModel;

	private JLabel lblName = new JLabel("Username: ", SwingConstants.RIGHT);
	private JLabel lblTo = new JLabel("Direct messaging:");

	/**
	 * This method constructs the ClientUI.
	 */
	public ClientUI() {
		contacts = new Contacts(this);
		connect(false);
		handleActionListeners();
		setLayout();
		setPreferredSize();
		add(panelNorth, BorderLayout.SOUTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.NORTH);

		//Colors

		panelNorth.setBackground(Color.darkGray);
		panelCenter.setBackground(Color.darkGray);
		panelSouth.setBackground(Color.darkGray);
		panelSouthSouth.setBackground(Color.darkGray);
		panelSouthCenter.setBackground(Color.darkGray);

		panelNorth.setForeground(Color.white);
		panelCenter.setForeground(Color.white);
		panelSouth.setForeground(Color.white);
		panelSouthSouth.setForeground(Color.white);
		panelSouthCenter.setForeground(Color.white);

		btnChoose.setBackground(Color.darkGray);
		image.setBackground(Color.darkGray);
		lblName.setBackground(Color.darkGray);
		tfUsername.setBackground(Color.darkGray);
		btnConnect.setBackground(Color.darkGray);
		btnDisconnect.setBackground(Color.darkGray);

		btnChoose.setForeground(Color.white);
		image.setForeground(Color.white);
		lblName.setForeground(Color.white);
		tfUsername.setForeground(Color.white);
		btnConnect.setForeground(Color.white);
		btnDisconnect.setForeground(Color.white);
		lblTo.setForeground(Color.white);

		panelSouthCenter.add(btnChoose);
		panelSouthCenter.add(image);
		panelSouthCenter.add(lblName);
		panelSouthCenter.add(tfUsername);
		panelSouthSouth.add(btnConnect);
		panelSouthSouth.add(btnDisconnect);

		panelNorth.add(panelSouthCenter, BorderLayout.CENTER);
		panelNorth.add(panelSouthSouth, BorderLayout.SOUTH);

		messageListModel = new DefaultListModel<Object>();
		textPaneViewer = new JList<Object>(messageListModel);
		textPaneViewer.setSelectionModel(new disableItemSelection());
		textPaneViewer.setBackground(new Color(220,220,220));

		//Colors
		textPaneViewer.setBackground(Color.darkGray);
		btnSend.setBackground(Color.darkGray);
		textPaneMessage.setBackground(Color.darkGray);
		btnImage.setBackground(Color.darkGray);
		btnContacts.setBackground(Color.darkGray);

		textPaneViewer.setForeground(Color.white);
		btnSend.setForeground(Color.white);
		textPaneMessage.setForeground(Color.white);
		btnImage.setForeground(Color.white);
		btnContacts.setForeground(Color.white);

		panelCenter.add(new JScrollPane(textPaneViewer),BorderLayout.CENTER);
		panelCenter.add(lblTo, BorderLayout.SOUTH);

		panelSouth.add(btnSend, BorderLayout.EAST);
		panelSouth.add(textPaneMessage, BorderLayout.CENTER);
		panelSouth.add(btnImage,BorderLayout.WEST);
		panelSouth.add(btnContacts, BorderLayout.SOUTH);

		image.setEnabled(false);
	}

	//Adds ActionListeners to the buttons.
	private void handleActionListeners() {
		btnChoose.addActionListener(this);
		btnConnect.addActionListener(this);
		btnSend.addActionListener(this);
		btnImage.addActionListener(this);
		btnDisconnect.addActionListener(this);
		btnContacts.addActionListener(this);
	}

	//Sets the layout of the panels
	private void setLayout() {
		setLayout(new BorderLayout());
		panelSouthCenter.setLayout(new GridLayout(1,4));
		panelSouthSouth.setLayout(new GridLayout(0,2));
		panelNorth.setLayout(new BorderLayout());
		panelCenter.setLayout(new BorderLayout());
		panelSouth.setLayout(new BorderLayout());
	}

	//Sets the size of the panels
	private void setPreferredSize() {
		btnChoose.setPreferredSize(new Dimension(80,30));
		btnChoose.setOpaque(true);
		btnConnect.setPreferredSize(new Dimension(80,30));
		btnConnect.setOpaque(true);
		btnSend.setPreferredSize(new Dimension(150,30));
		btnSend.setOpaque(true);
		btnImage.setPreferredSize(new Dimension(150,30));
		btnImage.setOpaque(true);
		btnContacts.setPreferredSize(new Dimension(80,30));
		btnContacts.setOpaque(true);
		tfUsername.setPreferredSize(new Dimension(150, 40));
	}

	/**
	 * This method appends an object to the list.
	 * 
	 * @param obj the object
	 */

	public void append(Object obj) {
		if(obj instanceof Message) {
			Message m = (Message)obj;
			messageListModel.addElement(m.getIcon());
			messageListModel.addElement(m.getText()+ " sent from " + m.getSender().getUsername()+ ". Sent " + m.getTimeSent());
		}

		else {
			messageListModel.addElement(obj);
		}

	}

	//Enables the buttons if user successfully connects.
	public void connect(boolean connect) {
		this.connect = connect;
		btnDisconnect.setEnabled(connect);
		btnConnect.setEnabled(!connect);
		btnSend.setEnabled(connect);
		btnImage.setEnabled(connect);
		btnChoose.setEnabled(!connect);
		btnContacts.setEnabled(connect);
		textPaneMessage.setEnabled(connect);
		tfUsername.setEnabled(!connect);
		if(messageListModel!=null && !connect) {
			messageListModel.removeAllElements();
			textPaneViewer.setModel(messageListModel);
		}
	}
	/**
	 * This method sets the recieverlist when DMing someone.
	 * 
	 * @param reciverList the reciverlist 
	 */

	//Sets reciever for the message
	public void setReciverList(ArrayList<User> reciverList) {
		for(User u : reciverList) {
			this.reciverList.add(u);
		}
		for(User u:reciverList) {
			if(!lblTo.getText().contains(u.getUsername())) {
				lblTo.setText(lblTo.getText()+u.getUsername()+",");
			}
		}
	}

	private ImageIcon resizeImage(String ImagePath, JTextPane imageTextPane){
		ImageIcon MyImage = new ImageIcon(ImagePath);
		Image img = MyImage.getImage();
		Image newImg = img.getScaledInstance(imageTextPane.getHeight(), imageTextPane.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}

	private ImageIcon selectImage() {
		ImageIcon img = null;
		JFileChooser file = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
		file.addChoosableFileFilter(filter);
		int result = file.showSaveDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			File selectedFile = file.getSelectedFile();
			String path = selectedFile.getAbsolutePath();
			img = resizeImage(path, image);
		}
		return img;
	}

	/**
	 * @return This method returns the Contacts object after
	 *  the users have been displayed by the client class.
	 */

	public static Contacts getContacts() {
		return contacts;
	}

	/**
	 * @return the Client-object
	 */

	public Client getClient() {
		return client;
	}

	//Handles all actionListeners we added to the buttons.

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnChoose) {
			profileImage=selectImage();
			if(profileImage!=null)
				image.insertIcon(profileImage);
		}

		if(e.getSource()==btnConnect) {
			String username = tfUsername.getText().trim();
			System.out.println(username + " has connected to the server.");
			if(!username.isEmpty() && profileImage!= null) {
				user = new User(username, profileImage);
				client = new Client(ipAdress, port, user, this);
				if(!client.start()) return;
				connect(true);
			}

			else {
				JOptionPane.showMessageDialog(null, "Select a username and pick a picture.");
			}
		}

		if(e.getSource()==btnSend) {
			if(!textPaneMessage.getText().isEmpty()) {
				if(imageChoosed) client.sendMessage(new Message(Message.MESSAGE, textPaneMessage.getText(),img,user,reciverList));
				else {
					client.sendMessage(new Message(Message.MESSAGE, textPaneMessage.getText(),null,user,reciverList));
				}
				textPaneMessage.setText("");
				textPaneMessage.repaint();
				lblTo.setText("Direct messaging:");
				for(int i=0;i<reciverList.size();i++) {
					reciverList.remove(i);
				}
				img=null;
			}
		}

		if(e.getSource()==btnImage) {
			img=selectImage();
			imageChoosed=true;
			textPaneMessage.insertIcon(img);
		}

		if(e.getSource()==btnDisconnect) {
			try {
				contacts.writeContacts();
			}
			catch(IOException e1) {

			}
			contacts.clearContactList();
			client.logout();
			connect(false);
		}

		if(e.getSource() == btnContacts) {
			client.clearUserlist();
			contacts.createFrame();
		}
	}

	private class disableItemSelection extends DefaultListSelectionModel {
		private static final long serialVersionUID = 1L;
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1,-1);
		}
	}

	public static void main(String[] args) {
		ClientUI chatUI = new ClientUI();
		JFrame frame = new JFrame("Chat Window");
		frame.setPreferredSize(new Dimension(900, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		frame.getContentPane().add(chatUI);
		frame.setLocationRelativeTo(null);
	}
}