package se.perloven.buslines.service;

import se.perloven.buslines.model.Stop;

import java.util.List;

public record LineSummary(int lineNumber, List<Stop> stops) {

    public int totalStops() {
        return stops.size();
    }
}
