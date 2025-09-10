package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAOUtenteHibernate extends DAOGenericoHibernate<Utente> implements IDAOUtente {


    @Override
    public Utente findByEmail(String email) throws DAOException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Utente> criteria = builder.createQuery(Utente.class);
        Root<Utente> root = criteria.from(Utente.class);
        criteria.select(root);
        criteria.where(builder.equal(builder.lower(root.get("email")), email.toLowerCase()));
        List<Utente> results = getSession().createQuery(criteria).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

}
