package dev.buzenets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 */
//TODO: Use Tomcat/Jetty/ with Spring Web/Webflux (JAX-RS) in real world
public class Server implements Runnable {
    private final int serverPort;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public Server(int port) {
        this.serverPort = port;
    }

    public void run() {
        openServerSocket();
        while (!isStopped()) {
            try {
                final Socket clientSocket = serverSocket.accept();
                new Thread(new Worker(clientSocket)).start();
            } catch (IOException e) {
                if (isStopped()) {
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
        }
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
}
