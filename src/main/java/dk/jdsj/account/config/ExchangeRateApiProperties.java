package dk.jdsj.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "exchange-api")
public class ExchangeRateApiProperties {

    private String apiKey;
    private String version;
    private String baseUrl;

    public String getFullUrl() {
        return String.format("%s/%s/%s/", baseUrl, version, apiKey);
    }
}
