package antyk03.marketplaceserver.modello.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UtenteDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
