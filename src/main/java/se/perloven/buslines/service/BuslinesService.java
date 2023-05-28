package se.perloven.buslines.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import se.perloven.buslines.client.TrafiklabClient;
import se.perloven.buslines.model.Journey;
import se.perloven.buslines.model.Response;
import se.perloven.buslines.model.Stop;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BuslinesService {
    private final static Logger log = LoggerFactory.getLogger(BuslinesService.class);
    private final TrafiklabClient client;

    public BuslinesService(TrafiklabClient client) {
        this.client = client;
    }

    private record LineSummary(int lineNumber, List<Stop> stops) {

        public int totalStops() {
            return stops.size();
        }

        @Override
        public String toString() {
            String stopInfo = stops.stream()
                    .map(stop -> "%s (%d)".formatted(stop.stopPointName(), stop.stopPointNumber()))
                    .collect(Collectors.joining(", ", "[", "]"));
            return "Line %d with %d stops: %s".formatted(lineNumber, totalStops(), stopInfo);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void processBusLines() {
        List<Journey> journeys = getJourneys();
        log.debug("Retrieved {} journey stops", journeys.size());

        List<Stop> stops = getStops();
        log.debug("Retrieved {} stops", stops.size());

        List<LineSummary> top10 = calculateTop10(journeys, stops);
        System.out.println("===== Top 10 bus lines (by stop count) =====");
        top10.forEach(System.out::println);
    }

    private List<Journey> getJourneys() {
        Optional<Response<Journey>> journeyData = client.getJourneyData();
        if (journeyData.isEmpty()) {
            return Collections.emptyList();
        }

        return journeyData.get().responseData().result();
    }

    private List<Stop> getStops() {
        Optional<Response<Stop>> stopData = client.getStopData();
        if (stopData.isEmpty()) {
            return Collections.emptyList();
        }

        return stopData.get().responseData().result();
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
