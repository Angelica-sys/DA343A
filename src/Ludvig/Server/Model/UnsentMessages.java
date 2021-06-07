package Ludvig.Server.Model;
import Ludvig.SharedResources.User;
import Ludvig.SharedResources.Message;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * UnsentMessages class stores the Message-object that failed to be sent to a client due to not being connected to the server,
 * in a synchronized HashMap along with the message/s respective recipient
 */
public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> unsent = new HashMap<User, ArrayList<Message>>();

    /**
     * put method places the unsent Message-object in it's recipients(client) ArrayList of unsent Messages
     * @param user The user that couldn't receive a message/s
     * @param message The message that is waiting to be sent to its recipient
     */
    public synchronized void put(User user,Message message) {
        if(findUser(user)){
            findList(user).add(message);
        }
        else {
            ArrayList<Message> list = new ArrayList<Message>();
            unsent.put(user, list);
            findList(user).add(message);
        }
    }

    public synchronized ArrayList<Message> findList(User user){
        return unsent.get(user);
    }

    /**
     * get method retrieves the unsent message/s from a specified user
     * @param user The user whose messages that failed to be sent and is to be retrieved
     * @return ArrayList
     */
    public synchronized ArrayList<Message> get(User user) {
        return unsent.remove(user);
    }

    /**
     * findUser method checks if a specified user exists in the HashMap of unsent Messages
     * @param user The specified user
     * @return boolean
     */
    public synchronized boolean findUser(User user){
        return unsent.containsKey(user);
    }
}