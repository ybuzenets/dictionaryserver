package dev.buzenets.dictionary.server.functions;

import java.util.Set;
import java.util.function.BinaryOperator;

public class RemoveValuesFunction implements BinaryOperator<Set<String>> {
    private boolean modified;

    @Override
    public Set<String> apply(Set<String> oldValue, Set<String> newValue) {
        modified = oldValue.removeAll(newValue);
        return oldValue.isEmpty() ? null : oldValue;
    }

    public boolean isModified() {
        return modified;
    }
}
