package Ludvig.Server.Controller;

import Ludvig.SharedResources.Message;
import Ludvig.SharedResources.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class used to handle clients that connect to the server.
 */
public class ClientHandler {
    private Socket socket;
    private InputThread it;
    private Controller controller;
    private ObjectOutputStream oos;

    /**
     * Class constructor that saves the references to it's socket and controller reference, and also creates and starts
     * the inner Thread-class that ClientHandler uses
     * @param socket The client's socket to connect to the server
     * @param controller Reference to controller
     */
    public ClientHandler(Socket socket, Controller controller)
    {
        try {
            this.socket = socket;
            this.controller = controller;
            it = new InputThread();
            it.start();
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an object to the connected client.
     * @param o Object to send to the client.
     */
    public void sendObject(Object o) {
        try {
            oos.writeObject(o);
            oos.flush();
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * closeClient sets up the closing process of disconnecting the server and a client
     */
    public void closeClient(){
        if(!socket.isClosed() && socket.isConnected())
        {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        it.interrupt();
        it = null;
    }




    /**
     * InputThread is an inner class with a thread that will continually checks for objects to be
     * received through the ObjectInputStream between a client and the server
     * @version 1.0
     */
    private class InputThread extends Thread
    {
        private ObjectInputStream ois;
        private Object o;

        /**
         * The thread will setup the ObjectInputStream and check if there are any objects to be received
         * If so then they are sent through the callback-object listener
         */
        public void run(){
            System.out.println("Lyssnar på klient");
            try
            {
                ois = new ObjectInputStream(socket.getInputStream());
                o = ois.readObject();
            } catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            while(!Thread.interrupted()){
                try{
                    o = ois.readObject();
                    if (o instanceof User){
                        User u = (User) o;
                        controller.loginUser(u, ClientHandler.this);
                    } else if (o instanceof Message){
                        Message m = (Message) o;
                        m.setTimeReceivedByServer();
                        if (m.getLogout() == 0)
                            controller.sendMessage(m);
                        else
                            controller.removeClient(ClientHandler.this);
                    }
                } catch (IOException e) {
                    closeClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            controller.removeClient(ClientHandler.this);
            System.out.println("STÄNGER TRÅD");
        }
    }
}
