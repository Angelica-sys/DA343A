package Ludvig.Server.server;

import Ludvig.Server.controller.ClientListenerInterface;
import Ludvig.Server.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClientHandler is the class that will handle the connection between the server and a connected client
 * @version 1.0
 */
public class ClientHandler {
    private Socket socket;
    private ClientListenerInterface listener;
    private OutputThread ot;
    private InputThread it;

    /**
     * Class constructor that saves the references to it's socket and callback reference, and also creates and starts
     * the two inner Thread-classes that ClientHandler uses
     * @param socket The client's socket to connect to the server
     * @param listener The callback reference from the Controller
     */
    public ClientHandler(Socket socket, ClientListenerInterface listener){
        this.socket=socket;
        this.listener=listener;
        it = new InputThread();
        ot = new OutputThread();
        ot.start();
        it.start();
    }

    /**
     * sendObject sends an object to the inner class InputThread
     * @param o An object that will be sent through the server-client connection
     */
    public void sendObject(Object o){
        ot.addObject(o);
    }

    /**
     * closeClient sets up the closing process of disconnecting the server and a client
     */
    public void closeClient(){
            listener.closeClient(this);
        if(!socket.isClosed() && socket.isConnected()){
            try {
                socket.close();
                System.out.println("Closing " + ot.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        it.interrupt();
        ot.interrupt();
        ot = null;
        it = null;
    }

    /**
     * OutputThread is an inner class that will be the thread that will continually checks for objects to be
     * sent through the ObjectStream between the server and a client
     * @author Johan, Niklas
     * @version 1.0
     */
    private class OutputThread extends Thread {
        private Buffer<Object> buffer = new Buffer<Object>();
        private ObjectOutputStream oos;

        /**
         * The thread will setup the ObjectOutputStream and check if the buffer has any objects to be sent
         * If so then they are sent through the stream
         */
        public void run(){
            try{
                System.out.println("Client " + toString() + " running");
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
            while(!Thread.interrupted()){
                try {
                    Object o = buffer.get();
                    if(o instanceof String){
                        String command = (String)o;
                        oos.writeUTF(command);
                    }
                    else {
                        oos.writeObject(o);
                    }
                } catch (IOException e) {
                    closeClient();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Adds objects to the buffer
         * @param o An object
         */
        public void addObject(Object o){
            buffer.put(o);
        }
    }

    /**
     * InputThread is an inner class that will be the thread that will continually checks for objects to be
     * received through the ObjectStream between a client and the server
     * @author Johan, Niklas
     * @version 1.0
     */
    private class InputThread extends Thread {
        private ObjectInputStream ois;
        private Object o;

        /**
         * The thread will setup the ObjectInputStream and check if there are any objects to be received
         * If so then they are sent through the callback-object listener
         */
        public void run(){
            System.out.println("Lyssnar på klient");
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                o = ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            listener.sendUser(o, ClientHandler.this);
            while(!Thread.interrupted()){
                System.out.println("SNURRAR");
                try{
                    o = ois.readObject();
                    if (o instanceof Ludvig.Client.Model.User){
                        System.out.println("USER MOTTAGEN");

                    } else if (o instanceof  Ludvig.Client.Model.Message){
                        listener.sendMessage(o);
                    }
                } catch (IOException e) {
                    closeClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("STÄNGER TRÅD");
        }
    }
}


