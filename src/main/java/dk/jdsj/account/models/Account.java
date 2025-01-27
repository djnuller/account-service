package dk.jdsj.account.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String accountNumber;
    private BigDecimal balance;

    // owner object so we can have more owners for an account?
    private String firstName;
    private String lastName;
}
