package Ludvig.Server.Model;

import Ludvig.Server.Controller.ClientHandler;
import Ludvig.SharedResources.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Class handling all connected clients, stores them in HashMap
 * @Author Ludvig Wedin Pettersson, Jonathan Engström, Angelica Asplund
 * @version 1.0
 */
public class OnlineClients {
    private HashMap<User, ClientHandler> clients = new HashMap<User, ClientHandler>();
    private final Object lock = new Object();

    /**
     * Adds User and its ClientHandler to HashMap
     * @param user Connecting user
     * @param client Users ClientHandler
     */
    public synchronized void put(User user, ClientHandler client){
        synchronized (lock){
            clients.put(user, client);
        }
    }

    /**
     * Get fetches a specified users ClientHandler
     * @param user User object
     * @return ClientHandler
     */
    public synchronized ClientHandler get(User user){
        synchronized (lock){
            return clients.get(user);
        }
    }

    /**
     * Checks if a user already exists in the HAshMap
     * @param user The specified user
     * @return Boolean
     */
    public synchronized boolean findUser(User user){
        synchronized (lock){

        }
        return clients.containsKey(user);
    }

    /**
     * Removes position in the HashMap using a specified ClientHandler
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
     * Creates and returns an ArrayList with all online users
     * @return ArrayList
     */
    public synchronized ArrayList<User> getOnlineList(){
        synchronized (lock){
            ArrayList<User> users = new ArrayList<User>();
            for(User u : clients.keySet()){
                users.add(u);
            }
            return users;
        }
    }


}
