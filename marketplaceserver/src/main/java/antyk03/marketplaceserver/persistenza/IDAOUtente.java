package antyk03.marketplaceserver.persistenza;

import antyk03.marketplaceserver.modello.Utente;

public interface IDAOUtente extends IDAOGenerico<Utente> {

    public Utente findByEmail(String email);

}
