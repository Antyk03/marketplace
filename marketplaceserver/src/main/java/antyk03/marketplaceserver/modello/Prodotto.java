package antyk03.marketplaceserver.modello;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Prodotto {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private int quantita;
    private Long idVenditore;

    public Prodotto(String nome, String descrizione, BigDecimal prezzo, int quantita, Long idVenditore) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.idVenditore = idVenditore;
    }


}
