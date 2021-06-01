package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.View.Login.LoginFrame;
import Ludvig.Client.View.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Top panel that holds a selection of buttons
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
 */
public class NorthPanel extends JPanel {
    private JButton contacts;
    private JButton connect;
    private JButton disconnect;

    private LoginFrame loginFrame;
    private ClientController controller;
    private MainView view;
    private Contacts contactsFrame;

    /**
     * Constructs panel, receives references to controller and MainView
     * @param view MainView
     * @param controller ClientController
     */
    public NorthPanel(MainView view, ClientController controller){
        this.view = view;
        this.controller = controller;

        connect = new JButton("Connect");
        connect.addActionListener(this::actionEvent);
        add(connect);
        connect.setVisible(false);

        disconnect = new JButton("Disconnect");
        disconnect.addActionListener(this::actionEvent);
        add(disconnect);

        contacts = new JButton("Contact list");
        contacts.addActionListener(this::actionEvent);
        add(contacts);
    }

    /**
     * Enables or disables buttons depending on incoming parameter
     * @param conn Boolean
     */
    public void enableDisableButtons(Boolean conn){
        connect.setEnabled(!conn);
        disconnect.setEnabled(conn);
        contacts.setEnabled(conn);
    }

    /**
     * Listens to registered ActionListeners
     * @param e Source of call
     */
    public void actionEvent(ActionEvent e){
        if (e.getSource() == contacts){
            if (contactsFrame != null){
                contactsFrame.dispose();
            }
            contactsFrame = new Contacts(view, controller);
        }
        if (e.getSource() == connect){
            if (loginFrame != null){
                loginFrame.dispose();
            }
            //loginFrame = new LoginFrame(controller);
        }

        if (e.getSource() == disconnect){
            controller.disconnect();
            new LoginFrame();
            view.dispose();
        }
    }
}
