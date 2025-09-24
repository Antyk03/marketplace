package antyk03.marketplaceserver.filtri;

import antyk03.marketplaceserver.enums.EStrategiaPersistenza;
import antyk03.marketplaceserver.modello.Configurazione;
import jakarta.annotation.Priority;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Provider
@Slf4j
@Priority(100)
public class HibernateFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String EM_PROPERTY = "ENTITY_MANAGER";

    @Override
    public void filter (ContainerRequestContext requestContext) throws IOException {
        if (!Configurazione.getInstance().getStrategiaDb().equals(EStrategiaPersistenza.DB_HIBERNATE)) {
            return;
        }
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            return;
        }
        log.debug("Apro l'EntityManager e avvio la transazione");
        EntityManager em = Configurazione.getInstance().getEmf().createEntityManager();
        em.getTransaction().begin();
        requestContext.setProperty(EM_PROPERTY, em);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        EntityManager em = (EntityManager) requestContext.getProperty(EM_PROPERTY);
        if (em == null) {
            return;
        }
        try {
            if (em.getTransaction().isActive()) {
                log.debug("Commit della transazione");
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                log.debug("Rollback della transazione a causa di un errore");
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
          em.close();
        }
    }

    public static EntityManager getEntityManager(ContainerRequestContext requestContext) {
        return (EntityManager) requestContext.getProperty(EM_PROPERTY);
    }

}
