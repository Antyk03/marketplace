package antyk03.marketplaceserver.persistenza.hibernate;

import antyk03.marketplaceserver.modello.Configurazione;
import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOGenerico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DAOGenericoHibernate<T> implements IDAOGenerico<T> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public DAOGenericoHibernate() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected EntityManager getEntityManager() throws DAOException {
        return Configurazione.getInstance().getEmf().createEntityManager();
    }

    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T makePersistent(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);

            T result;
            if (id != null && em.find(persistentClass, id) != null) {
                log.debug("L'entità esiste già, eseguo merge: {}", entity);
                result = em.merge(entity);
            } else {
                log.debug("Nuova entità, eseguo persist: {}", entity);
                em.persist(entity);
                result = entity;
            }
            em.getTransaction().commit();
            return result;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            log.error("Errore nel persist dell'entità {}",entity, ex);
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public void makeTransient(T entity) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            T managedEntity = null;
            if (id != null) {
                managedEntity = em.find(persistentClass, id);
            }
            if (managedEntity!= null) {
                log.debug("Elimino l'entità dal DB: {}", managedEntity);
                em.remove(managedEntity);
            } else {
                log.debug("L'entità non esiste più: {}", entity);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findById(Long id) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            return em.find(persistentClass, id);
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() throws DAOException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(persistentClass);
            Root<T> root = cq.from(persistentClass);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<T> findByEqual(String attributeName, Object attributeValue) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(persistentClass);
            Root<T> root = cq.from(persistentClass);
            cq.select(root).where(cb.equal(root.get(attributeName), attributeValue));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<T> findByEqual (Map<String, Object> andSelections) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(persistentClass);
            Root<T> root = cq.from(persistentClass);
            cq.select(root);

            List<Predicate> predicates = new ArrayList<>();
            andSelections.forEach((k, v) -> predicates.add(cb.equal(root.get(k), v)));

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<T> findByEqualIgnoreCase(String attributeName, String attributeValue) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(persistentClass);
            Root<T> root = cq.from(persistentClass);
            cq.select(root);
            Predicate caseInsensitive = cb.equal(cb.upper(root.get(attributeName)), attributeValue.toUpperCase());
            cq.where(caseInsensitive);
            return em.createQuery(cq).getResultList();
        } catch (Exception ex) {
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public T saveOrMerge(T obj, Long id) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            T persistentObject = em.find(persistentClass, id);
            if (persistentObject != null) {
                log.debug("Get ha trovato l'oggetto con id {}", id);
                em.getTransaction().commit();
                return persistentObject;
            } else {
                em.persist(obj);
                em.getTransaction().commit();
                return obj;
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public T merge(T obj) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T merged = em.merge(obj);
            em.getTransaction().commit();
            return merged;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw new DAOException(ex);
        } finally {
            em.close();
        }
    }


}
