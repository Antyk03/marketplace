package antyk03.marketplaceserver.modello;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "prodotti_ordine")
@Data
@NoArgsConstructor
public class ProdottoOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idProdotto;
    @Setter
    private Long idOrdine;
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
