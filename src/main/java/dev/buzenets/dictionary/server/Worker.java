package dev.buzenets.dictionary.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Worker implements Runnable {
    private final Socket clientSocket;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            final String currentThread = Thread.currentThread()
                .toString();
            output.write(String.format("HTTP/1.1 200 OK%n%nWorkerRunnable: %s", currentThread)
                .getBytes());
            output.close();
            input.close();
        } catch (IOException e) {
            //report exception somewhere
        }
    }
}
