package Ludvig.bort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class LoginPanel extends JPanel {
    private JLabel nameLabel;
    private JTextField userName;
    private JButton selectPic;
    private JButton login;
    private String str;
    private JFileChooser chooser;

    public LoginPanel(){
        setSize(new Dimension(200, 100));
        setLayout(null);

        nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(75, 100, 50,25);
        add(nameLabel);

        userName = new JTextField("Enter name here");
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
