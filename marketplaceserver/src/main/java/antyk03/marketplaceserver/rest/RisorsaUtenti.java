package antyk03.marketplaceserver.rest;


import antyk03.marketplaceserver.modello.dto.UtenteDTO;
import antyk03.marketplaceserver.service.ServiceUtenti;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;


@RequestScoped
@Path("/utenti")
@Slf4j
public class RisorsaUtenti {

    @Inject
    private ServiceUtenti serviceUtenti;

    @Context
    private SecurityContext securityContext;

    @PermitAll
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login (@NotNull @Valid UtenteDTO utenteDTO) {
        return serviceUtenti.login(utenteDTO);
    }

    /*
    @PermitAll
    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    public String check() {
        log.info("Eseguo controllo GET /utenti/check");
        return "Servizio utenti attivo!";
    }*/

}
