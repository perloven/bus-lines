package se.perloven.buslines.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.task.AsyncTaskExecutor;
import se.perloven.buslines.client.TrafiklabClient;
import se.perloven.buslines.model.Journey;
import se.perloven.buslines.model.Stop;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static se.perloven.buslines.TestUtils.someString;

class BuslinesServiceTest {
    @Mock
    private AsyncTaskExecutor executor;

    @Mock
    private TrafiklabClient client;

    private BuslinesService service;

    @BeforeEach
    void reset() {
        openMocks(this);
        this.service = new BuslinesService(client, executor);
    }

    private void givenResponseData(List<Journey> journeys, List<Stop> stops) {
        when(executor.submit(any(Callable.class)))
                .thenReturn(completedFuture(journeys))
                .thenReturn(completedFuture(stops));
    }

    @Test
    void noResponseData() {
        givenResponseData(Collections.emptyList(), Collections.emptyList());

        var top10 = service.getTop10BusLines();

        assertTrue(top10.isEmpty());
    }

    @Test
    void oneLineWithMoreStops() {
        // One line with 2 stops, 9 lines with 1 stop
        List<Journey> journeys = List.of(
                new Journey(1, 11),
                new Journey(1, 12),
                new Journey(2, 21),
                new Journey(3, 31),
                new Journey(4, 41),
                new Journey(5, 51),
                new Journey(6, 61),
                new Journey(7, 71),
                new Journey(8, 81),
                new Journey(9, 91),
                new Journey(10, 101)
        );
        List<Stop> stops = List.of(
                busStop(11, "First stop"), busStop(12, "Last stop"),
                busStop(21),
                busStop(31),
                busStop(41),
                busStop(51),
                busStop(61),
                busStop(71),
                busStop(81),
                busStop(91),
                busStop(101)
        );
        givenResponseData(journeys, stops);

        var top10 = service.getTop10BusLines();

        assertEquals(10, top10.size());
        var topJourney = top10.get(0);
        assertEquals(1, topJourney.lineNumber());
        assertEquals(2, topJourney.totalStops());
        assertEquals(
                List.of(busStop(11, "First stop"), busStop(12, "Last stop")),
                topJourney.stops()
        );
    }

    private Stop busStop(int number) {
        return busStop(number, someString());
    }

    private Stop busStop(int number, String name) {
        return new Stop(number, name, "BUSTERM");
    }

    @Test
    void filterOutNonBusStops() {
        List<Journey> journeys = List.of(
                new Journey(1, 11),
                new Journey(1, 12),
                new Journey(1, 13),
                new Journey(2, 21),
                new Journey(2, 22)
        );
        List<Stop> stops = List.of(
                nonBusStop(11), nonBusStop(12), nonBusStop(13),
                busStop(21), busStop(22)
        );
        givenResponseData(journeys, stops);

        var top10 = service.getTop10BusLines();

        assertEquals(2, top10.size());
        var topJourney = top10.get(0);
        assertEquals(2, topJourney.lineNumber());
        assertEquals(2, topJourney.stops().size());
        assertEquals(2, topJourney.totalStops());
        var lastJourney = top10.get(1);
        assertEquals(1, lastJourney.lineNumber());
        assertEquals(0, lastJourney.stops().size());
        assertEquals(0, lastJourney.totalStops());
    }

    private Stop nonBusStop(int number) {
        return new Stop(number, someString(), "METROSTN");
    }
}
