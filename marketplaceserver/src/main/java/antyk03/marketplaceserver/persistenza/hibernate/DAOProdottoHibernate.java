package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.modello.Prodotto;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOProdotto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class DAOProdottoHibernate extends DAOGenericoHibernate<Prodotto> implements IDAOProdotto {

    @Override
    public List<Prodotto> findAllBuyable() {
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Prodotto> cq = cb.createQuery(Prodotto.class);
            Root<Prodotto> root = cq.from(Prodotto.class);
            cq.select(root);
            cq.where(cb.gt(root.get("quantita"), 0));
            List<Prodotto> results = em.createQuery(cq).getResultList();
            return results.isEmpty() ? null : results;
        } catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

}
