package Ludvig.Server.Controller;

import Ludvig.SharedResources.Message;
import Ludvig.SharedResources.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class that handles connected clients
 * @Author Ludvig Wedin Pettersson, Jonathan Engström, Angelica Asplund
 */
public class ClientHandler {
    private Socket socket;
    private InputThread it;
    private Controller controller;
    private ObjectOutputStream oos;

    /**
     * Class constructor that saves the references to it's socket and controller reference, and also creates and starts
     * the inner Thread-class that ClientHandler uses
     * @param socket Socket to connect to the server
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
     * Sends object to the connected client.
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
     * closeClient handles disconnecting clients
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
        controller.removeClient(ClientHandler.this);
    }




    /**
     * InputThread is a private inner class that is a thread, the thread will check for incoming objects from the InputStream
     * @version 1.0
     */
    private class InputThread extends Thread
    {
        private ObjectInputStream ois;
        private Object o;

        /**
         * Run method of the Thread, will run as long as client is connected to server.
         * Checks for incoming objects and handles them depending on type.
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
                            closeClient();
                    }
                } catch (IOException e) {
                    closeClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            closeClient();
            System.out.println("STÄNGER TRÅD");
        }
    }
}
