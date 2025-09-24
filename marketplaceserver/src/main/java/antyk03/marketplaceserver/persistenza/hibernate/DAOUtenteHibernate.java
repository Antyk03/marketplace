package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.filtri.HibernateFilter;
import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAOUtenteHibernate extends DAOGenericoHibernate<Utente> implements IDAOUtente {


    @Override
    public Utente findByEmail(String email) throws DAOException {
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Utente> cq = cb.createQuery(Utente.class);
            Root<Utente> root = cq.from(Utente.class);

            cq.select(root);
            cq.where(cb.equal(cb.lower(root.get("email")), email.toLowerCase()));

            List<Utente> results = em.createQuery(cq).getResultList();
            return results.isEmpty() ? null : results.get(0);
        }catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

}
