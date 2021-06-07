package Ludvig.Server.Model;

import Ludvig.Server.Controller.ClientHandler;
import Ludvig.SharedResources.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * OnlineClients is a synchronized HashMap containing the online users with their ClientHandler
 * Class handling all connected clients and stores them in a HashMap
 * @Author Ludvig Wedin Pettersson, Jonathan Engström, Angelica Asplund
 * @version 1.0
 */
public class OnlineClients {
    private HashMap<User, ClientHandler> clients = new HashMap<User, ClientHandler>();
    private final Object lock = new Object();

    /**
     * the put method places a new user and its ClientHandler in the HashMap clients
     * Adds User and its ClientHandler to HashMap
     * @param user Connecting user
     * @param client The online user's ClientHandler
     */
    public synchronized void put(User user, ClientHandler client){
        synchronized (lock){
            clients.put(user, client);
        }
    }

    /**
     * the get method retreives the user (from the input parameter) and its ClientHandler.
     * @param user The specified user object
     * @return ClientHandler
     */
    public synchronized ClientHandler get(User user){
        synchronized (lock){
            return clients.get(user);
        }
    }

    /**
     * findUser method checks if the user (from the input parameter) exists in the HashMap clients
     * @param user The specified user
     * @return Boolean
     */
    public synchronized boolean findUser(User user){
        synchronized (lock){

        }
        return clients.containsKey(user);
    }

    /**
     * removeClient removes an entry position of a User-ClientHandler pair(key,value) in the HashMap if that client exists as
     * a value in any entry pair in the HashMap
     * @param client The specified ClientHandler
     * @return boolean
     */
    public synchronized boolean removeClient(ClientHandler client){
        boolean removed = false;
        synchronized (lock){
            for (Map.Entry me : clients.entrySet()){
                if (me.getValue() == client){
                    clients.remove(me.getKey());
                    System.out.println("CLIENT REMOVED");
                    removed = true;
                    for (Map.Entry m : clients.entrySet()){
                        System.out.println(m.getValue().toString());
                    }
                }
            }
        }
        return removed;
    }

    /**
     * getOnlineList method creates and return an ArrayList of the current active client connected to the server.
     * @return ArrayList
     */
    public synchronized ArrayList<User> getOnlineClientsList(){
        synchronized (lock){
            ArrayList<User> users = new ArrayList<User>();
            for(User u : clients.keySet()){
                users.add(u);
            }
            return users;
        }
    }


}
