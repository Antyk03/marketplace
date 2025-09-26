package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.Ordine;
import antyk03.marketplaceserver.modello.ProdottoOrdine;
import antyk03.marketplaceserver.persistenza.DAOFactory;
import antyk03.marketplaceserver.persistenza.IDAOOrdine;
import antyk03.marketplaceserver.persistenza.IDAOProdottoOrdine;

import java.util.ArrayList;
import java.util.List;

public class DAOProdottoOrdineMock extends DAOGenericoMock<ProdottoOrdine> implements IDAOProdottoOrdine {

    private IDAOOrdine daoOrdine = DAOFactory.getInstance().getDaoOrdine();

    @Override
    public List<ProdottoOrdine> findByIdOrdineAndOrdineStatus (Long idOrdine, EStatus status) {
        List<ProdottoOrdine> prodottiOrdine = new ArrayList<>();
        for (Ordine ordine : daoOrdine.findAll()) {
            if (ordine.getStatus().equals(status)) {
                for (ProdottoOrdine po : this.findAll()) {
                    if (po.getIdOrdine() == idOrdine) {
                        prodottiOrdine.add(po);
                    }
                }
            }
        }
        return prodottiOrdine;
    }

}
