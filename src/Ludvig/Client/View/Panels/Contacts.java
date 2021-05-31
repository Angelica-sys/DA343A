package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.View.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Class handling saved and currently online contacts. Allows saving contacts and selection of receivers of next sent message.
 * @Version 1.1
 * @Author Ludvig Wedin Pettersson
 */

public class Contacts extends JFrame implements ActionListener, PropertyChangeListener {
    private MainView view;
    private ClientController controller;
    private ArrayList<String> allUsers;
    private ArrayList<String> onlineUserList = new ArrayList<>();
    private ArrayList<String> savedUserList = new ArrayList<>();

    private JPanel north;
    private JPanel south;
    private JPanel center;

    private JLabel onlineUsers;
    private JLabel savedContacts;

    private JButton saveContact;
    private JButton selectContacts;

    private DefaultListModel<String> savedListModel;
    private DefaultListModel<String> onlineListModel;
    private JList<String> savedList;
    private JList<String> onlineList;

    /**
     * Constructor that builds the window
     * @param view Instance of MainView
     * @param controller Instance of ClientController
     */
    public Contacts(MainView view, ClientController controller){
        this.controller = controller;
        this.view = view;
        controller.addListener(this);
        allUsers = new ArrayList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Contacts");

        // North
        north = new JPanel();
        north.setLayout(new BorderLayout());
        add(north, BorderLayout.NORTH);

        onlineUsers = new JLabel("Online users");
        north.add(onlineUsers, BorderLayout.NORTH);

        onlineListModel = new DefaultListModel<>();
        onlineList = new JList<>(onlineListModel);
        onlineList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        north.add(new JScrollPane(onlineList), BorderLayout.CENTER);

        //Center
        center = new JPanel();
        center.setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);

        savedContacts = new JLabel("Saved contacts");
        center.add(savedContacts, BorderLayout.NORTH);

        savedListModel = new DefaultListModel<>();
        savedList = new JList<>(savedListModel);
        savedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        center.add(new JScrollPane(savedList), BorderLayout.CENTER);

        //South
        south = new JPanel();
        add(south, BorderLayout.SOUTH);

        saveContact = new JButton("Save contact");
        saveContact.addActionListener(this::actionPerformed);
        south.add(saveContact);

        selectContacts = new JButton("Select contacts");
        selectContacts.addActionListener(this::actionPerformed);
        south.add(selectContacts);

        pack();
        setVisible(true);

        setContacts();
    }

    /**
     * Method that grabs separate User lists from ClientController and puts them into lists.
     */
    public void setContacts(){
        allUsers.clear();
        savedListModel.clear();
        onlineListModel.clear();
        savedUserList = controller.getSavedContacts();
        onlineUserList = controller.getOnlineUserNames();
        for (String userName : savedUserList){
            allUsers.add(userName);
            savedListModel.addElement(userName);
        } for (String userName : onlineUserList){
            allUsers.add(userName);
            onlineListModel.addElement(userName);
        }
    }

    /**
     * Method that grabs all the selected Users from the lists and puts them into an ArrayList of receivers.
     * After ArrayList is complete, method sets the ArrayList in MainView to be used.
     */
    public void selectedUsers(){
        int[] selectedIx = onlineList.getSelectedIndices();
        ArrayList<String> selectedUsers = new ArrayList<>();
        for (int i = 0; i < selectedIx.length; i++) {
            Object o = onlineList.getModel().getElementAt(selectedIx[i]);
            for(String onlineUser : onlineUserList) {
                if(o.equals(onlineUser))
                    selectedUsers.add(onlineUser);
            }
        }
        onlineList.removeSelectionInterval(0,onlineList.getMaxSelectionIndex());
        selectedIx = savedList.getSelectedIndices();
        for (int i = 0; i < selectedIx.length; i++){
            Object o = savedList.getModel().getElementAt(selectedIx[i]);
            for (String savedUser : savedUserList){
                if (o.equals(savedUser) && !selectedUsers.contains(savedUser)){
                    selectedUsers.add(savedUser);
                }
            }
        }
        savedList.removeSelectionInterval(0,savedList.getMaxSelectionIndex());
        view.setReceiverList(selectedUsers);
        dispose();
    }

    /**
     * Gets the selected objects from user lists and tries to save them.
     */
    public void saveContacts(){
        int[] selUsers = onlineList.getSelectedIndices();
        for (int i = 0; i < selUsers.length; i++){
            Object o = onlineList.getModel().getElementAt(selUsers[i]);
            for (String userName : onlineUserList){
                if (o.equals(userName)){
                    if (checkIfDuplicate(userName))
                    {
                        break;
                    } else {
                        controller.saveContact(userName);
                    }
                }
            }
        }
        setContacts();
    }

    /**
     * Checks if user already is saved, used by saveContacts method.
     * @param userName
     * @return if duplicate = true
     */
    public Boolean checkIfDuplicate(String userName)
    {
        for (String savedUser : savedUserList){
            if (userName.equals(savedUser))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Triggered by ActionListeners bound to buttons.
     * @param e Source of call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveContact){
            saveContacts();
        }
        if (e.getSource() == selectContacts){
            selectedUsers();
        }
    }

    /**
     * Triggers everytime ClientController receives updated online user list.
     * @param evt Source of call
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Users")){
            setContacts();
        }
    }
}
