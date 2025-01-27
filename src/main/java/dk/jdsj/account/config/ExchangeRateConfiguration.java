package dk.jdsj.account.config;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ExchangeRateConfiguration {

    @Bean(name = "exchangeRateRestTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder, ExchangeRateApiProperties apiProperties) {
        return builder
                .rootUri(apiProperties.getFullUrl())
                .build();
    }
}
