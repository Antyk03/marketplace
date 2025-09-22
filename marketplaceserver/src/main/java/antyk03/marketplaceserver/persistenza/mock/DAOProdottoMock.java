package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.modello.Prodotto;
import antyk03.marketplaceserver.persistenza.IDAOProdotto;

import java.util.ArrayList;
import java.util.List;

public class DAOProdottoMock extends DAOGenericoMock<Prodotto> implements IDAOProdotto {

    public List<Prodotto> findAllBuyable() {
        List<Prodotto> prodotti = new ArrayList<>();
        for (Prodotto prodotto :this.findAll()) {
            if (prodotto.getQuantita()>0) {
                prodotti.add(prodotto);
            }
        }
        return prodotti;
    }

}
