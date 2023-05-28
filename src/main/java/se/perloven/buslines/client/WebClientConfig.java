package se.perloven.buslines.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder, @Value("${trafiklab.base-url}") String baseUrl) {
        var httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(15));
        return builder.clone()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(codecConfigurer -> codecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize((int) DataSize.ofMegabytes(10).toBytes())
                )
                .build();
    }
}
