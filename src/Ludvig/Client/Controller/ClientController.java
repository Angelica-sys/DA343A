package Ludvig.Client.Controller;
import Ludvig.SharedResources.Message;
import Ludvig.SharedResources.User;
import Ludvig.Client.View.MainView;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Controller for the client side with inner Thread receiving message objects
 * @Version 1.0
 * @Author Ludvig Wedin Pettersson
 */
public class ClientController {
    private MainView view;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String ip;
    private int port;
    private User user;
    private Thread inputstreamReceiverThread;
    private PropertyChangeSupport pcs; //Callback
    private Message message;
    private ArrayList<User> onlineContacts = new ArrayList<>();
    private ArrayList<User> savedContacts = new ArrayList<>();
    private Socket socket;


    /**
     * Constructing the class, receiving and setting ip-address, port for the connection.
     * @param ip ip-address
     * @param port port#
     */
    public ClientController(String ip, int port, String name){
        pcs = new PropertyChangeSupport(this);
        this.ip = ip;
        this.port = port;
        view = new MainView(this, name);
    }


    /**
     * Method connecting the client to server, creates and sends a user object for verification.
     * User object is created with incoming parameters name and filepath for picture.
     * @param name String of name
     * @param path String of filepath
     */
    public Boolean connect(String name, String path){
        try {
            socket = new Socket(ip, port);
            ImageIcon icon = new ImageIcon(path);
            user = null;
            user = new User(name,icon);
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos.writeObject(user);
                oos.flush();
                oos.writeObject(user);
                oos.flush();
                if (inputstreamReceiverThread == null){
                    inputstreamReceiverThread = new Receiver();
                    inputstreamReceiverThread.start();
                } System.out.println("Klient uppkopplad");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        view.enableDisableButtons(true);
        //Message s = new Message("MEDDELANDE", "JAG", null);   TESTAR SÅ MEDDELANDE FUNKAR
        //pcs.firePropertyChange("Message", null, s);           TESTAR SÅ MEDDELANDE FUNKAR
        return true;
    }

    /**
     * Tries to sign off and disconnect from the server.
     */
    public void disconnect(){
        try {
            oos.writeObject(new Message(1, user)); // skicka meddelande så att tråd stängs på server
            if(ois != null) ois.close();
            if(oos != null) oos.close();
            if(socket != null) socket.close();
            user = null;
        }
        catch(Exception e) {}
        view.enableDisableButtons(false);
    }

    /**
     * Creates Message object from parameters and puts into the stream.
     * @param text The text that is to be sent
     * @param receiversUserNames List of receivers usernames
     * @param image Image file to be sent
     */
    public synchronized void sendMessage(String text, ArrayList<String> receiversUserNames, ImageIcon image){
        try {
            ArrayList<User> receivers = new ArrayList<>();

            for(String receiverUserName : receiversUserNames){
                for(User onlineUser : onlineContacts){
                    if(receiverUserName.equals(onlineUser.getUsername())){
                        receivers.add(onlineUser);
                    }
                }
                for (User savedContact : savedContacts){
                    if (receiverUserName.equals(savedContact.getUsername()) && !receivers.contains(savedContact)){
                        receivers.add(savedContact);
                    }
                }
            }

            Message newMessage = new Message(text, user, receivers, image);
            oos.writeObject(newMessage);
            oos.flush();
            oos.flush();
            System.out.println("Meddelande skickat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a contact User object in a file on the drive.
     * @param username Received user object
     */
    public void saveContact(String username){
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("files/contacts-" + user.getUsername() + ".bin", true))) {
            for(User user : onlineContacts){
                if(user.getUsername().equals(username)){
                    output.writeObject(user);
                    output.flush();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        getSavedContacts();
    }

    /**
     * Reads file with saved User objects stored on the drive and places them in an ArrayList.
     * @return Returns ArrayList object
     */
    public ArrayList<String> getSavedContacts(){
        ArrayList<String> userNames = new ArrayList<>();
        savedContacts.clear();
        try (FileInputStream fis = new FileInputStream("files/contacts-" + user.getUsername() + ".bin")){
            User user;
            while (true){
                ObjectInputStream input = new ObjectInputStream(fis);
                user = (User) input.readObject();
                savedContacts.add(user);
                userNames.add(user.getUsername());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("No more objects to read");
        }
        return userNames;
    }

    public ArrayList<String> getOnlineUserNames() {
        ArrayList<String> onlineUserNames = new ArrayList<>();

        for(User user : onlineContacts){
            onlineUserNames.add(user.getUsername());
        }
        return onlineUserNames;
    }



    /**
     * Adds a listener to the class
     * @param listener
     */
    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Thread receiving Objects from the stream
     */
    private class Receiver extends Thread{

        /**
         * Method that grabs objects from the stream
         */
        public void run(){
            try {
                System.out.println("Klient lyssnar på: " + port);
                while (true) {
                    System.out.println("Kör");
                    Object object = ois.readObject();
                    if (object instanceof Message){
                        String messageString = null;

                        message = (Message) object;
                        message.setTimeMessageReceivedClient(LocalDateTime.now().toString());
                        System.out.println(message.toString());

                        messageString = message.getText() + " sent from " + message.getSender().getUsername() + " Time received: " + message.getTimeMessageReceivedClient();

                        if(message.getImage() != null){
                            pcs.firePropertyChange("Picture", null, message.getImage());
                        }
                        pcs.firePropertyChange("Message", null, messageString);
                        System.out.println("Klient har fått ett meddelande från server");
                    } else if (object instanceof ArrayList){
                        onlineContacts.clear();
                        onlineContacts = (ArrayList<User>) object;
                        pcs.firePropertyChange("Users", null, onlineContacts);
                        System.out.println("Klient har fått ny användarlista");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
