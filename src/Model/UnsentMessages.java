package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {

    /**
     * This class stores all the unsent messages inside a HashMap
     *
     * @version 1.0
     * @author Mahmoud Daabas
     */

    private HashMap<String, ArrayList<Message>> storedMessages = new HashMap<String, ArrayList<Message>>();

    /**
     * Adds an unsent message to the hash map.
     *
     * @param username this is the username of the reciever.
     * @param message this is the message that was supposed to be sent.
     */

    public synchronized void addMessage(String username, Message message) {

        if(get(username) == null) {
            storedMessages.put(username, new ArrayList<Message>());
        }
        get(username).add(message);
    }

    /**
     * This method returns the unsent messages to a user in a list.
     *
     * @param username this is the username of the reciever.
     * @return Returns an ArrayList with all the unsent messages for the specific user.
     */

    public synchronized ArrayList<Message> get(String username) {
        return storedMessages.get(username);
    }

    /**
     * This method is used to delete the stored messages after they have been sent.
     *
     * @param username this is the username of the reciever.
     */

    public synchronized void delete(String username) {
        storedMessages.remove(username);
    }


}
