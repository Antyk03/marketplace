package antyk03.marketplaceserver.persistenza;

import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.ProdottoOrdine;

import java.util.List;

public interface IDAOProdottoOrdine extends IDAOGenerico<ProdottoOrdine> {

    public List<ProdottoOrdine> findByIdOrdineAndOrdineStatus(Long idOrdine, EStatus status);
}
