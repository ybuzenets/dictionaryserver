package dev.buzenets.dictionary.server.commands;

import dev.buzenets.dictionary.server.model.Dictionary;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum Command {
    ADD {
        public void execute(List<String> args, OutputStream output) {
            args.stream()
                .findFirst()
                .ifPresent(key -> DICTIONARY.add(
                    key,
                    args.stream()
                        .skip(1)
                        .collect(Collectors.toList())
                ));
            try {
                output.write("Значения слова успешно добавлены".getBytes());
            } catch (IOException e) {
                LOG.severe(e::getMessage);
            }
        }
    }, DELETE {
        public void execute(List<String> args, OutputStream output) {
            args.stream()
                .findFirst()
                .map(key -> Dictionary.getInstance()
                    .delete(
                        key,
                        args.stream()
                            .skip(1)
                            .collect(Collectors.toList())
                    ))
                .filter(b -> b)
                .ifPresentOrElse(t -> {
                    try {
                        output.write("Значения слова успешно удалены".getBytes());
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                }, () -> {
                    try {
                        output.write("Cлово/значение отсутвует в словаре".getBytes());
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                });
        }
    }, GET {
        public void execute(List<String> args, OutputStream output) {
            args.stream()
                .findFirst()
                .map(key -> Dictionary.getInstance()
                    .get(key))
                .ifPresentOrElse(response -> response.forEach(word -> {
                    try {
                        output.write(word.getBytes());
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                }), () -> {
                    try {
                        output.write("Cлово отсутвует в словаре".getBytes());
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                });
        }
    };

    private static final Dictionary DICTIONARY = Dictionary.getInstance();
    private static final Logger LOG = Logger.getLogger(Command.class.getName());

    public static Command getByName(String name) {
        switch (name) {
            case "add":
                return ADD;
            case "delete":
                return DELETE;
            case "get":
                return GET;
            default:
                throw new IllegalArgumentException("Unknown command " + name);
        }
    }

    public void execute(List<String> args, OutputStream output) {
        //default implementation
    }
}
