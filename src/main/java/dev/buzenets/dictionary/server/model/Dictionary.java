package dev.buzenets.dictionary.server.model;

import dev.buzenets.dictionary.server.functions.RemoveValuesFunction;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Dictionary {
    private static Dictionary INSTANCE;
    private final ConcurrentMap<String, Set<String>> dict = new ConcurrentHashMap<>();

    private Dictionary() {
    }

    public void add(String key, Collection<String> words) {
        dict.merge(key, new ConcurrentSkipListSet<>(words), (old, newValue) -> {
            old.addAll(newValue);
            return old;
        });
    }

    public boolean delete(String key, Collection<String> words) {
        final RemoveValuesFunction remappingFunction = new RemoveValuesFunction();
        dict.merge(key, new ConcurrentSkipListSet<>(words), remappingFunction);
        return remappingFunction.isModified();
    }

    public Collection<String> get(String key) {
        return dict.get(key);
    }

    public static Dictionary getInstance() {
        return Optional.ofNullable(INSTANCE)
            .orElseGet(() -> {
                INSTANCE = new Dictionary();
                return INSTANCE;
            });
    }
}
