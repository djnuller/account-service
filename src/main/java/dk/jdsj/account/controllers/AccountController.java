package dk.jdsj.account.controllers;

import dk.jdsj.account.exceptions.NotFoundException;
import dk.jdsj.account.models.Account;
import dk.jdsj.account.models.CreateAccountRequest;
import dk.jdsj.account.models.DepositRequest;
import dk.jdsj.account.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    @Operation(summary = "Create and account", responses = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request schema is not valid"),
    })
    public Account createAccount(@RequestBody final CreateAccountRequest request) {
        log.info("Creating new account with first name: {}, last name: {}", request.getFirstName(), request.getLastName());
        var account = accountService.createAccount(request);
        log.info("Account created with account number: {}", account.getAccountNumber());
        return account;
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Transfer money between accounts", responses = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public Account getAccountBalance(@PathVariable final String accountNumber) throws NotFoundException {
        log.info("Getting account balance for account number: {}", accountNumber);
        return accountService.getAccountBalance(accountNumber);
    }

    @GetMapping("/get-all")
    public List<Account> getAllAccounts() {
        log.info("Getting all accounts");
        return accountService.getAllAccounts();
    }

    @PostMapping("/deposit")
    @Operation(summary = "Transfer money between accounts", responses = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Schema is not valid"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public Account deposit(final DepositRequest request) throws NotFoundException {
        log.info("Depositing {} to account {}", request.getAmount(), request.getAccountNumber());
        return accountService.depositMoney(request);
    }
}
