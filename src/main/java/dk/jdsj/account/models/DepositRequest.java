package dk.jdsj.account.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "DepositRequest", description = "Request to deposit money into an account")
public class DepositRequest {

    @Schema(description = "Account number", example = "1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed")
    @NotBlank
    private String accountNumber;

    @Schema(description = "Amount to deposit", example = "100.00")
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    @NegativeOrZero(message = "Amount must be positive")
    private BigDecimal amount;

    private DepositTypes depositType;
}
