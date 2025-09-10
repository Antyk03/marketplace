package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.enums.EStatus;
import antyk03.marketplaceserver.modello.Ordine;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOOrdine;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAOOrdineHibernate extends DAOGenericoHibernate<Ordine> implements IDAOOrdine {
    @Override
    public List<Ordine> findByIdUtente(Long idUtente) throws DAOException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Ordine> criteria = builder.createQuery(Ordine.class);
        Root<Ordine> root = criteria.from(Ordine.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("idUtente"), idUtente));

        return getSession().createQuery(criteria).getResultList();
    }

    @Override
    public Ordine findCarrelloUtente(Long idUtente) throws DAOException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Ordine> criteria = builder.createQuery(Ordine.class);
        Root<Ordine> root = criteria.from(Ordine.class);
        criteria.select(root);

        Predicate byUtente = builder.equal(root.get("idUtente"), idUtente);
        Predicate byStatus = builder.equal(root.get("status"), EStatus.DRAFT);
        criteria.where(builder.and(byUtente, byStatus));

        List<Ordine> results = getSession().createQuery(criteria).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
