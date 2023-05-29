package se.perloven.buslines.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import se.perloven.buslines.model.Journey;
import se.perloven.buslines.model.Response;
import se.perloven.buslines.model.ResponseDataType;
import se.perloven.buslines.model.Stop;

import java.util.Optional;

@Component
public class TrafiklabClient {
    private final static Logger log = LoggerFactory.getLogger(TrafiklabClient.class);
    private final static int OK_RESPONSE_CODE = 0;

    private final WebClient webClient;
    private final String apiKey;

    public TrafiklabClient(WebClient webClient, @Value("${trafiklab.api-key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    public Optional<Response<Journey>> getJourneyData() {
        return fetchData("jour", new ParameterizedTypeReference<>() {});
    }

    public Optional<Response<Stop>> getStopData() {
        return fetchData("stop", new ParameterizedTypeReference<>() {});
    }

    private <T extends ResponseDataType> Optional<Response<T>> fetchData(String model,
                                                                         ParameterizedTypeReference<Response<T>> type) {
        final Response<T> response;
        try {
            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("model", model)
                            .queryParam("key", this.apiKey)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(type)
                    .block();
        } catch (WebClientException e) {
            log.error("Trafiklab client error ()", e);
            return Optional.empty();
        }

        if (response == null || response.statusCode() != OK_RESPONSE_CODE) {
            log.warn("Retrieved bad response from Trafiklab: {}", response);
            return Optional.empty();
        } else {
            return Optional.of(response);
        }
    }
}
