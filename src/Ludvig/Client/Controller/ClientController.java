package Ludvig.Client.Controller;
import Ludvig.Client.Model.Message;
import Ludvig.Client.Model.User;
import Ludvig.Client.View.Login.LoginFrame;
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
    private Thread receiverThread;
    private PropertyChangeSupport pcs;
    private Message message;
    private ArrayList<User> onlineUsers = new ArrayList<>();
    private Socket socket;


    /**
     * Constructing the class, receiving and setting ip-address, port for the connection.
     * @param ip ip-address
     * @param port port#
     */
    public ClientController(String ip, int port){
        pcs = new PropertyChangeSupport(this);
        this.ip = ip;
        this.port = port;
        view = new MainView(this);
    }


    /**
     * Method connecting to server, creates and sends a user object for verification.
     * User object is created with incoming parameters name and filepath for picture.
     * @param name String of name
     * @param path String of filepath
     */
    public Boolean connect(String name, String path){
        try {
            socket = new Socket(ip, port);
            ImageIcon icon = new ImageIcon(path);
            user = new User(name,icon);
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos.writeObject(user);
                if (receiverThread == null){
                    receiverThread = new Receiver();
                    receiverThread.start();
                }
                System.out.println("Klient uppkopplad");
            } catch (IOException e) {
                return false;
            }
        } catch(Exception e){
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
            oos.writeObject(new Message(1, user.getName())); // skicka meddelande så att tråd stängs på server
            if(ois != null) ois.close();
            if(oos != null) oos.close();
            if(socket != null) socket.close();
        }
        catch(Exception e) {}
        view.enableDisableButtons(false);
    }

    /**
     * Creates Message object from parameters and puts into the stream.
     * @param text The text that is to be sent
     * @param receivers List of receivers
     * @param image Image file to be sent
     */
    public synchronized void sendMessage(String text, ArrayList<User> receivers, ImageIcon image){
        Message newMessage = new Message(text, user.getName(), receivers, image);
        try {
            oos.writeObject(newMessage);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a contact User object in a file on the drive.
     * @param user Received user object
     */
    public void saveContact(User user){
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("files/contacts.bin", true))) {
            output.writeObject(user);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Reads file with saved User objects stored on the drive and places them in an ArrayList.
     * @return Returns ArrayList object
     */
    public ArrayList<User> getSavedContacts(){
        ArrayList<User> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("files/contacts.bin")){
            User user;
            while (true){
                ObjectInputStream input = new ObjectInputStream(fis);
                user = (User) input.readObject();
                list.add(user);

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No more objects to read");
        }
        for (User i: list
        ) {
            System.out.println(i.getName());
        }
        return list;
    }

    public ArrayList<User> getOnlineUsers() {
        return onlineUsers;
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
                    Object object = ois.readObject();
                    if (object instanceof Message){
                        message = (Message) object;
                        message.setTimeMessageReceivedClient(LocalDateTime.now().toString());
                        pcs.firePropertyChange("Message", null, message);
                    } else if (object instanceof ArrayList){
                        onlineUsers.clear();
                        onlineUsers = (ArrayList<User>) object;
                        pcs.firePropertyChange("Users", null, onlineUsers);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
            }
        }
    }

}
