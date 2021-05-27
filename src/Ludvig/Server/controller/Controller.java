package Ludvig.Server.controller;

import Ludvig.Server.model.Message;
import Ludvig.Server.model.OnlineClients;
import Ludvig.Server.model.UnsentMessages;
import Ludvig.Server.model.User;
import Ludvig.Server.server.ClientHandler;
import Ludvig.Server.server.Server;
import Ludvig.Server.view.Gui;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Controller is the class that handles all the logical functions of the program
 * @version 1.0
 */
public class Controller {
    private OnlineClients clients = new OnlineClients();
    private UnsentMessages unsent = new UnsentMessages();
    private ObjectOutputStream oos;

    /**
     * Class constructor. Creates a new Server-object, Gui and ObjectOutputStream
     * @param port Server port
     */
    public Controller(int port){
        new Server(port, new ClientListener());
        try {
            new Gui(InetAddress.getLocalHost(), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("files/ServerLog.dat")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeToLog("Server online");
    }

    /**
     * loginUser checks if the connecting user already exists as an active user in the system and
     * either accepts or denies the users connection depending on the result
     * @param user The user that is trying to connect to the server
     * @param client The connecting user's client handler
     */
    public void loginUser(User user, ClientHandler client){
        if(!clients.findUser(user)) {
            client.sendObject("loginAccept");
            clients.put(user, client);
            sendUserList();
            sendUnsentMessages(user);
            writeToLog(user.getUsername() + " logged in");
        }
        else {
            client.sendObject("loginDenied");
            client.closeClient();
            writeToLog(user.getUsername() + " denied log in");
        }
    }

    /**
     * removeClient removes an active user in the system if that user's client is the
     * same as the client in the parameter
     * @param client The client to be removed
     */
    public void removeClient(ClientHandler client){
        if(clients.removeClient(client)){
            sendUserList();
            writeToLog("Client " + client.toString() + " removed");
        }
    }

    /**
     * sendMessage takes a Message-object in the parameter and sends it forward to the users in the recipient list
     * within the Message-object
     * @param message The message that server received and need to send forward
     */
    public void sendMessage(Message message){
        for (User user : message.getToUser()) {
            if (clients.findUser(user)) {
                message.setTimeReceived(new Date().toString());
                clients.get(user).sendObject("message");
                clients.get(user).sendObject(message);
                writeToLog("Sent message");
            }
            else {
                unsent.put(user, message);
                writeToLog("Message stored until recipient is online");
            }
        }
    }

    /**
     * sendUnsentMessage sends stored Message-object that have not yet been sent to the User-object in the parameter
     * @param user The recipient of the unsent messages
     */
    public void sendUnsentMessages(User user){
        if(unsent.findUser(user)){
            for(Message m : unsent.get(user)){
                sendMessage(m);
                writeToLog("Sent message from storage");
            }
        }
    }

    /**
     * sendUserList sends an ArrayList of the current active users to each active user
     */
    public void sendUserList(){
        ArrayList<User> onlineList = clients.getOnlineList();
        for(User u : onlineList){
            ClientHandler cl = clients.get(u);
            cl.sendObject("online");
            cl.sendObject(onlineList);
            writeToLog("Sent online-list");
        }
    }

    /**
     * writeToLog writes the current date and then the parameter line to a .dat file
     * @param line Information about the action performed by the Controller
     */
    public void writeToLog(String line){
        try {
            oos.writeObject(new Date());
            oos.writeObject(line);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ClientListener is a callback class to handle the communication between Controller and ClientHandler
     * @author Johan, Niklas
     * @version 1.0
     */
    private class ClientListener implements ClientListenerInterface {

        /**
         * sendUser receives the user trying to connect and a reference of it's client handler, from the ClientHandler
         * @param o Object that will always be a User-object, but ClientHandler don't have access to the User-class
         * @param client A reference to the connecting user's client handler
         */
        @Override
        public void sendUser(Object o, ClientHandler client) {
            if(o instanceof User){
                User user = (User)o;
                loginUser(user, client);
                writeToLog("User " + user.getUsername() + " logged in");
            }
        }

        /**
         * sendMessage is called by the ClientHandler-object
         * @param o Object that will always be a User-object, but ClientHandler don't have access to the User-class
         */
        @Override
        public void sendMessage(Object o) {
            Message message = (Message)o;
            message.setTimeSent(new Date().toString());
            Controller.this.sendMessage(message);
            writeToLog("Sent message");
        }

        /**
         * closeClient is used by the  ClientHandler-object to calls for it's connection to be closed.
         * @param client A reference to the ClientHandler-object whose connection is being closed
         */
        @Override
        public void closeClient(ClientHandler client){
            removeClient(client);
            writeToLog("Removed client " + client);
        }
    }
}
