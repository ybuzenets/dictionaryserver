package dev.buzenets.dictionary.server;

import dev.buzenets.dictionary.server.commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Worker implements Runnable {
    private final Socket clientSocket;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try (
            InputStream input = clientSocket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(input);
            OutputStream output = clientSocket.getOutputStream()
        ) {
            final String command = ois.readUTF();
            final List<String> args = ((List<String>) ois.readObject());
            Command.getByName(command)
                .execute(args, output);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
