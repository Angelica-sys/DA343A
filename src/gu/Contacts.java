package gu;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Is used to create a contacts
 * 
 * @author
 *
 */

public class Contacts extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JPanel panelNorth = new JPanel();
	private JPanel panelCenter = new JPanel();
	private JPanel panelSouth = new JPanel();

	private JLabel activeUsers = new JLabel("Current active users in the chat");
	private JLabel yourContacts = new JLabel("Your contacts");

	private JButton addContact = new JButton("Add contact");
	private JButton removeContact = new JButton("Remove Contact");
	private JButton send = new JButton("DM selected contacts");

	private DefaultListModel<String> activeUserListModel;
	private DefaultListModel<String> contactListModel;
	private JList<String> userList;
	private JList<String> contactList;

	private ArrayList<User> activeUserArrayList = new ArrayList<User>();
	private ArrayList<User> contactsArrayList = new ArrayList<User>();

	private ClientUI clientUI;

	private String username;

	/**
	 * Constructs a Contacts-object
	 * 
	 * @param clientUI the clientUI
	 */
	public Contacts(ClientUI clientUI) {
		this();
		this.clientUI=clientUI;
	}

	/**
	 * Constructs a Contacts-object
	 */
	public Contacts() {
		activeUserListModel = new DefaultListModel<String>();
		contactListModel = new DefaultListModel<String>();
		userList = new JList<String>(activeUserListModel);
		contactList = new JList<String>(contactListModel);


		panelNorth.setBackground(Color.darkGray);
		panelCenter.setBackground(Color.darkGray);
		panelSouth.setBackground(Color.darkGray);
		userList.setBackground(Color.darkGray);
		contactList.setBackground(Color.darkGray);
		addContact.setBackground(Color.darkGray);
		removeContact.setBackground(Color.darkGray);
		send.setBackground(Color.darkGray);


		panelNorth.setForeground(Color.white);
		panelCenter.setForeground(Color.white);
		panelSouth.setForeground(Color.white);
		userList.setForeground(Color.white);
		contactList.setForeground(Color.white);
		addContact.setForeground(Color.white);
		removeContact.setForeground(Color.white);
		send.setForeground(Color.white);
		activeUsers.setForeground(Color.white);
		yourContacts.setForeground(Color.white);

		userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		contactList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		setLayout();
		setComponetSize();
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);
		panelNorth.add(activeUsers, BorderLayout.NORTH);
		panelCenter.add(yourContacts, BorderLayout.NORTH);
		panelCenter.add(new JScrollPane(contactList), BorderLayout.CENTER);
		panelSouth.add(addContact, BorderLayout.WEST);
		panelSouth.add(send, BorderLayout.CENTER);
		panelSouth.add(removeContact, BorderLayout.EAST);	
		panelNorth.add(new JScrollPane(userList), BorderLayout.CENTER);
		send.addActionListener(this);
		addContact.addActionListener(this);
	}

	/**
	 * Creates the frame
	 */

	public void createFrame() {
		JFrame frame = new JFrame("Your contacts - " + clientUI.getClient().getUser().getUsername());
		frame.setPreferredSize(new Dimension(500,600));
		frame.setBounds(900,0,500,600);
		frame.setVisible(true);
		frame.pack();
		frame.getContentPane().add(this);
	}

	private void setLayout() {
		setLayout(new BorderLayout());
		panelNorth.setLayout(new BorderLayout());
		panelCenter.setLayout(new BorderLayout());
		panelSouth.setLayout(new BorderLayout());

	}

	private void setComponetSize() {
		contactList.setPreferredSize(new Dimension(70,100));
		userList.setPreferredSize(new Dimension(70,100));
		addContact.setPreferredSize(new Dimension(140,20));
		removeContact.setPreferredSize(new Dimension(140,20));
	}

	/**
	 * Displays the active users
	 * 
	 * @param ""arrayList"" of the currently active users.
	 */

	public void displayUsers(ArrayList<User> users) {
		activeUserArrayList.clear();
		for(User u : users)
			activeUserArrayList.add(u);
		removeDuplicates(activeUserArrayList);
		activeUserListModel.removeAllElements();
		for(User u: activeUserArrayList) {
			activeUserListModel.addElement(u.getUsername());
		}
	}

	private void test(ArrayList<String> alist) {
		activeUserArrayList = new ArrayList<>();
		for(String u : alist) {
			activeUserArrayList.add(new User(u,null));
		}
		removeDuplicates(activeUserArrayList);
		for(User u : activeUserArrayList) {
			activeUserListModel.addElement(u.getUsername());
		}
	}

	private void removeDuplicates(ArrayList<User> alist) {
		for(int i=0;i<alist.size();i++) {
			int multi=0;
			for(int j=0;j<alist.size();j++) {
				if(alist.get(i).getUsername().equals(alist.get(j).getUsername())) {
					if(multi>0)
						alist.remove(j);
					else
						multi++;
				}
			}
		}
	}

	/**
	 * Sets the contacts
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public void setContacts(ArrayList<User> al) throws FileNotFoundException, IOException {
		for(User u : al) {
			contactsArrayList.add(u);
		}
		removeDuplicates(contactsArrayList);
		try {
			for(User u: contactsArrayList) {
				String userName = u.getUsername();
				if(!contactListModel.contains(userName) && !u.getUsername().equals(clientUI.getClient().getUser().getUsername()))
					contactListModel.addElement(userName);
			}
		}catch(ConcurrentModificationException e) {
			e.printStackTrace();
		};
	}
	

	private ArrayList<User> selectedItem(ArrayList<User> al, JList<String> list) {
		int[] selectedIx = list.getSelectedIndices();
		ArrayList<User> selectedUsers = new ArrayList<>();
		for (int i = 0; i < selectedIx.length; i++) {
			Object sel = list.getModel().getElementAt(selectedIx[i]);
			for(User u:al) {
				if(sel.equals(u.getUsername()))
					selectedUsers.add((User) u);
			}
		}
		list.removeSelectionInterval(0,list.getMaxSelectionIndex());
		return selectedUsers;
	}

	public static void main(String[] args) {
		Contacts contacts = new Contacts();
		ArrayList<String> list = new ArrayList<String>();
		/*list.add("123");
		list.add("fff");
		list.add("1524");
		list.add("1524");
		contacts.test(list);*/
		contacts.createFrame();
	}

	private void writeContactsToFile(String filename) throws FileNotFoundException, IOException {
		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("files/"+filename+".dat")))) {
			oos.writeInt(contactsArrayList.size());
			for(User u:contactsArrayList) {
				oos.writeObject(u);
				oos.flush();
			}
		}
	}

	private ArrayList<User> readContactsFromFile(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		ArrayList<User> al = new ArrayList<User>();
		try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("files/"+filename+".dat")))) {
			int n = ois.readInt();
			for(int i=0;i<n;i++) {
				al.add((User)ois.readObject());
			}
		}
		return al;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==send) {
			//Sends the selected users to the clientUI.
			clientUI.setReciverList(selectedItem(activeUserArrayList, userList));
			clientUI.setReciverList(selectedItem(contactsArrayList, contactList));
		}
		if(e.getSource()==addContact) {
			try {
				setContacts(selectedItem(activeUserArrayList, userList));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void clearContactList() {
		contactsArrayList.clear();
		contactListModel.clear();
	}

	public void writeContacts() throws FileNotFoundException, IOException {
		writeContactsToFile("contacts_"+clientUI.getClient().getUser().getUsername());
	}
	public void readContacts() throws FileNotFoundException, ClassNotFoundException, IOException {
		String filename = "contacts_"+clientUI.getClient().getUser().getUsername();
		File file = new File("files/"+filename+".dat");
		if(file.exists()){
			setContacts(readContactsFromFile(filename));
		}
		else {
			file.createNewFile();
		}
	}
}



