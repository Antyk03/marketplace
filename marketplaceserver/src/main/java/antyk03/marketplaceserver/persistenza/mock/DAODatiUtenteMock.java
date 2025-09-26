package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Prodotto;
import antyk03.marketplaceserver.persistenza.IDAODatiUtente;

public class DAODatiUtenteMock extends DAOGenericoMock<DatiUtente> implements IDAODatiUtente {

    @Override
    public DatiUtente findByIdUtente (Long idUtente) {
        for (DatiUtente du: findAll()) {
            if (du.getIdUtente() == idUtente) {
                return du;
            }
        }
        return  null;
    }


}
