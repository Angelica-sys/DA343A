package Ludvig.Client.View.Login;

import Ludvig.Client.Controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

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

    public void closeWindow(){
        dispose();
    }

    private class LoginPanel extends JPanel {
        private JLabel nameLabel;
        private JTextField userName;
        private JButton selectPic;
        private JButton login;
        private String str = "";
        private JFileChooser chooser;

        public LoginPanel(){
            setSize(new Dimension(200, 100));
            setLayout(null);

            nameLabel = new JLabel("Name: ");
            nameLabel.setBounds(75, 100, 50,25);
            add(nameLabel);

            userName = new JTextField("");
            userName.setBounds(125, 100, 150,25);
            add(userName);

            selectPic = new JButton("Select picture");
            selectPic.setBounds(125, 150, 150, 25);
            selectPic.addActionListener(this::actionPerformed);
            add(selectPic);

            login = new JButton("Login");
            login.setBounds(125, 200, 150,25);
            login.addActionListener(this::actionPerformed);
            add(login);
        }

        public void actionPerformed(ActionEvent e){
            if (e.getSource() == login){
                String name = userName.getText();
                if (name.equals("") || str.equals("")){
                    JOptionPane.showMessageDialog(null, "Username and picture must be selected", "Error", JOptionPane.WARNING_MESSAGE);
                }else {
                    if (controller.connect(name, str)){
                        closeWindow();
                    }else {
                        JOptionPane.showMessageDialog(null, "Couldn't connect, try again", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } else if (e.getSource() == selectPic){
                if (chooser == null){
                    chooser = new JFileChooser();
                }
                chooser.showOpenDialog(null);
                str = chooser.getSelectedFile().getAbsolutePath();
                System.out.println(str);

            }
        }
    }

}
