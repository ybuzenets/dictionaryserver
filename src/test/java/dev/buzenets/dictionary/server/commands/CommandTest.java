package dev.buzenets.dictionary.server.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandTest {

    @Test
    void shouldReturnCorrectCommandByName() {
        assertEquals( Command.ADD, Command.getByName("add"));
        assertEquals(Command.GET, Command.getByName("get"));
        assertEquals(Command.DELETE, Command.getByName("delete"));
    }

    @Test
    void shouldThrowExceptionOnUnknownCommand() {
        assertThrows(IllegalArgumentException.class, () -> Command.getByName("unknown"));
    }
}