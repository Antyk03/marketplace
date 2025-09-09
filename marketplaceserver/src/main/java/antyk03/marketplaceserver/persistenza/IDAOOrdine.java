package antyk03.marketplaceserver.persistenza;

import antyk03.marketplaceserver.modello.Ordine;

import java.util.List;

public interface IDAOOrdine extends IDAOGenerico<Ordine> {

    public List<Ordine> findByIdUtente(Long idUtente);

    public Ordine findCarrelloUtente(Long idUtente);
}
