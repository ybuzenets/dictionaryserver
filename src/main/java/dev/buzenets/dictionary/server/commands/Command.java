package dev.buzenets.dictionary.server.commands;

import dev.buzenets.dictionary.server.model.Dictionary;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum Command {
    ADD {
        @Override
        public void execute(List<String> args, OutputStreamWriter output) {
            args.stream()
                .findFirst()
                .ifPresent(key -> DICTIONARY.add(
                    key,
                    args.stream()
                        .skip(1)
                        .collect(Collectors.toList())
                ));
            try {
                output.write("Значения слова успешно добавлены");
            } catch (IOException e) {
                LOG.severe(e::getMessage);
            }
        }
    }, DELETE {
        @Override
        public void execute(List<String> args, OutputStreamWriter output) {
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
                        output.write("Значения слова успешно удалены");
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                }, () -> {
                    try {
                        output.write("Слово/значение отсутвует в словаре");
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                });
        }
    }, GET {
        @Override
        public void execute(List<String> args, OutputStreamWriter output) {
            args.stream()
                .findFirst()
                .map(key -> Dictionary.getInstance()
                    .get(key))
                .ifPresentOrElse(response -> response.forEach(word -> {
                    try {
                        output.write(word);
                    } catch (IOException e) {
                        LOG.severe(e::getMessage);
                    }
                }), () -> {
                    try {
                        output.write("Слово отсутвует в словаре");
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

    public void execute(List<String> args, OutputStreamWriter output) {
        //default implementation
    }
}
