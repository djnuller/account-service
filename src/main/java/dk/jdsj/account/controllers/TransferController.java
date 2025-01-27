package dk.jdsj.account.controllers;

import dk.jdsj.account.exceptions.InsufficientFundsException;
import dk.jdsj.account.exceptions.NotFoundException;
import dk.jdsj.account.models.TransferRequest;
import dk.jdsj.account.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@Slf4j
@RequiredArgsConstructor
public class TransferController {
    private final AccountService accountService;

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between accounts", responses = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request eg. insufficient funds"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public void transfer(final TransferRequest request) throws NotFoundException, InsufficientFundsException {
        log.info("Transferring {} from account {} to account {}", request.getAmount(), request.getFromAccount(), request.getToAccount());
        accountService.transferMoney(request);
        log.info("Transfer complete");
    }
}
