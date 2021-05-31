package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that holds the chat
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
 */
public class CenterPanel extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> listPane;
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
    public void append(String message) {
        listModel.addElement(message);
    }
}
