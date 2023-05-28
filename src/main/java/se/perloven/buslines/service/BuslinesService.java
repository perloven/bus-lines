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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BuslinesService {
    private final static Logger log = LoggerFactory.getLogger(BuslinesService.class);
    private final TrafiklabClient client;

    public BuslinesService(TrafiklabClient client) {
        this.client = client;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void processBusLines() {
        List<Journey> journeys = getJourneys();
        log.info("Retrieved {} journey stops", journeys.size());

        List<Stop> stops = getStops();
        log.info("Retrieved {} stops", stops.size());
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
}
