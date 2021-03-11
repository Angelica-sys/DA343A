package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Client.Model.Message;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends JPanel {
    private DefaultListModel<Object> listModel;
    private JList<Object> listPane;
    private ClientController controller;

    public CenterPanel(ClientController controller){
        this.controller = controller;
        listModel = new DefaultListModel<>();
        listPane = new JList<>(listModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(listPane), BorderLayout.CENTER);
    }

    public void append(Object message) {
        if(message instanceof Message) {
            Message m = (Message) message;
            listModel.addElement(m.getImage());
            listModel.addElement(m.getText()+ " sent from " + m.getSender());
        }

        else {
            listModel.addElement(message);
        }

    }
}