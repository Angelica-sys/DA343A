package Ludvig.Server.server;

import Ludvig.Server.controller.ClientListenerInterface;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server is the systems server that is a multithreaded server and uses a tcp connection to it's clients.
 * @version 1.0
 */
public class Server extends Thread{
    private ClientListenerInterface listener;
    private ServerSocket serverSocket;

    /**
     * Class constructor that sets up the ServerSocket and starts the server thread
     * @param port The port used by the ServerSocket
     * @param listener The callback class that will handle the connection between the Controller and the ClientHandlers
     */
    public Server(int port, ClientListenerInterface listener){
        this.listener=listener;
        try{
          serverSocket = new ServerSocket(port);
          start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The thread will listen for new clients to connect and when it happens, creates a new ClientHandler-object to
     * handle the connected clients connection
     */
    public void run(){
        while(!Thread.interrupted()){
            try{
                new ClientHandler(serverSocket.accept(), listener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
