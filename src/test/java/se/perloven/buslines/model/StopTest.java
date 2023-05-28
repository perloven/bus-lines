package se.perloven.buslines.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static se.perloven.buslines.TestUtils.someInt;
import static se.perloven.buslines.TestUtils.someString;

class StopTest {

    @Test
    void rejectNullStopPointNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Stop(null, someString(), someString()));
    }

    @Test
    void rejectNullStopPointName() {
        assertThrows(IllegalArgumentException.class, () -> new Stop(someInt(), null, someString()));
    }

    @Test
    void rejectNullStopAreaTypeCode() {
        assertThrows(IllegalArgumentException.class, () -> new Stop(someInt(), null, someString()));
    }

    @Test
    void createOk() {
        assertDoesNotThrow(() -> new Stop(someInt(), someString(), someString()));
    }
}
