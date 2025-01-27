package dk.jdsj.account.services;

import dk.jdsj.account.models.Currency;
import dk.jdsj.account.models.ExchangeRateApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    @Qualifier("exchangeRateRestTemplate")
    private final RestTemplate restTemplate;

    public Map<String, BigDecimal> getExchangeRate(final Currency fromCurrency, final Currency toCurrency, final BigDecimal amount) throws RuntimeException {
        log.info("Getting exchange rates");
        var url = "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;
        var response = restTemplate.getForObject(url, ExchangeRateApiResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to get exchange rate");
        }

        return Map.of(
                fromCurrency.name(), amount,
                toCurrency.name(), response.getConversion_result()
        );
    }
}
