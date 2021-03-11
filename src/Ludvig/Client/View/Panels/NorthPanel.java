package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.View.Login.LoginFrame;
import Ludvig.Client.View.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NorthPanel extends JPanel {
    private JButton contacts;
    private JButton connect;
    private JButton disconnect;

    private LoginFrame loginFrame;
    private ClientController controller;
    private MainView view;
    private Contacts contactsFrame;

    public NorthPanel(MainView view, ClientController controller){
        this.view = view;
        this.controller = controller;

        connect = new JButton("Connect");
        connect.addActionListener(this::actionEvent);
        add(connect);

        disconnect = new JButton("Disconnect");
        disconnect.addActionListener(this::actionEvent);
        add(disconnect);

        contacts = new JButton("Contact list");
        contacts.addActionListener(this::actionEvent);
        add(contacts);
    }

    public void enableDisableButtons(Boolean conn){
        connect.setEnabled(!conn);
        disconnect.setEnabled(conn);
        contacts.setEnabled(conn);
    }

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
            loginFrame = new LoginFrame(controller);
        }

        if (e.getSource() == disconnect){
            controller.disconnect();
        }
    }
}
