package Ludvig.Server.Model;

import Ludvig.Server.Controller.ClientHandler;
import Ludvig.SharedResources.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * OnlineClients is a synchronized HashMap containing the online users and their respective ClientHandler
 * @version 1.0
 */
public class OnlineClients {
    private HashMap<User, ClientHandler> clients = new HashMap<User, ClientHandler>();

    /**
     * put places a new user and it's ClientHandler in the HashMap
     * @param user A new online user
     * @param client The online user's ClientHandler
     */
    public synchronized void put(User user, ClientHandler client){
        clients.put(user, client);
    }

    /**
     * get retreives the ClientHandler that is used by the specified user
     * @param user The specified user
     * @return ClientHandler
     */
    public synchronized ClientHandler get(User user){
        return clients.get(user);
    }

    /**
     * findUser checks if the specified user exists in the HashMap
     * @param user The specified user
     * @return boolean
     */
    public synchronized boolean findUser(User user){
        return clients.containsKey(user);
    }

    /**
     * removeClient removes an entry of a User-ClientHandler in the HashMap if the specified client exists as
     * a value in any entry pair within the HashMap
     * @param client The specified ClientHandler
     * @return boolean
     */
    public synchronized boolean removeClient(ClientHandler client){
        boolean removed = false;
        Iterator iter = clients.keySet().iterator();
        while(iter.hasNext()){
            User key = (User)iter.next();
            if(clients.get(key).equals(client)){
                iter.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * getOnlineList retrieves an ArrayList of the current active users in the system
     * @return ArrayList
     */
    public synchronized ArrayList<User> getOnlineList(){
        ArrayList<User> users = new ArrayList<User>();
        for(User u : clients.keySet()){
            users.add(u);
        }
        return users;
    }


}
