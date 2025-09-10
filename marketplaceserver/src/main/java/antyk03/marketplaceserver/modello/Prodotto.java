package antyk03.marketplaceserver.modello;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "prodotti")
@Data
@NoArgsConstructor
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(length = 1000)
    private String descrizione;
    @Column(nullable = false)
    private BigDecimal prezzo;
    @Column(nullable = false)
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
