package Ludvig.Client.View;

import Client.Model.Message;
import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.Model.User;
import Ludvig.Client.View.Panels.CenterPanel;
import Ludvig.Client.View.Panels.NorthPanel;
import Ludvig.Client.View.Panels.SouthPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MainView extends JFrame implements PropertyChangeListener {
    private ClientController controller;
    private SouthPanel sPanel;
    private CenterPanel cPanel;
    private NorthPanel nPanel;
    private ArrayList<User> receiverList;
    private ImageIcon image;


    public MainView(ClientController controller){
        this.controller = controller;
        receiverList = new ArrayList<>();
        controller.addListener(this);
        setupFrame();
    }

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

    public void setImage(ImageIcon image){
        this.image = image;
    }

    public ImageIcon getImage(){
        return image;
    }

    public void setReceiverList(ArrayList<User> users){
        receiverList.clear();
        receiverList = users;
    }

    public ArrayList<User> getReceiverList(){
        return receiverList;
    }

    public void enableDisableButtons(Boolean connected){
        nPanel.enableDisableButtons(connected);
        sPanel.enableDisableButtons(connected);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Message")){
            cPanel.append(evt.getNewValue());
        }
    }
}
