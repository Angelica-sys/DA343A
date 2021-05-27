package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Ludvig.SharedResources.Message;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that holds the chat
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
 */
public class CenterPanel extends JPanel {
    private DefaultListModel<Object> listModel;
    private JList<Object> listPane;
    private ClientController controller;

    /**
     * Constructs the panel
     * @param controller
     */
    public CenterPanel(ClientController controller){
        this.controller = controller;
        listModel = new DefaultListModel<>();
        listPane = new JList<>(listModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(listPane), BorderLayout.CENTER);
    }

    /**
     * Method for adding messages to the chat
     * @param message
     */
    public void append(Object message) {
        if(message instanceof Message) {
            Message m = (Message) message;
            listModel.addElement(m.getImage());
            listModel.addElement(m.getText()+ " sent from " + m.getSender() + " Time received: " + m.getTimeMessageReceivedClient() );
        }

        else {
            listModel.addElement(message);
        }

    }
}
