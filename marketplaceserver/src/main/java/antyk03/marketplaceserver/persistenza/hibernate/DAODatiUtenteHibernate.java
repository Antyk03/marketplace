package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAODatiUtente;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAODatiUtenteHibernate extends DAOGenericoHibernate<DatiUtente> implements IDAODatiUtente {

    @Override
    public DatiUtente findByIdUtente(Long idUtente) throws DAOException {
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<DatiUtente> cq = cb.createQuery(DatiUtente.class);
            Root<DatiUtente> root = cq.from(DatiUtente.class);

            cq.select(root);
            cq.where(cb.equal(root.get("idUtente"), idUtente));

            List<DatiUtente> results = em.createQuery(cq).getResultList();
            return  results.isEmpty() ? null : results.get(0);
        } catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

}
