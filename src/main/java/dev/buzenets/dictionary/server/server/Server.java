package dev.buzenets.dictionary.server.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 */
//TODO: Use Tomcat/Jetty/ with Spring Web/Webflux (JAX-RS) in real world
public class Server implements Runnable {
    private final String address;
    private final int serverPort;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public Server(String address, int port) {
        this.serverPort = port;
        this.address = address;
    }

    public void run() {
        openServerSocket(address);
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

    private void openServerSocket(String host) {
        try {
            this.serverSocket = new ServerSocket(serverPort, 50, InetAddress.getByName(host));
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }
}
