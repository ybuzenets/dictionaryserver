package dev.buzenets.dictionary.server;

import com.beust.jcommander.JCommander;
import dev.buzenets.dictionary.server.args.Args;

public class Main {
    public static void main(String[] argv) {
        final Args args = new Args();
        JCommander.newBuilder()
            .addObject(args)
            .build()
            .parse(argv);
        new Server(args.getAddress(), args.getPort()).run();
    }
}
