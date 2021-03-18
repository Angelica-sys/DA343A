package Ludvig.Server.server;

import java.util.LinkedList;

/**
 * Buffer is a class to store objects while they wait for processing
 * @version 1.0
 * @param <T> Any object, to be specified by the class using the buffer
 */
public class Buffer<T> {
    private LinkedList<T> buffer = new LinkedList<T>();

    /**
     * Adds an object to the end of the buffer
     * @param obj An object
     */
    public synchronized void put(T obj) {
        buffer.addLast(obj);
        notifyAll();
    }

    /**
     * get is a synchronized method that will retrieve the first object in the buffer if there is any. If not then
     * it will make sure that the the accessing thread waits until there is something to retrieve
     * @return Any object chosen by the class using the buffer
     * @throws InterruptedException Exception is thrown if the accessing thread is interrupted
     */
    public synchronized T get() throws InterruptedException {
        while(buffer.isEmpty()) {
            wait();
        }
        return buffer.removeFirst();
    }

    /**
     * size returns the size of the buffer
     * @return int
     */
    public int size() {
        return buffer.size();
    }
}
