package dk.jdsj.account.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateAccountRequest", description = "Request for creating an account, account number is generated automatically")
public class CreateAccountRequest {

    @Schema(description = "First name of the account holder", example = "John")
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the account holder", example = "Doe")
    @NotBlank
    private String lastName;
}
