package antyk03.marketplaceserver.modello.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProdottoDTO {

    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String descrizione;
    @DecimalMin(value = "0.01")
    private float prezzo;
    @Min(1)
    private int quantita;

}
