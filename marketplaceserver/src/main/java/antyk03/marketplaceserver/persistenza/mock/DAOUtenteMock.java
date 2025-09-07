package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOUtente;

import java.util.List;

public class DAOUtenteMock extends DAOGenericoMock<Utente> implements IDAOUtente {


    @Override
    public Utente findByEmail(String email) {
        for(Utente u: this.findAll()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
}
