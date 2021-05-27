package Ludvig.Server.model;
import Ludvig.SharedResources.User;
import Ludvig.SharedResources.Message;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * UnsentMessages stores the Message-object that failed to be sent, in a synchronized
 * HashMap along with their respective recipient
 */
public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> unsent = new HashMap<User, ArrayList<Message>>();

    /**
     * put places the unsent Message-object in it's recipients ArrayList of unsent Messages
     * @param user The user who couldn't receive a message
     * @param message The message that was to be sent
     */
    public synchronized void put(User user,Message message) {
        if(findUser(user)){
            get(user).add(message);
        }
        else {
            ArrayList<Message> list = new ArrayList<Message>();
            list.add(message);
            unsent.put(user, list);
        }
    }

    /**
     * get retrieves the unsent messages from a specified user
     * @param user The user whose unsent messages is to be retrieved
     * @return ArrayList
     */
    public synchronized ArrayList<Message> get(User user) {
        return unsent.get(user);
    }

    /**
     * findUser checks if a specified user exists in the HashMap
     * @param user The specified user
     * @return boolean
     */
    public synchronized boolean findUser(User user){
        return unsent.containsKey(user);
    }
}