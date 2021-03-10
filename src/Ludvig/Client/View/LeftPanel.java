package Client.View;

import Client.Controller.ClientController;
import Client.View.Login.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftPanel extends JPanel{
    private JButton jButton;
    private ClientController controller;

    public LeftPanel(ClientController controller){
        this.controller = controller;
        jButton = new JButton("Login");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(controller);
            }
        });
        add(jButton);
    }
}
