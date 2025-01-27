package dk.jdsj.account.services;

import dk.jdsj.account.entities.AccountEntity;
import dk.jdsj.account.exceptions.InsufficientFundsException;
import dk.jdsj.account.exceptions.NotFoundException;
import dk.jdsj.account.models.*;
import dk.jdsj.account.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountEventService accountEventService;

    public Account createAccount(final CreateAccountRequest request) {
        log.info("Creating new account");
        var newAccount = AccountEntity.builder()
                .accountNumber(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .balance(BigDecimal.ZERO)
                .build();

        var accountEntity = accountRepository.save(newAccount);
        log.info("Account created with account number: {}", accountEntity.getAccountNumber());

        return Account.builder()
                .accountNumber(accountEntity.getAccountNumber())
                .balance(accountEntity.getBalance())
                .firstName(accountEntity.getFirstName())
                .lastName(accountEntity.getLastName())
                .build();
    }

    public Account getAccountBalance(final String accountNumber) throws NotFoundException {
        log.info("Getting account balance for account number: {}", accountNumber);
        var accountEntity = accountRepository.findByAccountNumber(accountNumber);

        if (accountEntity == null) {
            throw new NotFoundException("Account {" + accountNumber + "} not found");
        }

        return Account.builder()
                .accountNumber(accountEntity.getAccountNumber())
                .balance(accountEntity.getBalance())
                .build();
    }

    @Transactional
    public void transferMoney(final TransferRequest request) throws NotFoundException, InsufficientFundsException {
        log.info("Transferring {} from account {} to account {}", request.getAmount(), request.getFromAccount(), request.getToAccount());

        var fromAccount = accountRepository.findByAccountNumber(request.getFromAccount());

        if (fromAccount == null) {
            throw new NotFoundException("fromAccount {" + request.getFromAccount() + "} not found");
        }

        var toAccount = accountRepository.findByAccountNumber(request.getToAccount());

        if (toAccount == null) {
            throw new NotFoundException("toAccount {" + request.getToAccount() + "} not found");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountEventService.logTransferEvent(toAccount, fromAccount, request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Transactional
    public Account depositMoney(final DepositRequest request) throws NotFoundException {
        log.info("Depositing {} to account {} using deposit type {}", request.getAmount(), request.getAccountNumber(), request.getDepositType().name());
        var account = accountRepository.findByAccountNumber(request.getAccountNumber());

        if (account == null) {
            throw new NotFoundException("Account {" + request.getAccountNumber() + "} not found");
        }

        accountEventService.logDepositEvent(account, request.getAmount(), request.getDepositType());

        account.setBalance(account.getBalance().add(request.getAmount()));

        var updatedAccount = accountRepository.save(account);

        return Account.builder()
                .accountNumber(updatedAccount.getAccountNumber())
                .balance(updatedAccount.getBalance())
                .build();
    }

    public List<Account> getAllAccounts() {
        var accountEntities = accountRepository.findAll();

        return accountEntities.stream()
                .map(accountEntity -> Account.builder()
                        .accountNumber(accountEntity.getAccountNumber())
                        .balance(accountEntity.getBalance())
                        .build())
                .toList();
    }
}
