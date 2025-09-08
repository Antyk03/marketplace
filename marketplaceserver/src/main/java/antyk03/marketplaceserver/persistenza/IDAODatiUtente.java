package antyk03.marketplaceserver.persistenza;

import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Utente;

public interface IDAODatiUtente extends IDAOGenerico<DatiUtente> {

    public DatiUtente findByIdUtente (Long idUtente);

}
