package se.perloven.buslines.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import se.perloven.buslines.model.Stop;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {
    private final BuslinesService buslinesService;

    public ResultService(BuslinesService buslinesService) {
        this.buslinesService = buslinesService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printBusLines() throws Exception {
        List<LineSummary> top10 = buslinesService.getTop10BusLines();
        if (top10.isEmpty()) {
            return;
        }

        printTop10(top10);
        printTop1(top10.get(0));
    }

    private void printTop10(List<LineSummary> top10Lines) {
        System.out.println("===== Top 10 bus lines (by stop count) =====");
        top10Lines.forEach(line ->
                System.out.println("- Line %d with %d stops".formatted(line.lineNumber(), line.totalStops()))
        );
    }

    private void printTop1(LineSummary line) {
        String stopNames = line.stops().stream()
                .map(Stop::stopPointName)
                .collect(Collectors.joining(", "));
        System.out.println();
        System.out.println("Line %d is in the lead with %d stops.".formatted(line.lineNumber(), line.totalStops()));
        System.out.println("Here are their names:" + System.lineSeparator() + stopNames);
    }
}
