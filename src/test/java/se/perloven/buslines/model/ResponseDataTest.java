package se.perloven.buslines.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static se.perloven.buslines.TestUtils.someInt;

class ResponseDataTest {

    @Test
    void rejectNullResult() {
        assertThrows(IllegalArgumentException.class, () -> new ResponseData<Journey>(null));
    }

    @Test
    void createOk_emptyList() {
        assertDoesNotThrow(() -> new ResponseData<Journey>(Collections.emptyList()));
    }

    @Test
    void createOk() {
        var journey = new Journey(someInt(), someInt());

        assertDoesNotThrow(() -> new ResponseData<>(List.of(journey)));
    }
}
