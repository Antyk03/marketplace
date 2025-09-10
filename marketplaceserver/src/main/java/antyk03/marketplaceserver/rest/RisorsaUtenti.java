package antyk03.marketplaceserver.rest;


import antyk03.marketplaceserver.modello.DatiUtente;
import antyk03.marketplaceserver.modello.Utente;
import antyk03.marketplaceserver.modello.dto.DatiUtenteDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.modello.dto.RegistrazioneUtenteDTO;
import antyk03.marketplaceserver.modello.dto.UtenteDTO;
import antyk03.marketplaceserver.service.ServiceUtenti;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Path("")
@RequestScoped
@Slf4j
public class RisorsaUtenti {

    @Inject
    private ServiceUtenti serviceUtenti;

    @Context
    private SecurityContext securityContext;

    @PermitAll
    @POST
    @Path("/utenti/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login (@NotNull @Valid UtenteDTO utenteDTO) {
        return serviceUtenti.login(utenteDTO);
    }

    /*
    @PermitAll
    @GET
    @Path("/utenti/check")
    @Produces(MediaType.TEXT_PLAIN)
    public String check() {
        log.info("Eseguo controllo GET /utenti/check");
        return "Servizio utenti attivo!";
    }*/

    @GET
    @Path("/admin/utenti")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatiUtenteDTO> getUtenti () {
        String email = securityContext.getUserPrincipal().getName();
        return serviceUtenti.getUtenti(email);
    }

    @GET
    @Path("/admin/prodotti")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdottoDTO> getProdotti () {
        String email = securityContext.getUserPrincipal().getName();
        return serviceUtenti.getProdotti(email);
    }

    @POST
    @Path("/admin/{idUtente}/blocca")
    @Produces(MediaType.APPLICATION_JSON)
    public DatiUtenteDTO bloccaOSbloccaUtente(@PathParam("idUtente") @NotNull Long idUtente) {
        String email = securityContext.getUserPrincipal().getName();
        return serviceUtenti.bloccaOSbloccaUtente(idUtente, email);
    }

    @POST
    @Path("/admin/{idProdotto}/rimuovi")
    @Produces(MediaType.TEXT_PLAIN)
    public void rimuoviProdotto(@PathParam("idProdotto") @NotNull Long idProdotto) {
        String email = securityContext.getUserPrincipal().getName();
        serviceUtenti.rimuoviProdotto(idProdotto, email);
    }

    @PermitAll
    @POST
    @Path("/utenti/registra")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String registraUtente(RegistrazioneUtenteDTO registrazioneUtenteDTO) {
        UtenteDTO utenteDTO = registrazioneUtenteDTO.getUtenteDTO();
        DatiUtenteDTO datiUtenteDTO = registrazioneUtenteDTO.getDatiUtenteDTO();
        if (!utenteDTO.getEmail().equalsIgnoreCase(datiUtenteDTO.getEmail())) {
            throw new IllegalArgumentException("Email non coincidono.");
        }
        return serviceUtenti.registraUtente(utenteDTO, datiUtenteDTO);
    }

    @POST
    @Path("/utenti/elimina")
    @Produces(MediaType.TEXT_PLAIN)
    public void eliminaUtente() {
        String email = securityContext.getUserPrincipal().getName();
        serviceUtenti.eliminaUtente(email);
    }
}
