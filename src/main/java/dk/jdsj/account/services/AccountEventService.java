package dk.jdsj.account.services;

import dk.jdsj.account.entities.AccountEntity;
import dk.jdsj.account.entities.AccountEventEntity;
import dk.jdsj.account.exceptions.NotFoundException;
import dk.jdsj.account.models.AccountEventType;
import dk.jdsj.account.models.DepositTypes;
import dk.jdsj.account.models.TransferType;
import dk.jdsj.account.repositories.AccountEventRepository;
import dk.jdsj.account.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEventService {
    private final AccountEventRepository accountEventRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void logTransferEvent(final AccountEntity toAccount, final AccountEntity fromAccount, final BigDecimal amount) {
        log.info("AccountEvent amount {} from account {} to account {}", amount, fromAccount.getAccountNumber(), toAccount.getAccountNumber());

        var toAccountEvent = AccountEventEntity.builder()
                .account(toAccount)
                .amount(amount)
                .oldBalance(toAccount.getBalance())
                .newBalance(toAccount.getBalance().add(amount))
                .accountEventType(AccountEventType.TRANSFER)
                .transferType(TransferType.DEBIT)
                .referenceKey("fromAccount")
                .referenceValue(fromAccount.getAccountNumber())
                .build();

        var fromAccountEvent = AccountEventEntity.builder()
                .account(fromAccount)
                .amount(amount)
                .oldBalance(fromAccount.getBalance())
                .newBalance(fromAccount.getBalance().subtract(amount))
                .accountEventType(AccountEventType.TRANSFER)
                .transferType(TransferType.CREDIT)
                .referenceKey("toAccount")
                .referenceValue(toAccount.getAccountNumber())
                .build();

        accountEventRepository.save(toAccountEvent);
        accountEventRepository.save(fromAccountEvent);
    }

    @Transactional
    public void logDepositEvent(final AccountEntity account, final BigDecimal amount, final DepositTypes depositType) {
        log.info("AccountEvent amount {} deposited to account {}", amount, account.getAccountNumber());

        var accountEvent = AccountEventEntity.builder()
                .account(account)
                .amount(amount)
                .oldBalance(account.getBalance())
                .newBalance(account.getBalance().add(amount))
                .accountEventType(AccountEventType.DEPOSIT)
                .transferType(TransferType.DEBIT)
                .referenceKey("deposit")
                .referenceValue(depositType.name())
                .build();

        accountEventRepository.save(accountEvent);
    }

    public List<AccountEventEntity> getAccountEvents(final String accountNumber) throws NotFoundException {
        log.info("Getting account events for account number: {}", accountNumber);

        var account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account {" + accountNumber + "} not found");
        }

        return accountEventRepository.findAllByAccountNumber(accountNumber);
    }

}
