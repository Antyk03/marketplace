package antyk03.marketplaceserver.modello;

import lombok.Data;

@Data
public class Utente {

    private Long id;
    private String email;
    private String password;

    public Utente(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
