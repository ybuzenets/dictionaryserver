package dev.buzenets.dictionary.server.model;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryTest {

    @Test
    void dictionaryShouldBeSingleton() {
        final Dictionary firstInstance = Dictionary.getInstance();
        final Dictionary secondInstance = Dictionary.getInstance();
        assertThat(firstInstance).isSameAs(secondInstance);
        assertThat(firstInstance).isNotNull();
    }

    @Test
    void shouldInitializeEmpty() {
        final Dictionary dictionary = new Dictionary();
        final Collection<String> afterInit = dictionary.get("unknown");
        assertThat(afterInit).isNull();
    }

    @Test
    void addAndGetShouldBeConsistent() {
        final Dictionary dictionary = new Dictionary();
        final Set<String> values = Collections.singleton("somevalue");
        final String key = "someword";
        dictionary.add(key, values);
        final Collection<String> result = dictionary.get(key);
        assertThat(result).containsOnlyElementsOf(values);
    }

    @Test
    void getAndDeleteShouldBeConsistent() {
        final Dictionary dictionary = new Dictionary();
        final Set<String> values = Collections.singleton("somevalue");
        final String key = "someword";
        dictionary.add(key, values);
        dictionary.delete(key, values);
        final Collection<String> result = dictionary.get(key);
        assertThat(result).isNull();
    }

    @Test
    void addShouldNotReplaceOldValues() {
        final Dictionary dictionary = new Dictionary();
        final Set<String> oldValue = Collections.singleton("oldvalue");
        final Set<String> newValue = Collections.singleton("newValue");
        final String key = "someword";
        dictionary.add(key, oldValue);
        dictionary.add(key, newValue);
        final Collection<String> result = dictionary.get(key);
        assertThat(result).containsAll(oldValue)
            .containsAll(newValue);
    }

    @Test
    void deleteShouldReturnTrueIfValueDeleted() {
        final Dictionary dictionary = new Dictionary();
        final Set<String> values = Collections.singleton("somevalue");
        final String key = "someword";
        dictionary.add(key, values);
        final boolean wasDeleted = dictionary.delete(key, values);
        assertThat(wasDeleted).isTrue();
    }

    @Test
    void deleteShouldReturnFalseIfNoValueDeleted() {
        final Dictionary dictionary = new Dictionary();
        final Set<String> values = Collections.singleton("somevalue");
        final String key = "someword";
        final boolean wasDeleted = dictionary.delete(key, values);
        assertThat(wasDeleted).isFalse();
    }
}