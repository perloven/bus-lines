package se.perloven.buslines.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder, @Value("${trafiklab.base-url}") String baseUrl) {
        return builder.clone()
                .baseUrl(baseUrl)
                .codecs(codecConfigurer -> codecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize((int) DataSize.ofMegabytes(10).toBytes())
                )
                .build();
    }
}
