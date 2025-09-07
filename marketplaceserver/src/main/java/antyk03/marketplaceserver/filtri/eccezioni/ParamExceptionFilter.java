package antyk03.marketplaceserver.filtri.eccezioni;


import antyk03.marketplaceserver.persistenza.hibernate.DAOUtilHibernate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ParamException;

@Slf4j
@Provider
//Il filtro è necessario in queanto ParamException non verrebbe catturato dal GenericExceptionFilter, ma da un altro filtro più specifico di Jersey
public class ParamExceptionFilter implements ExceptionMapper<ParamException> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Response toResponse(ParamException ex) {
        try {
            if (DAOUtilHibernate.getSessionFactory().getCurrentSession().isOpen()) {
                log.debug("Effettuo il rollback della transazione");
                DAOUtilHibernate.rollback();
            }
        } catch (Exception e) {
            log.warn("Impossibile effettuare il rollback della transazione {}", e.getMessage(), e);
        }
        log.error("Errore durante la gestione della richiesta  {}", ex.getMessage(), ex);
        ObjectNode json = mapper.createObjectNode();
        json.put("error", ex.getParameterName() + ": " + ex.getCause().getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(json.toPrettyString()).build();
    }

}
