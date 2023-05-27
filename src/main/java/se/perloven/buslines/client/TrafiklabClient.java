package se.perloven.buslines.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TrafiklabClient {
    private final WebClient webClient;

    public TrafiklabClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
