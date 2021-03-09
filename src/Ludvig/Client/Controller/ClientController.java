package Client.Controller;

import Client.Model.Message;
import Client.Model.User;
import Client.View.Login.LoginFrame;
import Client.View.MainView;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller for the client side with inner Thread receiving message objects
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
    public void connect(String name, String path){
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
            } catch (IOException e) {}
        } catch(Exception e){}
    }

    /**
     * Puts Message object from parameter into the stream.
     * @param message Message object.
     */
    public synchronized void sendMessage(Message message){
        try {
            oos.writeObject(message);
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

    /**
     * Tries to sign off and disconnect from the server.
     */
    public void disconnect(){
        try {
            //oos.writeObject(); skicka meddelande så att tråd stängs på server
            if(ois != null) ois.close();
            if(oos != null) oos.close();
            if(socket != null) socket.close();
        }
        catch(Exception e) {}
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
