package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.modello.Ordine;
import antyk03.marketplaceserver.modello.ProdottoOrdine;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOProdottoOrdine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class DAOProdottoOrdineHibernate extends DAOGenericoHibernate<ProdottoOrdine> implements IDAOProdottoOrdine {

    @Override
    public List<ProdottoOrdine> findByIdOrdineAndOrdineStatus (Long idOrdine, EStatus status) {
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<ProdottoOrdine> root = cq.from(ProdottoOrdine.class);
            Root<Ordine> ordineRoot = cq.from(Ordine.class);
            cq.select(root).where(
                cb.and(
                    cb.equal(root.get("idOrdine"), idOrdine),
                    cb.equal(ordineRoot.get("id"), idOrdine),
                    cb.equal(ordineRoot.get("status"), status)
                )
            );
            List<ProdottoOrdine> results = em.createQuery(cq).getResultList();
            return  results.isEmpty() ? new ArrayList<>() : results;
        } catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }


}
