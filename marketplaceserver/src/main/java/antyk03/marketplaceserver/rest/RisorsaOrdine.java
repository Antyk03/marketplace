package antyk03.marketplaceserver.rest;

import antyk03.marketplaceserver.modello.dto.OrdineDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoDTO;
import antyk03.marketplaceserver.modello.dto.ProdottoOrdineDTO;
import antyk03.marketplaceserver.service.ServiceDatiUtente;
import antyk03.marketplaceserver.service.ServiceOrdine;
import antyk03.marketplaceserver.service.ServiceUtenti;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

import javax.print.attribute.standard.Media;
import java.util.List;


@Path("/ordine")
@RequestScoped
@Slf4j
public class RisorsaOrdine {

    @Inject
    private ServiceDatiUtente serviceDatiUtente;
    @Inject
    private ServiceUtenti serviceUtenti;
    @Inject
    private ServiceOrdine serviceOrdine;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/visualizza")
    @Produces(MediaType.APPLICATION_JSON)
    public OrdineDTO visualizzaCarrello() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceOrdine.visualizzaCarrello(email);
    }

    //Utente aggiunge prodotto al carrello, un utente USER seleziona un prodotto e lo aggiunge al carrello (ORDINE STATUS.DRAFT)
    @POST
    @Path("/aggiungi/{idProdotto}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long aggiungiAlCarrello(ProdottoOrdineDTO prodottoOrdineDTO) {
        String email = securityContext.getUserPrincipal().getName();
        return serviceOrdine.aggiungiAlCarrello(prodottoOrdineDTO, email);
    }

    @POST
    @Path("/acquista")
    @Produces(MediaType.APPLICATION_JSON)
    public OrdineDTO effettuaOrdine() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceOrdine.effettuaOrdine(email);
    }

    @GET
    @Path("/storico")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrdineDTO> visualizzaStorico() {
        String email = securityContext.getUserPrincipal().getName();
        return serviceOrdine.visualizzaStorico(email);
    }

    @POST
    @Path("/{idProdotto}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public void rimuoviProdotto (@NotNull @PathParam("idProdotto") Long idProdotto) {
        String email = securityContext.getUserPrincipal().getName();
        serviceOrdine.rimuoviProdotto(idProdotto, email);
    }
}
