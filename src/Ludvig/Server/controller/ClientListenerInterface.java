package Ludvig.Server.controller;

import Ludvig.Server.server.ClientHandler;

/**
 * A callback interface to be used for connecting ClientHandler and Constructor
 * @version 1.0
 */
public interface ClientListenerInterface {
    void sendUser(Object user, ClientHandler client);
    void sendMessage(Object message);
    void closeClient(ClientHandler client);
}
