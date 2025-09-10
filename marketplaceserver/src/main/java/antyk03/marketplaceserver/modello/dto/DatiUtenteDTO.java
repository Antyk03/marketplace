package antyk03.marketplaceserver.modello.dto;

import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.enums.EStatoUtente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DatiUtenteDTO {

    @NotBlank
    private Long id;
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private ERuolo ruolo;
    private EStatoUtente statoUtente;
    @NotBlank
    private Long idUtente;


}
