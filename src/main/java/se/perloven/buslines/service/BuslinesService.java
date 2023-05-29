package se.perloven.buslines.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import se.perloven.buslines.client.TrafiklabClient;
import se.perloven.buslines.model.Journey;
import se.perloven.buslines.model.Stop;

import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BuslinesService {
    private final static Logger log = LoggerFactory.getLogger(BuslinesService.class);
    private final TrafiklabClient client;
    private final AsyncTaskExecutor executor;

    public BuslinesService(TrafiklabClient client, AsyncTaskExecutor executor) {
        this.client = client;
        this.executor = executor;
    }

    public List<LineSummary> getTop10BusLines() throws Exception {
        Future<List<Journey>> journeysFuture = getJourneysAsync();
        Future<List<Stop>> stopsFuture = getStopsAsync();

        List<Journey> journeys = journeysFuture.get();
        log.debug("Retrieved {} journey stops", journeys.size());
        List<Stop> stops = stopsFuture.get();
        log.debug("Retrieved {} stops", stops.size());

        return calculateTop10(journeys, stops);
    }

    private Future<List<Journey>> getJourneysAsync() {
        return executor.submit(() -> client.getJourneyData()
                .map(response -> response.responseData().result())
                .orElse(Collections.emptyList())
        );
    }

    private Future<List<Stop>> getStopsAsync() {
        return executor.submit(() -> client.getStopData()
                .map(response -> response.responseData().result())
                .orElse(Collections.emptyList())
        );
    }

    private List<LineSummary> calculateTop10(List<Journey> allJourneys, List<Stop> allStops) {
        Map<Integer, Stop> busStopsByNumber = allStops.stream()
                .filter(Stop::isBusStop)
                .collect(Collectors.toMap(Stop::stopPointNumber, Function.identity()));
        Map<Integer, List<Journey>> journeysByLine = allJourneys.stream()
                .collect(Collectors.groupingBy(Journey::lineNumber));

        List<LineSummary> lineSummaries = new ArrayList<>();
        journeysByLine.forEach((lineNumber, journeys) -> {
            var stops = journeys.stream()
                    .map(journey -> busStopsByNumber.get(journey.journeyPatternPointNumber()))
                    .filter(Objects::nonNull)
                    .toList();
            lineSummaries.add(new LineSummary(lineNumber, stops));
        });

        return lineSummaries.stream()
                .sorted(Comparator.comparing(LineSummary::totalStops).reversed())
                .limit(10)
                .toList();
    }

}
