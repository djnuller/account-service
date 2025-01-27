package dk.jdsj.account.controllers;

import dk.jdsj.account.entities.AccountEventEntity;
import dk.jdsj.account.exceptions.NotFoundException;
import dk.jdsj.account.services.AccountEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account-event")
@Slf4j
@RequiredArgsConstructor
public class AccountEventController {

    private final AccountEventService accountEventService;

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get account events", description = "Get all events for a given account number", responses = {
            @ApiResponse(responseCode = "200", description = "Account events found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public List<AccountEventEntity> getAccountEvents(@PathVariable final String accountNumber) throws NotFoundException {
        log.info("Getting account events for account number: {}", accountNumber);
        return accountEventService.getAccountEvents(accountNumber);
    }
}
