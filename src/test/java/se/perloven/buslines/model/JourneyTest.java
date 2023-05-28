package se.perloven.buslines.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.perloven.buslines.TestUtils.someInt;

class JourneyTest {

    @Test
    void rejectNullLineNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Journey(null, someInt()));
    }

    @Test
    void rejectNullPointNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Journey(someInt(), null));
    }

    @Test
    void createOk() {
        assertDoesNotThrow(() -> new Journey(someInt(), someInt()));
    }
}
