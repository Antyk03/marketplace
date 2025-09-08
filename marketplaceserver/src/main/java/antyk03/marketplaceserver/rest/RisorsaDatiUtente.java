package antyk03.marketplaceserver.rest;


import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.service.ServiceDatiUtente;
import antyk03.marketplaceserver.service.ServiceUtenti;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

@Path("/dati")
@RequestScoped
@Slf4j
public class RisorsaDatiUtente {

    @Inject
    private ServiceUtenti serviceUtenti;
    @Inject
    private ServiceDatiUtente serviceDatiUtente;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public DatiUtenteDTO getInformazioni() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceDatiUtente.getInformazioni(email);
    }
}
