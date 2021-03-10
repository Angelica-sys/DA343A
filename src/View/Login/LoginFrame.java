package View.Login;

import Controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private ClientController controller;

    public LoginFrame(ClientController controller){
        this.controller = controller;

        setTitle("Login window");
        setResizable(false);
        setPreferredSize(new Dimension(400,400));
        add(new LoginPanel());
        pack();
        setVisible(true);
    }



}
