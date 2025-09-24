package antyk03.marketplaceserver.filtri.eccezioni;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class ValidationExceptionFilter implements ExceptionMapper<ValidationException> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Response toResponse(ValidationException ex) {
        log.error("Errore durante la gestione della richiesta: {}", ex.getMessage(), ex);

        ObjectNode json = mapper.createObjectNode();
        json.put("error", ex.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(json.toPrettyString()).build();
    }

}
