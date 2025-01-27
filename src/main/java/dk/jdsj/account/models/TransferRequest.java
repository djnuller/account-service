package dk.jdsj.account.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "TransferRequest", description = "Request for transfer between accounts")
public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}
