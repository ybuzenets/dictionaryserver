package dev.buzenets.dictionary.server.model;

import dev.buzenets.dictionary.server.functions.RemoveValuesFunction;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Dictionary {
    private final ConcurrentMap<String, Set<String>> dict = new ConcurrentHashMap<>();

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
}
