package antyk03.marketplaceserver.modello;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Prodotto {

    private Long id;
    private String nome;
    private String descrizione;
    private float prezzo;
    private int quantita;

    public Prodotto(String nome, String descrizione, float prezzo, int quantita) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }


}
