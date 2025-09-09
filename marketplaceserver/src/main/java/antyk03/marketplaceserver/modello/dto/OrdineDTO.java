package antyk03.marketplaceserver.modello.dto;

import antyk03.marketplaceserver.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdineDTO {

    private Long id;
    @NotBlank
    private EStatus status;
    @NotBlank
    private LocalDateTime dataCreazione;
    @NotBlank
    private String valuta;
    @NotBlank
    private BigDecimal totale;
    private List<ProdottoOrdineDTO> prodottiOrdineDTO = new ArrayList<>();

}
