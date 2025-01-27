package dk.jdsj.account.entities;

import dk.jdsj.account.models.AccountEventType;
import dk.jdsj.account.models.TransferType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "account_event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity account;
    private BigDecimal amount;

    private BigDecimal oldBalance;
    private BigDecimal newBalance;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Enumerated(EnumType.STRING)
    private AccountEventType accountEventType;

    private String referenceKey;
    private String referenceValue;
}
