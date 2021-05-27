package Ludvig.Client.View;

import Ludvig.SharedResources.Message;
import Ludvig.Client.Controller.ClientController;
import Ludvig.SharedResources.User;
import Ludvig.Client.View.Panels.CenterPanel;
import Ludvig.Client.View.Panels.NorthPanel;
import Ludvig.Client.View.Panels.SouthPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Main GUI frame, consists of several panels
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
 */
public class MainView extends JFrame implements PropertyChangeListener {
    private ClientController controller;
    private SouthPanel sPanel;
    private CenterPanel cPanel;
    private NorthPanel nPanel;
    private ArrayList<User> receiverList;
    private ImageIcon image;


    /**
     * Constructs the frame
     * @param controller ClientController reference
     */
    public MainView(ClientController controller){
        this.controller = controller;
        receiverList = new ArrayList<>();
        controller.addListener(this);
        setupFrame();
    }

    /**
     * Sets up all the panels in the frame
     */
    public void setupFrame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400,800));
        setTitle("Client");

        sPanel = new SouthPanel(this ,controller);
        add(sPanel, BorderLayout.SOUTH);

        cPanel = new CenterPanel(controller);
        add(cPanel, BorderLayout.CENTER);

        nPanel = new NorthPanel(this, controller);
        add(nPanel, BorderLayout.NORTH);

        ArrayList<User> receivers = new ArrayList<>();
        receivers.add(new User("Ludvig", null));
        Message message = new Message("Heyyo", null, receivers, new ImageIcon("files/gubbe.jpg"));
        cPanel.append(message);

        pack();
        setVisible(true);



    }

    /**
     * Setting an instance of ImageIcon
     * @param image Received ImageICon
     */
    public void setImage(ImageIcon image){
        this.image = image;
    }

    /**
     * Gets ImageIcon
     * @return ImageICon
     */
    public ImageIcon getImage(){
        return image;
    }

    /**
     * Receives and sets list of receivers
     * @param users list of users
     */
    public void setReceiverList(ArrayList<User> users){
        receiverList.clear();
        receiverList = users;
    }

    /**
     * Gets the receiver list
     * @return receiver list
     */
    public ArrayList<User> getReceiverList(){
        return receiverList;
    }

    /**
     * Method that enables/disables buttons on the panels depending on whether a connection is established or not
     * @param connected Boolean variable
     */
    public void enableDisableButtons(Boolean connected){
        nPanel.enableDisableButtons(connected);
        sPanel.enableDisableButtons(connected);
    }


    /**
     * Registered listener receiving objects, if object is a message it's appended to the chat pane
     * @param evt received object
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Message")){
            cPanel.append(evt.getNewValue());
        }
    }
}
