package dk.jdsj.account.services;

import dk.jdsj.account.EnableTestcontainers;
import dk.jdsj.account.models.*;
import dk.jdsj.account.repositories.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTestcontainers
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountEventService accountEventService;

    @Test
    @DisplayName("Create account - success")
    void createAccount() {
        var accountRequest = CreateAccountRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        var account = accountService.createAccount(accountRequest);

        assertThat(account).isNotNull();
        assertThat(account.getAccountNumber()).isNotBlank();
        assertThat(account.getFirstName()).isEqualTo("John");
        assertThat(account.getLastName()).isEqualTo("Doe");
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);

        var accountEntity = accountRepository.findByAccountNumber(account.getAccountNumber());
        assertThat(accountEntity).isNotNull();
        assertThat(accountEntity.getAccountNumber()).isEqualTo(account.getAccountNumber());
        assertThat(accountEntity.getFirstName()).isEqualTo("John");
        assertThat(accountEntity.getLastName()).isEqualTo("Doe");
        assertThat(accountEntity.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(accountEntity.getId()).isNotNull();
    }

    @Test
    @DisplayName("Get account balance - success")
    void getAccountBalance() {
        var accountRequest = CreateAccountRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        var account = accountService.createAccount(accountRequest);

        assertThat(account).isNotNull();

        var accountBalance = accountService.getAccountBalance(account.getAccountNumber());
        assertThat(accountBalance).isNotNull();
        assertThat(accountBalance.getAccountNumber()).isEqualTo(account.getAccountNumber());
        assertThat(accountBalance.getBalance()).isEqualByComparingTo(account.getBalance());
    }

    @Test
    @DisplayName("Transfer money - success")
    void transferMoney() {
        var toAccountRequest = CreateAccountRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        var toAccount = accountService.createAccount(toAccountRequest);

        assertThat(toAccount).isNotNull();


        var fromAccountRequest = CreateAccountRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        var fromAccount = accountService.createAccount(fromAccountRequest);

        assertThat(fromAccount).isNotNull();
        assertThat(fromAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);

        accountService.depositMoney(DepositRequest.builder()
                .accountNumber(fromAccount.getAccountNumber())
                .amount(BigDecimal.valueOf(100))
                .depositType(DepositTypes.CASH)
                .build());

        assertThat(accountService.getAccountBalance(fromAccount.getAccountNumber()).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));

        accountService.transferMoney(TransferRequest.builder()
                .fromAccount(fromAccount.getAccountNumber())
                .toAccount(toAccount.getAccountNumber())
                .amount(BigDecimal.valueOf(50))
                .build());

        assertThat(accountService.getAccountBalance(fromAccount.getAccountNumber()).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(accountService.getAccountBalance(toAccount.getAccountNumber()).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(50));
    }

    @Test
    @DisplayName("Deposit money - success")
    void depositMoney() {
        var accountRequest = CreateAccountRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        var account = accountService.createAccount(accountRequest);

        assertThat(account).isNotNull();
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);

        accountService.depositMoney(DepositRequest.builder()
                .accountNumber(account.getAccountNumber())
                .amount(BigDecimal.valueOf(100))
                .depositType(DepositTypes.CASH)
                .build());

        assertThat(accountService.getAccountBalance(account.getAccountNumber()).getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));

        var accountEvents = accountEventService.getAccountEvents(account.getAccountNumber());
        assertThat(accountEvents).isNotNull();
        assertThat(accountEvents).hasSize(1);
        assertThat(accountEvents.getFirst().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(accountEvents.getFirst().getOldBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(accountEvents.getFirst().getNewBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(accountEvents.getFirst().getAccountEventType()).isEqualByComparingTo(AccountEventType.DEPOSIT);
        assertThat(accountEvents.getFirst().getTransferType()).isEqualByComparingTo(TransferType.DEBIT);
        assertThat(accountEvents.getFirst().getReferenceKey()).isEqualTo("deposit");
        assertThat(accountEvents.getFirst().getReferenceValue()).isEqualTo(DepositTypes.CASH.name());
    }

    @Test
    @DisplayName("Get all accounts - success")
    void getAllAccounts() {
        var accountRequest = CreateAccountRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        accountService.createAccount(accountRequest);
        accountService.createAccount(accountRequest);
        accountService.createAccount(accountRequest);

        var accounts = accountService.getAllAccounts();
        assertThat(accounts).isNotNull();
        assertThat(accounts).hasSizeGreaterThan(2);
    }
}