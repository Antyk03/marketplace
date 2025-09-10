package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAODatiUtente;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAODatiUtenteHibernate extends DAOGenericoHibernate<DatiUtente> implements IDAODatiUtente {

    @Override
    public DatiUtente findByIdUtente(Long idUtente) throws DAOException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<DatiUtente> criteria = builder.createQuery(DatiUtente.class);
        Root<DatiUtente> root = criteria.from(DatiUtente.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("idUtente"), idUtente));

        List<DatiUtente> results = getSession().createQuery(criteria).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

}
