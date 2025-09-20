package antyk03.marketplaceserver.modello;

import antyk03.marketplaceserver.enums.EStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "ordini")
@Data
@NoArgsConstructor
@Slf4j
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUtente;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    private LocalDateTime dataCreazione;
    private String valuta;
    private BigDecimal totale;
    @Transient
    private List<ProdottoOrdine> prodotti = new ArrayList<>();

    public Ordine (Long idUtente, EStatus status, LocalDateTime dataCreazione, String valuta) {
        this.idUtente = idUtente;
        this.status = status;
        this.dataCreazione=dataCreazione;
        this.valuta = valuta;
    }

    public void calcolaTotale() {
        double totale = 0;
        for (ProdottoOrdine po: prodotti) {
            totale +=  po.getTotale().doubleValue();
            log.info(String.valueOf(totale));
        }
        this.totale = BigDecimal.valueOf(totale);
    }

    public ProdottoOrdine cercaProdottoOrdine(Long idProdotto) {
        for(ProdottoOrdine po: prodotti) {
            if (po.getIdProdotto() == idProdotto) {
                return po;
            }
        }
        return null;
    }


}
