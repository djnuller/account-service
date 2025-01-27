package dk.jdsj.account.controllers;

import dk.jdsj.account.models.Currency;
import dk.jdsj.account.services.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange-rate")
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/exchange-rate")
    @Operation(summary = "Get exchange rate", description = "Get exchange rate between two currencies")
    public Map<String, BigDecimal> getExchangeRate(@RequestParam Currency fromCurrency,
                                                   @RequestParam Currency toCurrency,
                                                   @RequestParam BigDecimal amount) {
        log.info("Getting exchange rates");
        return exchangeRateService.getExchangeRate(fromCurrency, toCurrency, amount);
    }
}
