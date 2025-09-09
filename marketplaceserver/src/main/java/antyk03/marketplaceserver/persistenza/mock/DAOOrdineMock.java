package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.Ordine;
import antyk03.marketplaceserver.persistenza.IDAOOrdine;

import java.util.ArrayList;
import java.util.List;

public class DAOOrdineMock extends DAOGenericoMock<Ordine> implements IDAOOrdine {

    @Override
    public List<Ordine> findByIdUtente(Long idUtente) {
        List<Ordine> ordiniUtente = new ArrayList<>();
        for (Ordine o: findAll()) {
            if (o.getIdUtente() == idUtente) {
                ordiniUtente.add(o);
            }
        }
        return ordiniUtente;
    }

    @Override
    public Ordine findCarrelloUtente(Long idUtente) {
        for (Ordine o: findAll()) {
            if (o.getIdUtente() == idUtente && o.getStatus().equals(EStatus.DRAFT)) {
                return o;
            }
        }
        return null;
    }
}
