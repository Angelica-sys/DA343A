package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.View.MainView;
import Ludvig.Main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SouthPanel extends JPanel {
    private ClientController controller;
    private MainView view;

    private SouthCenter southCenter;
    private SouthSouth southSouth;

    public SouthPanel(MainView view, ClientController controller){
        this.view = view;
        this.controller = controller;
        setLayout(new BorderLayout());
        southCenter = new SouthCenter();
        southSouth = new SouthSouth();
        add(southCenter, BorderLayout.CENTER);
        add(southSouth, BorderLayout.SOUTH);
    }

    public void enableDisableButtons(Boolean connected) {
        southSouth.attachPicture.setEnabled(connected);
        southSouth.sendMessage.setEnabled(connected);
        southCenter.messageField.setEnabled(connected);
    }


    public class SouthSouth extends JPanel{
        private JButton attachPicture;
        private JButton sendMessage;

        public SouthSouth(){
            attachPicture = new JButton("Select picture");
            attachPicture.addActionListener(this::actionPerformed);
            add(attachPicture);

            sendMessage = new JButton("Send Message");
            sendMessage.addActionListener(this::actionPerformed);
            add(sendMessage);
        }

        public String selectPicture(){
            String path = "";
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter extFilter = new FileNameExtensionFilter("*.Images", "jpg", "jpeg", "gif","png");
            chooser.addChoosableFileFilter(extFilter);
            int result = chooser.showSaveDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                path = chooser.getSelectedFile().getAbsolutePath();
            }
            return path;
        }

        public void actionPerformed(ActionEvent e){
            if (e.getSource() == sendMessage){
                if (view.getReceiverList().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Must specify at least 1 receiver", "Error", JOptionPane.WARNING_MESSAGE);
                }else {
                    controller.sendMessage(southCenter.messageField.getText(), view.getReceiverList(), view.getImage());
                }
            }
            if (e.getSource() == attachPicture){
                view.setImage(new ImageIcon(selectPicture()));
            }
        }
    }

    public class SouthCenter extends JPanel{
        private JTextField messageField;

        public SouthCenter(){
            setLayout(new BorderLayout());
            messageField = new JTextField("Your message here");
            add(messageField, BorderLayout.CENTER);
        }
    }
}
