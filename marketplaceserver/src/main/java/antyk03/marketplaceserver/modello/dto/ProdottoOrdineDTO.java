package antyk03.marketplaceserver.modello.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdottoOrdineDTO {

    private Long id;
    @NotBlank
    private Long idProdotto;
    @NotBlank
    private int quantita;
    @NotBlank
    private BigDecimal prezzoUnitario;
    private BigDecimal totale;

}
