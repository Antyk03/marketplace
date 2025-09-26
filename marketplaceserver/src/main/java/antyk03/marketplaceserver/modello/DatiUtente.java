package antyk03.marketplaceserver.modello;

import antyk03.marketplaceserver.enums.ERuolo;
import antyk03.marketplaceserver.enums.EStatoUtente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dati_utente")
@Data
@NoArgsConstructor
public class DatiUtente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUtente;

    private String username;

    private String email;
    @Enumerated(EnumType.STRING)
    private ERuolo ruolo;
    @Enumerated(EnumType.STRING)
    private EStatoUtente statoUtente;
    @Transient
    private List<Prodotto> prodotti = new ArrayList<>();

    public DatiUtente(Long idUtente, String username,String email, ERuolo ruolo, EStatoUtente statoUtente) {
        this.idUtente = idUtente;
        this.username = username;
        this.email = email;
        this.ruolo = ruolo;
        this.statoUtente = statoUtente;
    }

}
