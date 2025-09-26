package antyk03.marketplaceserver.persistenza;

import antyk03.marketplaceserver.modello.Prodotto;

import java.util.List;

public interface IDAOProdotto extends IDAOGenerico<Prodotto> {

    public List<Prodotto> findAllBuyable();

    public List<Prodotto> findByIdVenditore(Long idVenditore);

}
