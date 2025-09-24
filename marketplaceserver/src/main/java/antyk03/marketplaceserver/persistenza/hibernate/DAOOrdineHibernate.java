package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.modello.Ordine;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOOrdine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAOOrdineHibernate extends DAOGenericoHibernate<Ordine> implements IDAOOrdine {

    @Override
    public List<Ordine> findByIdUtente (Long idUtente) throws DAOException {
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Ordine> cq = cb.createQuery(Ordine.class);
            Root<Ordine> root = cq.from(Ordine.class);

            cq.select(root);
            cq.where(cb.equal(root.get("idUtente"), idUtente));
            List<Ordine> results = em.createQuery(cq).getResultList();
            return results.isEmpty() ? null : results;
        } catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public Ordine findCarrelloUtente(Long idUtente) throws DAOException {
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Ordine> cq = cb.createQuery(Ordine.class);
            Root<Ordine> root = cq.from(Ordine.class);
            cq.select(root);
            cq.where(cb.and(
                    cb.equal(root.get("idUtente"), idUtente),
                    cb.equal(root.get("status"), EStatus.DRAFT)
            ));
            List<Ordine> results = em.createQuery(cq).getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }
}
