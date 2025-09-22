package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.modello.Prodotto;
import antyk03.marketplaceserver.persistenza.IDAOProdotto;

import java.util.ArrayList;
import java.util.List;

public class DAOProdottoHibernate extends DAOGenericoHibernate<Prodotto> implements IDAOProdotto {

    public List<Prodotto> findAllBuyable() {
        List<Prodotto> prodotti = new ArrayList<>();
        return prodotti;
    }

}
