package server;

import entity.Message;
import entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private Object message = null;
    private final LinkedList<ClientHandler> clientHandlers = new LinkedList<>();
    private final LinkedList<User> connectedUsers = new LinkedList<>();

    private static int nextClientId = 0;
    private static int numberOfListenersActive = 0;

    public static void main(String[] args) {
        new Server(25000);
    }

    public Server(int port){
        try{
            serverSocket = new ServerSocket(port);
            new Thread(this).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Run-method for the main server thread. Waits for a client to connect and then creates a thread to handle it.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                clientHandlers.add( new ClientHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread{
        private Socket socket;
        private int clientHandlerId;
        private final Object lock = new Object();
        private ObjectOutputStream oos;

        private User connectedUser;

        public ClientHandler(Socket socket) throws IOException{
            this.start();
            this.socket = socket;
            oos = new ObjectOutputStream(this.socket.getOutputStream());
            new Listener().start();

            clientHandlerId = nextClientId;
            nextClientId++;
            System.out.println("ClientHandler" + clientHandlerId + ": Created.");
        }

        /**
         * Run-method for ClientHandler-threads. Puts the thread on hold until it's time to terminate it at which point
         * the ClientHandler-object is removed from the ClientHandler-list, and the User-object associated with it is
         * removed from the User-list.
         */
        @Override
        public void run() {
            synchronized (lock) {
                while (!isInterrupted()) {
                    try {
                        System.out.println("ClientHandler" + clientHandlerId + ": Waiting.");
                        lock.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            synchronized (clientHandlers) {
                clientHandlers.removeIf(cH -> cH == this);
                System.out.println("ClientHandler" + clientHandlerId + ": RIP.");
            }
            synchronized (connectedUsers){
                connectedUsers.removeIf(cU -> cU == connectedUser);
            }
        }

        /**
         * Sends a response to the Client handled by the ClientHandler-object.
         * @param response
         */
        private void sendResponse(Object response){
            try {
                oos.writeObject(response);
                oos.flush();
                response = null;
                System.out.println("ClientHandler" + clientHandlerId + ": Sent response.");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Threads used to listen for messages from the Clients.
         */
        private class Listener extends Thread{
            private int listenerId;
            private ObjectInputStream ois;

            public Listener()throws IOException{
                numberOfListenersActive++;
                listenerId = numberOfListenersActive;
                ois = new ObjectInputStream(socket.getInputStream());
            }

            /**
             * Run-method for the Listener.
             */
            @Override
            public void run() {
                System.out.println("Listener" + listenerId + ": Started.");
                while (true) {
                    try {
                        synchronized (lock) {
                            message = ois.readObject();
                            if(message instanceof User) {
                                handleUserObject((User) message);
                            }
                            else if(message instanceof Message) {
                                handleMessageObject((Message) message);
                            }
                            System.out.println("Listener" + listenerId + ".");
                        }
                        sleep(100);
                    }catch(IOException | ClassNotFoundException | InterruptedException e){
                        ClientHandler.this.interrupt();
                        break;
                    }
                }
            }

            private void handleUserObject(User user){
                connectedUser = (User)message;
                synchronized (connectedUsers) {
                    connectedUsers.add(connectedUser);
                    for (ClientHandler clientHandler : clientHandlers) {
                        clientHandler.sendResponse(connectedUsers);
                    }
                }
            }

            /**
             * Handles Message-objects sent to the server. Adds the time received to the object.
             * @param message
             */
            private void handleMessageObject(Message message){
                synchronized (clientHandlers) {
                    for (ClientHandler clientHandler : clientHandlers) {
                        LocalDateTime localDateTime = LocalDateTime.now();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        message.setTimeAndDateReceivedByServer(dateTimeFormatter.format(localDateTime));
                        clientHandler.sendResponse(message);
                    }
                }
            }
        }
    }
}
