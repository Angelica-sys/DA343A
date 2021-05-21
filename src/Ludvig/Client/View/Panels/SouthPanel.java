package Ludvig.Client.View.Panels;

import Ludvig.Client.Controller.ClientController;
import Ludvig.Client.View.MainView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel holding buttons and textfield for outgoing messages
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
 */
public class SouthPanel extends JPanel {
    private ClientController controller;
    private MainView view;

    private SouthCenter southCenter;
    private SouthSouth southSouth;

    /**
     * Constructs panel
     * @param view MainView
     * @param controller ClientController
     */
    public SouthPanel(MainView view, ClientController controller){
        this.view = view;
        this.controller = controller;
        setLayout(new BorderLayout());
        southCenter = new SouthCenter();
        southSouth = new SouthSouth();
        add(southCenter, BorderLayout.CENTER);
        add(southSouth, BorderLayout.SOUTH);
    }

    /**
     * Enables or disables buttons depending on incoming parameter
     * @param connected Boolean
     */
    public void enableDisableButtons(Boolean connected) {
        southSouth.attachPicture.setEnabled(connected);
        southSouth.sendMessage.setEnabled(connected);
        southCenter.messageField.setEnabled(connected);
    }


    /**
     * Inner class representing a panel
     */
    public class SouthSouth extends JPanel{
        private JButton attachPicture;
        private JButton sendMessage;

        /**
         * Constructing panel
         */
        public SouthSouth(){
            attachPicture = new JButton("Select picture");
            attachPicture.addActionListener(this::actionPerformed);
            add(attachPicture);

            sendMessage = new JButton("Send Message");
            sendMessage.addActionListener(this::actionPerformed);
            add(sendMessage);
        }

        /**
         * Method used to select a picture for a message
         * @return String with filepath
         */
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

        /**
         * Listens to registered ActionListeners
         * @param e Source of call
         */
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

    /**
     * Panel holding textfield where user enters text for outgoing message
     */
    public class SouthCenter extends JPanel{
        private JTextField messageField;

        public SouthCenter(){
            setLayout(new BorderLayout());
            messageField = new JTextField("Your message here");
            add(messageField, BorderLayout.CENTER);
        }
    }
}
