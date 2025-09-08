package antyk03.marketplaceserver.filtri;

import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.persistenza.DAOFactory;
import antyk03.marketplaceserver.persistenza.IDAOUtente;
import antyk03.marketplaceserver.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Priority;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;


@Provider
@Priority(Priorities.AUTHENTICATION)
@Slf4j
public class AutenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final ObjectMapper mapper = new ObjectMapper();
    @Context
    private ResourceInfo resourceInfo;
    private IDAOUtente daoUtente = DAOFactory.getInstance().getDaoUtente();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().startsWith("openapi.") || requestContext.getUriInfo().getPath().startsWith("application.wadl")) {
            return;
        }
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            return;
        }
        Class<?> resourceClass = resourceInfo.getResourceClass();
        Method method = resourceInfo.getResourceMethod();
        if (resourceClass.isAnnotationPresent(PermitAll.class) || method.isAnnotationPresent(PermitAll.class)) {
            log.debug("Accesso ad una risorsa pubblica");
            return;
        }
        String intestazioneAutorizzazione = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (!isTokenAutorizzazionePresente(intestazioneAutorizzazione)) {
            interrompiRichiesta("Token di autorizzazione non presente", requestContext);
            return;
        }
        String token = intestazioneAutorizzazione.substring(AUTHENTICATION_SCHEME.length()).trim();
        try {
            String emailUtente = JWTUtil.verificaToken(token);
            log.debug("Utente verificato: {}", emailUtente);
            SecurityContext originalContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new AppSecurityContext(emailUtente, originalContext.isSecure(), "Bearer"));
            /*Utente utente = daoUtente.findByEmail(emailUtente);
            if (utente == null) {
                interrompiRichiesta("Utente " + emailUtente + " non trovato", requestContext);
                return;
            }*/
        } catch (Exception ex) {
            log.error("Errore durante la validazione del token {}", ex.getMessage(), ex);
            interrompiRichiesta("Token di autorizzazione non valido", requestContext);
        }
    }

    private boolean isTokenAutorizzazionePresente(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void interrompiRichiesta(String messaggio, ContainerRequestContext requestContext) {
        ObjectNode json = mapper.createObjectNode();
        json.put("error", messaggio);
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity(json.toPrettyString()).build()
        );
    }

    class AppSecurityContext implements SecurityContext {
        private final String username;
        private final boolean secure;
        private final String authenticationScheme;

        public AppSecurityContext(String username, boolean secure, String authenticationScheme) {
            this.username = username;
            this.secure = secure;
            this.authenticationScheme = authenticationScheme;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> username;
        }

        @Override
        public boolean isSecure() {
            return this.secure;
        }

        @Override
        public String getAuthenticationScheme() {
            return authenticationScheme;
        }

        @Override
        public boolean isUserInRole (String role) {
            return true;
        }
    }

}
