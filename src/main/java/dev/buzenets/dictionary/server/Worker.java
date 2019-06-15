package dev.buzenets.dictionary.server;

import dev.buzenets.dictionary.server.commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class Worker implements Runnable {
    private final Logger log = Logger.getLogger(Worker.class.getName() +Thread.currentThread().getName());
    private final Socket clientSocket;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try (
            InputStream input = clientSocket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(input);
            OutputStream os = clientSocket.getOutputStream()
        ) {
            final String encoding = ois.readUTF();
            log.fine("Target encoding: " + encoding);

            final String command = ois.readUTF();
            log.fine("Command: " + command);
            final List<String> args = ((List<String>) ois.readObject());
            execute(os, encoding, command, args);
            log.fine("Finished");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private void execute(
        OutputStream os, String encoding, String command, List<String> args
    ) throws IOException {
        try (
            OutputStreamWriter output = new OutputStreamWriter(os, encoding)
        ) {
            Command.getByName(command)
                .execute(args, output);
        }
    }
}
