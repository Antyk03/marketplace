package antyk03.marketplaceserver.modello;

import antyk03.marketplaceserver.enums.ERuolo;
import lombok.Data;

@Data
public class DatiUtente {

    private Long id;
    private Long idUtente;
    private String username;
    private String email;
    private ERuolo ruolo;

    public DatiUtente(Long idUtente, String username ,String email, ERuolo ruolo) {
        this.idUtente = idUtente;
        this.username = username;
        this.email = email;
        this.ruolo = ruolo;
    }

}
