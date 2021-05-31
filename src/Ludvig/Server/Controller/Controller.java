package Ludvig.Server.Controller;
import Ludvig.Server.View.Gui;
import Ludvig.SharedResources.Message;
import Ludvig.SharedResources.User;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import Ludvig.Server.Model.*;


public class Controller {
    private OnlineClients clients = new OnlineClients();
    private UnsentMessages unsent = new UnsentMessages();
    private ObjectOutputStream oos;

    /**
     * Class constructor. Creates a new Server-object, Gui and ObjectOutputStream
     * @param port Server port
     */
    public Controller(int port)
    {
        new Server(port);
        try
        {
            new Gui(InetAddress.getLocalHost(), port);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        try
        {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("files/ServerLog.dat")));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
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
    public void loginUser(User user, ClientHandler client)
    {
        if(!clients.findUser(user))
        {
            clients.put(user, client);
            sendUserList();
            sendUnsentMessages(user);
            writeToLog(user.getUsername() + " logged in");
            System.out.println("Accepted login");
        }
        else
        {
            client.closeClient();
            writeToLog(user.getUsername() + " denied log in");
            System.out.println("denied login");
        }
    }

    /**
     * removeClient removes an active user in the system if that user's client is the
     * same as the client in the parameter
     * @param client The client to be removed
     */
    public void removeClient(ClientHandler client)
    {
        if(clients.removeClient(client))
        {
            System.out.println("Användare borttagen");
            sendUserList();
            writeToLog("Client " + client.toString() + " removed");
        }
    }

    /**
     * sendMessage takes a Message-object in the parameter and sends it forward to the users in the recipient list
     * within the Message-object
     * @param message The message that server received and need to send forward
     */
    public synchronized void sendMessage(Message message)
    {
        for (User user : message.getReceivers())
        {
            if (clients.findUser(user))
            {
                try {
                    clients.get(user).sendObject(message);
                    writeToLog("Sent message");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Hittade inte mottagare");
                unsent.put(user, message);
                writeToLog("Message stored until recipient is online");
            }
        }
    }

    /**
     * sendUnsentMessage sends stored Message-object that have not yet been sent to the User-object in the parameter
     * @param user The recipient of the unsent messages
     */
    public void sendUnsentMessages(User user)
    {
        if(unsent.findUser(user))
        {
            System.out.println("Hittat osända");
            ArrayList<Message> unsentMessages = unsent.get(user);
            for(Message m : unsentMessages)
            {
                try {
                    sendMessage(m);
                    writeToLog("Sent message from storage");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * sendUserList sends an ArrayList of the current active users to each active user
     */
    public void sendUserList()
    {
        ArrayList<User> onlineList = clients.getOnlineList();
        for(User u : onlineList)
        {
            ClientHandler cl = clients.get(u);
            cl.sendObject(onlineList);
            writeToLog("Sent online-list");
        }
    }

    /**
     * writeToLog writes the current date and then the parameter line to a .dat file
     * @param line Information about the action performed by the Controller
     */
    public void writeToLog(String line)
    {
        try
        {
            oos.writeObject(new Date());
            oos.writeObject(line);
            oos.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Thread used to create connections to connecting clients.
     */
    private class Server extends Thread
    {
        private ServerSocket serverSocket;

        public Server(int port)
        {
            try
            {
                serverSocket = new ServerSocket(port);
                start();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * The thread will listen for new clients to connect and when it happens, creates a new ClientHandler-object to
         * handle the connected clients connection
         */
        public void run()
        {
            while(!Thread.interrupted())
            {
                try
                {
                    new ClientHandler(serverSocket.accept(), Controller.this);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
