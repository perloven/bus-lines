package se.perloven.buslines.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import se.perloven.buslines.client.TrafiklabClient;
import se.perloven.buslines.model.Journey;
import se.perloven.buslines.model.Response;

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
    public void getJourneys() {
        Optional<Response<Journey>> journeyData = client.getJourneyData();
        if (journeyData.isEmpty()) {
            return;
        }

        List<Journey> journeys = journeyData.get().responseData().result();
        log.info("Retrieved {} journey stops", journeys.size());
    }
}
