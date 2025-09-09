package antyk03.marketplaceserver.modello;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProdottoOrdine {

    private Long id;
    private Long idProdotto;
    private int quantita;
    private BigDecimal prezzoUnitario;
    private BigDecimal totale;

    public ProdottoOrdine (Long idProdotto, int quantita, BigDecimal prezzoUnitario) {
        this.idProdotto = idProdotto;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
        this.totale = prezzoUnitario.multiply(BigDecimal.valueOf(quantita));
    }

    public void calcolaTotale () {
        this.totale = prezzoUnitario.multiply(BigDecimal.valueOf(quantita));
    }
}
