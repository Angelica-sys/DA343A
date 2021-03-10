package View;

import Controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainView extends JFrame implements PropertyChangeListener {
    private ClientController controller;
    private LeftPanel lPanel;


    public MainView(ClientController controller){
        this.controller = controller;
        controller.addListener(this);
        setupFrame();
    }

    public void setupFrame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400,800));
        setTitle("Client");

        lPanel = new LeftPanel(controller);
        add(lPanel, BorderLayout.WEST);

        pack();
        setVisible(true);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
